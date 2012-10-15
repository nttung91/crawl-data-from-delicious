/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.tools;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author THANHTUNG
 */
public class MD5Convertor {
    public static String Convert2MD5(String source){
        try {
            byte[] arr = source.getBytes("UTF-8");
            MessageDigest md=null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(MD5Convertor.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] thedigest = md.digest(arr);
            BigInteger bigInt = new BigInteger(1,thedigest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
             hashtext = "0"+hashtext;
            }
           return hashtext;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MD5Convertor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
