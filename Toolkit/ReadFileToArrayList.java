package Toolkit;


/**
 * Write a description of ReadFileToArrayList here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ReadFileToArrayList extends ReadAndProcess{
public ReadFileToArrayList(int ln, Path p) {
lines = ln;
atEnd = false;
path = p;
list = new ArrayList<String>();
}

public void readProcess(int startnum) throws IOException {
start = startnum;
readFile();
}

public ArrayList<String> readProcess() throws IOException {
readProcess(0);
return this.getList();
}
 
protected void process(BufferedReader rd) throws IOException {
try {
readLineToArrayList(rd);
  }
catch (IOException e) {System.err.println("Caught IOException: " + e.getMessage());}
}


}

