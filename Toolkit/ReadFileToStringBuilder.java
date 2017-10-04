package Toolkit;


/**
 * Write a description of ReadToStringBuilder here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public class ReadFileToStringBuilder extends ReadAndProcess {

public ReadFileToStringBuilder(int ln, Path p) {
lines = ln;
atEnd = false;
path = p;
sbTx = new StringBuilder();
}

public void readProcess(int startnum) throws IOException {
start = startnum;
readFile();
}

public StringBuilder readProcess() throws IOException {
readProcess(0);
return this.getSB();
}

protected void process(BufferedReader rd) throws IOException {
try {
  readLineToStringBuilder(rd);
  }
catch (IOException e) {System.err.println("Caught IOException: " + e.getMessage());}
}

}
   
