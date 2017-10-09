package Toolkit;


/**
 * This method reads in a webpage into a StringBuilder object.
 * 
 * @author (Michael Whitton) 
 * @version (9/10/17)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

public class ReadWebpageToStringBuilder extends ReadAndProcess {
private String url;

public ReadWebpageToStringBuilder (int ln, String uri) {
lines = ln;
atEnd = false;
sbTx = new StringBuilder();
url = uri;
}

public void readProcess(int startnum) throws IOException {
start = startnum;
InputStream is = getUrl(url);
getWebpage(is);
}

public StringBuilder readProcess() throws IOException {
readProcess(0);
return this.getSB();
}

protected void process(BufferedReader rd) throws IOException {
try {
readLineToStringBuilder(rd);
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when writing the file" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
}

}
