package Toolkit;

/**
 * Write a description of TestReadWebpage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TestReadFileWebpage {
private StringBuilder result;

public TestReadFileWebpage() {
result = new StringBuilder();
}
    
public void testSaveImage() throws Exception {
String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048);
String destinationFile = rwsf.autoName(imageUrl);
rwsf.setFile(imageUrl, destinationFile);
rwsf.readProcess(0);
	}

public void testReadAndSave_Images() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
ReadFileToArrayList rfal = new ReadFileToArrayList(10000,p);
ArrayList<String> list = rfal.readProcess();
//System.out.println(list);
for (int i=0; i < list.size();i++)
  {String url = list.get(i);
  ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048);
  String destinationFile = rwsf.autoName(url);
  rwsf.setFile(url, destinationFile);
  rwsf.readProcess(0);
  }
}

public void testReadFileToArrayList() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
ReadFileToArrayList rfal = new ReadFileToArrayList(10000,p);
ArrayList<String> list = rfal.readProcess();   
System.out.println(list);    
}

public void testGetWebpage () throws FileNotFoundException,IOException, NullPointerException {
String url = "http://doi.org/"+"10.5258/SOTON/397556";
ReadWebpageToStringBuilder rws = new ReadWebpageToStringBuilder(10000,url);
StringBuilder page = rws.readProcess();
System.out.println(page);
    }

public void testReadFileToStringBuilder() throws IOException {
Path p = Paths.get("./example_files/image_urls.txt");
ReadFileToStringBuilder rfsb = new ReadFileToStringBuilder(10000,p);
StringBuilder sb = rfsb.readProcess();   
System.out.println(sb); 
}
    
}
