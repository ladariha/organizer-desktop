/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

/**
 *
 * @author Vladimír Řiha
 */
public class StringChecker {

    public String getAscii(String t) {
        String text = t;
        text = text.replaceAll("á", "&#225;");
        text = text.replaceAll("ä", "&#228;");
        text = text.replaceAll("č", "&#269;");
        text = text.replaceAll("ď", "&#271;");
        text = text.replaceAll("é", "&#233;");
        text = text.replaceAll("ě", "&#283;");
        text = text.replaceAll("ë", "&#235;");
        text = text.replaceAll("í", "&#237;");
        text = text.replaceAll("ň", "&#328;");
        text = text.replaceAll("ó", "&#243;");
        text = text.replaceAll("ö", "&#246;");
        text = text.replaceAll("ř", "&#345;");
        text = text.replaceAll("š", "&#353;");
        text = text.replaceAll("ť", "&#357;");
        text = text.replaceAll("ú", "&#250;");
        text = text.replaceAll("ů", "&#367;");
        text = text.replaceAll("ü", "&#252;");
        text = text.replaceAll("ý", "&#253;");
        text = text.replaceAll("ž", "&#382;");
        text = text.replaceAll("Á", "&#193;");
        text = text.replaceAll("Ä", "&#196;");
        text = text.replaceAll("Č", "&#268;");
        text = text.replaceAll("Ď", "&#270;");
        text = text.replaceAll("É", "&#201;");
        text = text.replaceAll("Ě", "&#282;");
        text = text.replaceAll("Ë", "&#203;");
        text = text.replaceAll("Í", "&#205;");
        text = text.replaceAll("Ň", "&#327;");
        text = text.replaceAll("Ó", "&#211;");
        text = text.replaceAll("Ö", "&#214;");
        text = text.replaceAll("Ř", "&#344;");
        text = text.replaceAll("Š", "&#352;");
        text = text.replaceAll("Ť", "&#356;");
        text = text.replaceAll("Ú", "&#218;");
        text = text.replaceAll("Ů", "&#366;");
        text = text.replaceAll("Ü", "&#220;");
        text = text.replaceAll("Ý", "&#221;");
        text = text.replaceAll("Ž", "&#381;");
        return text;
    }

    public String removeAscii(String t) {
        String text = t;
try{
        text=text.replaceAll("&#225;", "á");
text=text.replaceAll("&#228;", "ä");
text=text.replaceAll("&#269;", "č");
text=text.replaceAll("&#271;", "ď");
text=text.replaceAll("&#233;", "é");
text=text.replaceAll("&#283;", "ě");
text=text.replaceAll("&#235;", "ë");
text=text.replaceAll("&#237;", "í");
text=text.replaceAll("&#328;", "ň");
text=text.replaceAll("&#243;", "ó");
text=text.replaceAll("&#246;", "ö");
text=text.replaceAll("&#345;", "ř");
text=text.replaceAll("&#353;", "š");
text=text.replaceAll("&#357;", "ť");
text=text.replaceAll("&#250;", "ú");
text=text.replaceAll("&#367;", "ů");
text=text.replaceAll("&#252;", "ü");
text=text.replaceAll("&#253;", "ý");
text=text.replaceAll("&#382;", "ž");
text=text.replaceAll("&#193;", "Á");
text=text.replaceAll("&#196;", "Ä");
text=text.replaceAll("&#268;", "Č");
text=text.replaceAll("&#270;", "Ď");
text=text.replaceAll("&#201;", "É");
text=text.replaceAll("&#282;", "Ě");
text=text.replaceAll("&#203;", "Ë");
text=text.replaceAll("&#205;", "Í");
text=text.replaceAll("&#327;", "Ň");
text=text.replaceAll("&#211;", "Ó");
text=text.replaceAll("&#214;", "Ö");
text=text.replaceAll("&#344;", "Ř");
text=text.replaceAll("&#352;", "Š");
text=text.replaceAll("&#356;", "Ť");
text=text.replaceAll("&#218;", "Ú");
text=text.replaceAll("&#366;", "Ů");
text=text.replaceAll("&#220;", "Ü");
text=text.replaceAll("&#221;", "Ý");
text=text.replaceAll("&#381;", "Ž");
        return text;
}catch(Exception a){
return "";
}
    }
    
    public  String getHash(String t) {
        byte[] password = {00};
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-256");
            md5.update(t.getBytes());
            password = md5.digest();
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < password.length; i++) {
                hexString.append(Integer.toHexString(0xFF & password[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return password.toString();
    }

    public static String removeAccents(String t) {
        t = Normalizer.normalize(t, Normalizer.Form.NFD);
        t = (t.replaceAll("[^\\p{ASCII}]", ""));
        return t;
    }
}

