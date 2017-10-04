package Toolkit;

/**
 * Write a description of TestReadCsv here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TestReadCsv {
private StringBuilder sb;
    
TestReadCsv() {
sb = new StringBuilder();
}

public void testExtractFileSelect() throws FileNotFoundException, IOException {
OpenFile of = new OpenFile("CSV files", "csv");
File f = of.getFile();
Path fname = f.toPath();
System.out.println(fname);
String[] columns = {"Authors with affiliations", "EID"};
String searchTerm = "Kajita";
String breaker = ";";
ReadProcessCsv cs = new ReadProcessCsv(columns, breaker, "", fname);
int[] retcol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, retcol);
Utils ut = new Utils();
StringBuilder sb = ut.arrayListToString(results, "", true);
System.out.println(sb);
ut.writeFile("kajita.csv", sb);
}

public void testExtractText() throws FileNotFoundException, IOException {
Path fname = Paths.get("./example_files/search1.csv");
String[] columns = {"Authors with affiliations", "EID"};
String searchTerm = "Kajita";
String breaker = ";";
ReadProcessCsv cs = new ReadProcessCsv(columns, breaker, "", fname);
int[] retcol = {0};
ArrayList<String> results = cs.readProcess("Extract Text", searchTerm, retcol);
Utils ut = new Utils();
StringBuilder sb = ut.arrayListToString(results, "", true);
System.out.println(sb);
ut.writeFile("kajita.csv", sb);
}
  
}
