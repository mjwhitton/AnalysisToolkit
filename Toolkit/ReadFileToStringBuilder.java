package Toolkit;


/**
 * This method reads a file into a StringBuilder object.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */
import java.io.*;
import java.nio.file.*;
import javax.swing.JOptionPane;
public class ReadFileToStringBuilder extends ReadAndProcess {

public ReadFileToStringBuilder(int ln, Path p) {
lines = ln;
atEnd = false;
path = p;
sbTx = new StringBuilder();
}

@Override
public void readProcess(int startnum) {
start = startnum;
readFile();
}

public StringBuilder readProcess() {
readProcess(0);
return this.getSB();
}

@Override
public void process(BufferedReader rd) {
try {
  readLineToStringBuilder(rd);
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when writing the file" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
}

}
   
