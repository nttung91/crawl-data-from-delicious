
package delicious.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
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
    int start;
    List<String> list=null;
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeliciousCom.class);
    int state;
    int peroid;
    public DocumentByTag(String name,int thread,int currentPosition,int numberOfThread,ThreadGroup tg) {
        super(tg,name);
        this.state =0;
        this.name = name;
        
        
         list = DatabaseHelper.getMostPopularTag();
         this.peroid = list.size()/numberOfThread+1;
         this.start = thread * this.peroid+currentPosition;
         logger.info(String.format("%s started.\n", name));
        System.out.printf("%s started.\n", name);
        start();
    } 
    @Override
    public void run() {

       getRecentBookmarkByTag(list,name,start,start+peroid);
       //getDocumentHistory();
    }

    public void getRecentBookmarkByTag(List<String> list,String threadname,int start,int end) {
        ArrayList<String> l = null;
       
       // Random random = new Random();
        int j=0;
        try {
            for (j=start;j<=end && j<list.size();j++) {
         //       int j = random.nextInt(list.size());
                if (list.get(j).contains(" ")) continue;
            //    Thread.currentThread().sleep(500);
                l = DeliciousHepler.getPopularListBookmarkByTag(list.get(j), 1000);
                System.out.println("after"+l.size());
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
        } catch (MalformedURLException ex) {
             this.state = j;
               ex.printStackTrace();
            Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
             this.state = j;
               ex.printStackTrace();
            Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
        } catch ( ParseException ex) {
            this.state = j;
            System.out.println(threadname+" -------------------------------Thread end-------------------------");
            ex.printStackTrace();
            Logger.getLogger(DeliciousCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(threadname+" -------------------------------Thread end-------------------------");
    }
    public void getRecentTag() throws InterruptedException{
         int i = 10;
        int delaytime = 50;
        while (true){
            try {
                delaytime +=DeliciousHepler.getRecentTag();
                
               System.out.println("Pause "+delaytime+" sec.........................");
               Thread.sleep(delaytime*1000);
            } catch (MalformedURLException ex) {
                Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void getDocumentHistory(){
        System.out.println("Reading......document............");
        
        DocumentDAO dao = new DocumentDAO();
        List<Document> list =dao.getList1();
        System.out.println("Lay xong ds document!");
        for (int i=3143;i<list.size();i++){
            try {
                try {
                    DeliciousHepler.getAndSaveBookmarkHistoryByDocument(list.get(i));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DocumentByTag.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        
         DocumentByTag[] threads = new DocumentByTag[3];
          for (int i = 0;i<threads.length;i++){
                    if (threads[i]==null) {
                        threads[i] = new DocumentByTag("Thread #"+(i+1),i,0,threads.length, rootGroup);
                        
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
                        threads[i] = new DocumentByTag("Thread #"+(i+1),i,current,threads.length, rootGroup);
                        System.out.println("#"+threads[i].getName()+"Start again");
                        restartCount[i]++;
                    }
                }
                
        }
    }
}
