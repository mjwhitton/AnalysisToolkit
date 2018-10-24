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
    
public TestReadCsv() {
sb = new StringBuilder();
copyExampleFiles(false);
}

public TestReadCsv(boolean overwrite) {
sb = new StringBuilder();
copyExampleFiles(overwrite);
}

private void copyExampleFiles(boolean overwrite) {
//copy files from GitHub if needed
Utils ut = new Utils("./example_files/");
File f = new File("./example_files/search1.csv");
if (!f.exists() || overwrite==true) {ut.copyFileFromGithub("search1.csv");}
File f2 = new File("./example_files/scopus-search-results-Takaaki-Kajita.csv");
if (!f2.exists() || overwrite==true) {ut.copyFileFromGithub("scopus-search-results-Takaaki-Kajita.csv");}
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

public void testNewExtractFileSelect() throws FileNotFoundException, IOException {
Toolkit.OpenFile of = new Toolkit.OpenFile("CSV files", "csv");
File f = of.getFile();
Path fname = f.toPath();
//System.out.println(fname);
Toolkit.Utils ut = new Toolkit.Utils();
String searchTerm = ut.getValue("Enter the keyword to start extracting text from", "Enter the Search Term");
String breaker = ut.getValue("Enter the symbol (; etc.) that indicates to stop extracting text", "Enter the Breaker value");
boolean cError = true;
int colnum = 0;
while (cError == true)
  {
  String aCol = ut.getValue("Enter the column to extract text from (1,2,3,etc.)", "Enter the Analysis Column");
  try {colnum = Integer.parseInt(aCol); if (colnum > 0) {cError = false;}}
  catch (Exception ex) {continue;}
  }
int[] AnalyseCol = {colnum-1};
Toolkit.ReadProcessCsv cs = new Toolkit.ReadProcessCsv(breaker, "@", fname, true);
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, AnalyseCol);

StringBuilder sr = ut.arrayListToString(results, "", true);
JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files/", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile(ut.autoName(f)+".txt", sr);}
}
