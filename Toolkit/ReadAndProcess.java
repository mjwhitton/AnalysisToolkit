package Toolkit;
/**
 * Write a description of ReadAndProcess here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class ReadAndProcess {
protected int lines;
protected boolean atEnd;
protected int start;
protected Path path;
protected StringBuilder sbTx;
protected ArrayList<String> list;
protected String[] columns;

abstract public void readProcess(int startnum) throws IOException;
    
protected void readFile() {
try {
  BufferedReader rd = Files.newBufferedReader(path);
  process(rd);}
catch (IOException e1) {System.err.println("Caught IOException ("+path+"): " + e1.getMessage());} 
}

protected void readCsv() throws IOException {
Reader rd = new FileReader(path.toFile());
CSVParser parser = new CSVParser(rd, 
  CSVFormat.DEFAULT.withHeader());
  for (CSVRecord record : parser) {
    //Check required header are there
    String[] cm = new String[columns.length];
    for (int i = 0; i < columns.length; i++) {
        cm[i] = record.get(columns[i]);
      }
    processCsv(cm);
    }
  parser.close(); 
}

protected void process(BufferedReader rd) throws IOException {
//Define if needed;    
}

protected void processCsv(String[] cm) {
//Define if needed;    
}

protected InputStream getUrl(String url) {
//Create null version of key objects
InputStream is = null;
String webText = "";
//Open url, if this is not found print the error message
try {
  is = new URL(url).openStream();}
catch (NullPointerException e1) {System.err.println("Caught FileNotFoundException ("+url+"): " + e1.getMessage());}
catch (IOException e2) {System.err.println("Caught IOException ("+url+"): " + e2.getMessage());}
return is;
     }

protected void getWebpage(InputStream is) {
try {
  //Read in the url
  BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
  //Call readAll to convert to a string
  process(rd);
  //Stop reading the url
  is.close();}
  //If there is an error print the message
catch (NullPointerException e1) {System.err.println("Caught: NullPointerException" + e1.getMessage());}
catch (IOException e2) {System.err.println("Caught IOException" + e2.getMessage());}
}
 
protected void saveFile(String destinationFile, InputStream is, int byteRate) {
try {	
  OutputStream os = new FileOutputStream("./output_files/"+destinationFile);
    byte[] b = new byte[byteRate];
	int length;
	while ((length = is.read(b)) != -1) {
	  os.write(b, 0, length);
	  }
    is.close();
	os.close();
    }
catch (IOException e) {System.err.println("Caught: NullPointerException" + e.getMessage());}
	}

protected void readCharToStringBuilder(BufferedReader rd) throws IOException {
int cp;
//Read in one character at a time
while ((cp = rd.read()) != -1) {
  //Append to the StringBuilder
  sbTx.append((char) cp);
  }
  }  
  
protected void readLineToStringBuilder(BufferedReader rd) throws IOException {
int counts=0;
int stop = lines+start;
while(counts<stop) {
  String line = rd.readLine();
  if(line==null) {atEnd = true; break;}
  else if (counts>=start) {sbTx.append(line);}
  counts +=1;
  }
}

protected void readLineToArrayList(BufferedReader rd) throws IOException {
int counts=0;
while(true) {
  String line = rd.readLine();
  if(line==null) {atEnd = true; break;}
  else {list.add(line);}
  counts +=1;
  }
}

public StringBuilder getSB() {
return  sbTx;
}

public ArrayList<String> getList() {
return  list;
}

public Boolean atEnd() {
return atEnd;   
}

}