package Toolkit;


/**
 * Write a description of ReadWebpageToStringBuilder here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

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
readCharToStringBuilder(rd);
  }
catch (IOException e) {System.err.println("Caught IOException: " + e.getMessage());}
}

}
