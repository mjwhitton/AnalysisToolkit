package Toolkit;


/**
 * A method to save a file from the internet.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */
import java.io.*;

public class ReadWebpageSaveFile extends ReadAndProcess {
private String url;
private String destinationFile;
private boolean autoName;
private int byteRate;

public ReadWebpageSaveFile (int bytes) {
atEnd = false;
byteRate = bytes;
Utils ut = new Utils();
outputPath="./output_files/";
}

public ReadWebpageSaveFile (int bytes, String path) {
atEnd = false;
byteRate = bytes;
Utils ut = new Utils();
outputPath=path;
}

public String autoName(String fileurl) {
//This method creates the file name based on the url.
String autoname = fileurl.substring(fileurl.lastIndexOf('/') + 1, fileurl.length());
return autoname;
}

public void setFile(String fileurl, String filename) {
url = fileurl;
destinationFile = filename;
}

@Override
public void process(BufferedReader rd) {
//Not needed
}

@Override
public void readProcess(int startnum) {
start = startnum;
InputStream is = getUrl(url);
saveFile(destinationFile, is, byteRate);
}
     
}
