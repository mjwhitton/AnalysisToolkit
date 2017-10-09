package Toolkit;
/**
 * Write a description of TestTextSearch here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.nio.file.*;
import java.net.*;
public class TestTextSearch {
  
public void testSearchManyStartEndKeywords()throws FileNotFoundException, IOException {
Path p = Paths.get("./example_files/ComputerScienceArticles.htm");
ReadFileToStringBuilder rsb = new ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
TextSearch tx = new TextSearch();
tx.loadStringBuilder(sbTx);
String[] terms = {"http://thecolbertreport.cc.com", "\">"};
ArrayList<String> urls = tx.searchMany(terms);
Utils ut = new Utils();
StringBuilder sb = ut.arrayListToString(urls, ",", true);
System.out.println(sb);
ut.writeFile("urls.txt", sb);
}

public void testSearchManyStarkKeywordPlusOffset()throws FileNotFoundException, IOException {
Path p = Paths.get("./example_files/ComputerScienceArticles.htm");
ReadFileToStringBuilder rsb = new ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
TextSearch tx = new TextSearch(true, 30);
tx.loadStringBuilder(sbTx);
String[] terms = {"http://www.dukelearntoprogram.com/"};
ArrayList<String> urls = tx.searchMany(terms);
Utils ut = new Utils();
StringBuilder sb = ut.arrayListToString(urls, ",", true);
System.out.println(sb);
ut.writeFile("urls.txt", sb);
}

    
}
