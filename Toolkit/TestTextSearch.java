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
  
public void testingMany()throws FileNotFoundException, IOException {
Path p = Paths.get("./example_files/?");
ReadFileToStringBuilder rsb = new ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
TextSearch tx = new TextSearch(true, 10);
tx.loadStringBuilder(sbTx);
String[] terms = {"???"};
ArrayList<String> urls = tx.searchMany(terms);
Utils ut = new Utils();
StringBuilder sb = ut.arrayListToString(urls, "", true);
System.out.println(urls);
ut.writeFile("urls.txt", sb);
}

public void test2() throws FileNotFoundException, IOException {
Path p = Paths.get("./test/id.txt"); 
ReadFileToStringBuilder rsb = new ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
System.out.println(sbTx);   
}
    
}
