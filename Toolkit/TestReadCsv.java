package Toolkit;

/**
 * This method runs standard tests for ReadProcessCSV.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

public class TestReadCsv {
private final StringBuilder sb;
    
TestReadCsv() {
sb = new StringBuilder();
}

public void testExtractFileSelect() throws FileNotFoundException, IOException {
Toolkit.OpenFile of = new Toolkit.OpenFile("CSV files", "csv");
File f = of.getFile();
Path fname = f.toPath();
//System.out.println(fname);
String searchTerm = "Kajita";
String breaker = ";";
ReadProcessCsv cs = new ReadProcessCsv(breaker, "@", fname, true);
int[] AnalyseCol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, AnalyseCol);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sr = ut.arrayListToString(results, "", true);
JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files/", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("kajita.txt", sr);}

public void testExtractText() throws FileNotFoundException, IOException {
Path fname = Paths.get("./example_files/search1.csv");
String searchTerm = "Kajita";
String breaker = ";";
ReadProcessCsv cs = new ReadProcessCsv(breaker, "@", fname, true);
int[] AnalyseCol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, AnalyseCol);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sr = ut.arrayListToString(results, "", true);
JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files/", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("kajita.txt", sr);}
  
}
