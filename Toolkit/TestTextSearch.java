package Toolkit;
/**
 * This method runs standard tests for TextSearch.
 * 
 * @author (Michael Whitton) 
 * @version (9/10/17)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.nio.file.*;
import java.net.*;
import javax.swing.JOptionPane;
public class TestTextSearch {
  
public void testSearchManyStartEndKeywords()throws FileNotFoundException, IOException {
Path p = Paths.get("./example_files/ComputerScienceArticles.htm");
Toolkit.ReadFileToStringBuilder rsb = new Toolkit.ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
Toolkit.TextSearch tx = new Toolkit.TextSearch();
tx.loadStringBuilder(sbTx);
String[] terms = {"http://thecolbertreport.cc.com", "\">"};
ArrayList<String> urls = tx.searchMany(terms);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(urls, ",", true);
JOptionPane.showMessageDialog(null, sb.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("urls.txt", sb);
}

public void testSearchManyStarkKeywordPlusOffset()throws FileNotFoundException, IOException {
Path p = Paths.get("./example_files/ComputerScienceArticles.htm");
Toolkit.ReadFileToStringBuilder rsb = new Toolkit.ReadFileToStringBuilder(1000000, p);
StringBuilder sbTx = rsb.readProcess();
Toolkit.TextSearch tx = new Toolkit.TextSearch(true, 30);
tx.loadStringBuilder(sbTx);
String[] terms = {"http://www.dukelearntoprogram.com/"};
ArrayList<String> urls = tx.searchMany(terms);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(urls, ",", true);
JOptionPane.showMessageDialog(null, sb.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile("urls.txt", sb);
}

    
}
