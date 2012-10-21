/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delicious.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author THANHTUNG
 */
public class RecentTag {


  
    public void getRecentTag() throws InterruptedException{
         int i = 10;
        int delaytime = 50;
        while (true){
            try {
                delaytime +=DeliciousHepler.getRecentTag();
            } catch (MalformedURLException ex) {
                Logger.getLogger(RecentTag.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RecentTag.class.getName()).log(Level.SEVERE, null, ex);
            }
               
              System.out.println("Pause "+delaytime+" sec.........................");
              Thread.sleep(delaytime*1000);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
              
        RecentTag rt = new RecentTag();
        rt.getRecentTag();
    }
}
