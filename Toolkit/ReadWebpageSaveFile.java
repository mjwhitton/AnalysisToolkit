package Toolkit;


/**
 * Write a description of ReadWebpageSaveFile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ReadWebpageSaveFile extends ReadAndProcess {
private String url;
private String destinationFile;
private boolean autoName;
private int byteRate;

public ReadWebpageSaveFile (int bytes) {
atEnd = false;
byteRate = bytes;
}

public String autoName(String fileurl) {
String autoname = fileurl.substring(fileurl.lastIndexOf('/') + 1, fileurl.length());
return autoname;
}

public void setFile(String fileurl, String filename) {
url = fileurl;
destinationFile = filename;
}

public void readProcess(int startnum) throws IOException {
start = startnum;
InputStream is = getUrl(url);
saveFile(destinationFile, is, byteRate);
}
     
}
