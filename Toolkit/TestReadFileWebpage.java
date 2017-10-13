package Toolkit;

/**
 * This method runs standard tests for ReadWebpageSaveFile.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

public class TestReadFileWebpage {
private StringBuilder result;

public TestReadFileWebpage() {
result = new StringBuilder();
}
    
public void testSaveImage() throws Exception {
String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
Toolkit.ReadWebpageSaveFile rwsf = new Toolkit.ReadWebpageSaveFile(2048);
String destinationFile = rwsf.autoName(imageUrl);
rwsf.setFile(imageUrl, destinationFile);
rwsf.readProcess(0);
JOptionPane.showMessageDialog(null, "Image file has been saved to /output_files/", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
	}

public void testReadAndSave_Images() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
Toolkit.ReadFileToArrayList rfal = new Toolkit.ReadFileToArrayList(10000,p);
ArrayList<String> list = rfal.readProcess();
//System.out.println(list);
for (int i=0; i < list.size();i++)
  {String url = list.get(i);
  Toolkit.ReadWebpageSaveFile rwsf = new Toolkit.ReadWebpageSaveFile(2048);
  String destinationFile = rwsf.autoName(url);
  rwsf.setFile(url, destinationFile);
  rwsf.readProcess(0);
  }
JOptionPane.showMessageDialog(null, "Image files have been saved to /output_files/", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}

public void testReadFileToArrayList() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
Toolkit.ReadFileToArrayList rfal = new Toolkit.ReadFileToArrayList(10000,p);
ArrayList<String> list = rfal.readProcess();
JOptionPane.showMessageDialog(null, list.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}

public void testGetWebpage () throws FileNotFoundException,IOException, NullPointerException {
String url = "http://doi.org/"+"10.5258/SOTON/397556";
Toolkit.ReadWebpageToStringBuilder rws = new Toolkit.ReadWebpageToStringBuilder(10000,url);
StringBuilder page = rws.readProcess();
JOptionPane.showMessageDialog(null, page.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
    }

public void testReadFileToStringBuilder() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
Toolkit.ReadFileToStringBuilder rfsb = new Toolkit.ReadFileToStringBuilder(10000,p);
StringBuilder sb = rfsb.readProcess();   
JOptionPane.showMessageDialog(null, sb.toString(), "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}
    
}
