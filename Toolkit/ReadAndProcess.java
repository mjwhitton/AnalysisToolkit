package Toolkit;
/**
 * An abstract method for reading files and manipulating them.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */

import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.net.URL;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;

public abstract class ReadAndProcess {
protected int lines;
protected boolean atEnd;
protected int start;
protected Path path;
protected StringBuilder sbTx;
protected ArrayList<String> list;
protected String[] columns;
protected String outputPath;

abstract public void readProcess(int startnum) ;
    
protected void readFile() {
try {
  BufferedReader rd = Files.newBufferedReader(path);
  process(rd);}
catch(Exception ex){JOptionPane.showMessageDialog(null, "Caught Exception"+ex+path, "Error", JOptionPane.ERROR_MESSAGE);};
}


abstract public void process(BufferedReader rd);


protected InputStream getUrl(String url) {
//Create null version of key objects
InputStream is = null;
String webText = "";
//Open url, if this is not found print the error message
try {
  is = new URL(url).openStream();}
catch(Exception ex){JOptionPane.showMessageDialog(null, "Caught Exception"+ex+url, "Error", JOptionPane.ERROR_MESSAGE);};
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
catch(Exception ex){JOptionPane.showMessageDialog(null, "Caught Exception"+ex, "Error", JOptionPane.ERROR_MESSAGE);};
}
 
protected void saveFile(String destinationFile, InputStream is, int byteRate) {
try {	
  OutputStream os = new FileOutputStream(outputPath+destinationFile);
    byte[] b = new byte[byteRate];
	int length;
	while ((length = is.read(b)) != -1) {
	  os.write(b, 0, length);
	  }
    is.close();
	os.close();
    }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Caught Exception"+ex, "Error", JOptionPane.ERROR_MESSAGE);};
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
  else if (counts>=start) {sbTx.append(line+"\n");}
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