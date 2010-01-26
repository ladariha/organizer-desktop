/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.mozilla.intl.chardet.HtmlCharsetDetector;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

/**
 *
 * @author Lada Riha
 */
public class CharsetDetector implements nsICharsetDetectionObserver {

    public String encoding="";
    private String file = "";
    public CharsetDetector(String file){
    this.file = file;
    }


    public  void getCharset() throws FileNotFoundException, IOException {

            nsDetector det = new nsDetector(nsPSMDetector.ALL);
            det.Init(this);
            File file = new File(this.file);
            FileReader fr =  new FileReader(file);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream imp = new BufferedInputStream(fis);
            byte[] buf = new byte[1024];
            int len;
            boolean done = false;
            boolean isAscii = true;
            while ((len = imp.read(buf, 0, buf.length)) != -1) {
                if (isAscii) {
                    isAscii = det.isAscii(buf, len);
                }
                if (!isAscii && !done) {
                    done = det.DoIt(buf, len, false);
                }
            }
            det.DataEnd();
       
        } 

    public void Notify(String charset) {
        HtmlCharsetDetector.found = true;
                    this.encoding= charset;
    }

}
