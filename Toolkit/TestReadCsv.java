package Toolkit;

/**
 * This method runs standard tests for ReadProcessCSV.
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

public class TestReadCsv {
private StringBuilder sb;
    
TestReadCsv() {
sb = new StringBuilder();
}

public void testExtractFileSelect() throws FileNotFoundException, IOException {
Toolkit.OpenFile of = new Toolkit.OpenFile("CSV files", "csv");
File f = of.getFile();
Path fname = f.toPath();
//System.out.println(fname);
String[] columns = {"Authors with affiliations", "EID"};
String searchTerm = "Kajita";
String breaker = ";";
Toolkit.ReadProcessCsv cs = new Toolkit.ReadProcessCsv(columns, breaker, "", fname);
int[] retcol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, retcol);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(results, "", true);
JOptionPane.showMessageDialog(null, sb.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("kajita.csv", sb);
}

public void testExtractText() throws FileNotFoundException, IOException {
Path fname = Paths.get("./example_files/search1.csv");
String[] columns = {"Authors with affiliations", "EID"};
String searchTerm = "Kajita";
String breaker = ";";
Toolkit.ReadProcessCsv cs = new Toolkit.ReadProcessCsv(columns, breaker, "", fname);
int[] retcol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, retcol);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(results, "", true);
JOptionPane.showMessageDialog(null, sb.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("kajita.csv", sb);
}
  
}
