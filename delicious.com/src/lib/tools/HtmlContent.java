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

    public String getHtmlContent(String url) throws MalformedURLException, IOException {
        BufferedReader in = null;

        String res = "";
        URL oracle = new URL(url);

        in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {

            res = inputLine;
        }
        in.close();
        return res;




    }
}
