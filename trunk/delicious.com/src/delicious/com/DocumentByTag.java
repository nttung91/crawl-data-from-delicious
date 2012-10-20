
package delicious.com;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dao.DocumentDAO;
import model.pojo.Document;
import org.json.simple.parser.ParseException;


/**
 *
 * @author THANHTUNG
 */
public class DocumentByTag extends Thread {

    String name;
    int position;
    List<String> list=null;
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeliciousCom.class);
    int state;
    public DocumentByTag(String name, int startPosition,ThreadGroup tg) {
        super(tg,name);
        this.state =0;
        this.name = name;
        this.position = startPosition;
         list = DatabaseHelper.getMostPopularTag(1000);
         logger.info(String.format("%s started.\n", name));
        System.out.printf("%s started.\n", name);
        start();
    }

    @Override
    public void run() {

       getRecentBookmarkByTag(list,name,position,position+100);
       //getDocumentHistory();
    }

    public void getRecentBookmarkByTag(List<String> list,String threadname,int start,int end) {
        ArrayList<String> l = null;
       
       // Random random = new Random();
        int j=0;
        try {
            for (j=start;j<=end && j<list.size();j++) {
         //       int j = random.nextInt(list.size());
                l = DeliciousHepler.getRecentListBookmarkByTag(list.get(j), 1000);
                if (l != null && l.size() > 0) {
                    int count =0;
                    for (int i = 0; i < l.size(); i++) {
                        //System.out.println(threadname+" -- #" + i);
                        try {
                          if (DeliciousHepler.getAndSaveBookmarkOnly(l.get(i))) count++;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                     logger.info(String.format(threadname+" -- So Link lay dc tu tag #%d %s: %d/%d",j,list.get(j),count,l.size()));
                     System.out.println(String.format(threadname+" -- So Link lay dc tu tag #%d %s: %d/%d",j, list.get(j),count,l.size()));
                     
                }
            }
        } catch ( ParseException ex) {
            this.state = j;
            System.out.println(threadname+" -------------------------------Thread end-------------------------");
            Logger.getLogger(DeliciousCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(threadname+" -------------------------------Thread end-------------------------");
    }
    public void getRecentTag() throws InterruptedException{
         int i = 10;
        int delaytime = 50;
        while (true){
               delaytime +=DeliciousHepler.getRecentTag();
               
              System.out.println("Pause "+delaytime+" sec.........................");
              Thread.sleep(delaytime*1000);
        }
    }
    public void getDocumentHistory(){
        System.out.println("Reading......document............");
        
        DocumentDAO dao = new DocumentDAO();
        List<Document> list =dao.getList1();
        System.out.println("Lay xong ds document!");
        for (int i=3143;i<list.size();i++){
            try {
                DeliciousHepler.getAndSaveBookmarkHistoryByDocument(list.get(i));
            } catch (ParseException ex) {
                Logger.getLogger(DeliciousCom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        
         // getDocumentHistory();
//        
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
         
         DocumentByTag[] threads = new DocumentByTag[2];
          for (int i = 0;i<threads.length;i++){
                    if (threads[i]==null) {
                        threads[i] = new DocumentByTag("Thread #"+(i+1),i*150, rootGroup);
                        
                    }
                }
        int[] restartCount = new int[threads.length];
        boolean stopAll = false;
        int maxRetry = 10000;
        while (true){
               
                for (int i = 0;i<threads.length;i++){
                    if (restartCount[i]>maxRetry) {
                        stopAll = true;
                    }
                    if (stopAll) {
                        return;
                    }
                    if (!threads[i].isAlive()) {
                        Thread.sleep(3000);
                        int current = threads[i].state;
                        System.out.println("cf" + current);
                        threads[i] = new DocumentByTag("Thread #"+(i+1),i*150+current, rootGroup);
                        System.out.println("#"+threads[i].getName()+"Start again");
                        restartCount[i]++;
                    }
                }
                
        }
    }
}
