/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author THANHTUNG
 */
public class HtmlContent {

    public String getHtmlContent(String url) {
        BufferedReader in = null;
        try {
            String res = "";
            URL oracle = new URL(url);
            try {
                in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));
            } catch (Exception ex)  {
                if (in == null) {
                    return null;
                }
            }
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                res = inputLine;
            }
            in.close();
            return res;

        } catch (IOException ex) {
            Logger.getLogger(HtmlContent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HtmlContent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
