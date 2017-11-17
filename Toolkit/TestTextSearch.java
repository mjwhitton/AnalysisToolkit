package Toolkit;
/**
 * This method runs standard tests for TextSearch.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */
import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;
public class TestTextSearch {

public TestTextSearch() {
copyExampleFiles(false);
}

public TestTextSearch(boolean overwrite) {
copyExampleFiles(overwrite);
}

private void copyExampleFiles(boolean overwrite) {
//copy files from GitHub if needed
Utils ut = new Utils("./example_files/");
File f = new File("./example_files/ComputerScienceArticles.htm");
if (!f.exists() || overwrite==true) {ut.copyFileFromGithub("ComputerScienceArticles.htm");}
}
  
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
