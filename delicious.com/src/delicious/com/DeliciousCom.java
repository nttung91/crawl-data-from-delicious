/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delicious.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.dao.DocumentDAO;
import model.pojo.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author THANHTUNG
 */
public class DeliciousCom extends Thread {

    String name;
    int delay;
    List<String> list=null;
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeliciousCom.class);
    public DeliciousCom(String name, int delay,ThreadGroup tg) {
        super(tg,name);
        this.delay = delay;
        this.name = name;
         list = DatabaseHelper.getMostPopularTag(250);
         logger.info(String.format("%s started.\n", name));
        System.out.printf("%s started.\n", name);
        start();
    }

    @Override
    public void run() {

       // getRecentBookmarkByTag(list,name);
       getDocumentHistory();
    }

    public void getRecentBookmarkByTag(List<String> list,String threadname) {
        ArrayList<String> l = null;
       
        Random random = new Random();

        try {
            while (true) {
                int j = random.nextInt(list.size());
                l = DeliciousHepler.getRecentListBookmarkByTag(list.get(j), 1000);
                if (l != null && l.size() > 0) {
                    logger.info(String.format(threadname+" -- So post lay dc tu tag #%d: %d\n", j, l.size()));
                    System.out.printf(threadname+" -- So post lay dc tu tag #%d: %d\n", j, l.size());

                    for (int i = 0; i < l.size(); i++) {
                        //System.out.println(threadname+" -- #" + i);
                        try {
                            DeliciousHepler.getAndSaveBookmarkInfo(l.get(i));
                        } catch (Exception ex) {
                        }
                    }

                }
            }
        } catch (ParseException ex) {
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
        for (int i=1806;i<list.size();i++){
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
         
         DeliciousCom[] threads = new DeliciousCom[1];
          for (int i = 0;i<threads.length;i++){
                    if (threads[i]==null) {
                        threads[i] = new DeliciousCom("Thread #"+(i+1),0, rootGroup);
                    }
                }
        int[] restartCount = new int[threads.length];
        boolean stopAll = false;
        int maxRetry = 100;
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
                        threads[i] = new DeliciousCom("Thread #"+(i+1),0, rootGroup);
                        System.out.println("#"+threads[i].getName()+"Start again");
                        restartCount[i]++;
                    }
                }
                
        }
    }
}
