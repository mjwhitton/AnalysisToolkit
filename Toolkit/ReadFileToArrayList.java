package Toolkit;


/**
 * This method reads a file into an ArrayList.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

public class ReadFileToArrayList extends ReadAndProcess{
public ReadFileToArrayList(int ln, Path p) {
lines = ln;
atEnd = false;
path = p;
list = new ArrayList<>();
}

@Override
public void readProcess(int startnum) {
start = startnum;
readFile();
}

public ArrayList<String> readProcess() {
readProcess(0);
return this.getList();
}
 
@Override
public void process(BufferedReader rd) {
try {
readLineToArrayList(rd);
  }
catch(Exception ex){utl.logError(ex,"Error when writing the file",log);}
}

}

