package Toolkit;


/**
 * This method reads a file into an ArrayList.
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
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when writing the file" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
}

}

