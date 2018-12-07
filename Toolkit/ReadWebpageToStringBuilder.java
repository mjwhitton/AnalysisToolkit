package Toolkit;


/**
 * This method reads in a webpage into a StringBuilder object.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */
import java.io.*;
import javax.swing.JOptionPane;

public class ReadWebpageToStringBuilder extends ReadAndProcess {
private String url;

public ReadWebpageToStringBuilder (int ln, String uri) {
lines = ln;
atEnd = false;
sbTx = new StringBuilder();
url = uri;
}

@Override
public void readProcess(int startnum) {
start = startnum;
InputStream is = getUrl(url);
getWebpage(is);
}

public StringBuilder readProcess() throws IOException {
readProcess(0);
return this.getSB();
}

@Override
public void process(BufferedReader rd) {
try {
readLineToStringBuilder(rd);
  }
catch(Exception ex){utl.logError(ex,"Error when writing the file",log);}
}

}
