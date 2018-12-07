package Toolkit;
/**
 * This is a class used to contain methods shared across multiple other classes in the Toolkit.
 * This also creates the required output_files folder
 * 
 * @author (Michael Whitton) 
 * @version (9/10/17)
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.JOptionPane;

public class Utils {
  
private String folder;
private StringBuilder errorLog;
private boolean log;

public Utils() {
folder = "./output_files/";
createFolder(folder);
errorLog = new StringBuilder();
log = false;
}

public Utils(String fname) {
folder = fname;
createFolder(folder);
errorLog = new StringBuilder();
log = false;
}

public void createFolder(String fldr) {
String fol = fldr.substring(0, fldr.length()-1);
try
  {
  Files.createDirectories(Paths.get(fol));
    }  
catch(Exception ex){logError(ex,"Error when creating " + folder, log);}
}

public void setFolder(String fol) {
createFolder(fol);
folder = fol;
}

public void writeFile (String fname, StringBuilder sb) {
String text = sb.toString();
writeFile(fname, text);
}

public void writeFile (String fname, String text) {
//Method to save a StringBuilder as a text file.
try{
PrintWriter output = new PrintWriter(new File(folder+fname));
  output.write(text);
  output.close();}
catch(Exception ex){logError(ex,"Error when creating 'output_files' folder", log);}
}

public String autoName(File file) {
String f = file.getName();
int i = f.lastIndexOf(".");
if (i != -1) {f=f.substring(0, i);}
return f;
}

public StringBuilder arrayListToString (ArrayList<String> list, String separator, boolean newLine) {
//Method to convert an ArrayList object to a StringBuilder, e.g. to write to a file.
StringBuilder sb = new StringBuilder();
for (int i =0; i < list.size(); i++)
  {String s = list.get(i);
  sb.append(s).append(separator);
  if(newLine==true) {sb.append("\n");}
  }
return sb;    
}

public void copyFileFromGithub(String file) {
String fol = folder.substring(1,folder.length());
copyFileFromGithub(file, file, fol);
}

public void copyFileFromGithub(String file, String fileName) {
String fol = folder.substring(1,folder.length());
copyFileFromGithub(file, fileName, fol);
}

public void copyFileFromGithub(String file, String fileName, String fol) {
try
  {
   String rep = "https://raw.githubusercontent.com/mjwhitton/AnalysisToolkit/master"+fol;
  ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048, folder);
  rwsf.setFile(rep+file, fileName);
  rwsf.readProcess(0);
  }
catch(Exception ex){logError(ex,"Error when saving "+file, log);}
}

public void copyGeneralFiles() {
copyGeneralFiles(false);
}

public void copyGeneralFiles(boolean overwrite) {
Utils ux = new Utils("./");
File f1 = new File("./License.txt");
if (!f1.exists() || overwrite==true) {ux.copyFileFromGithub("License.txt");}
File f2 = new File("./README.TXT");
if (!f2.exists() || overwrite==true) {ux.copyFileFromGithub("README.TXT");}
}

public void writeFile2(String fname, StringBuilder sb) throws FileNotFoundException, IOException {
//An alternative way of coding writeFile.
        try {
            File f = new File(folder+fname);
            FileOutputStream is = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(sb.toString());
            w.close();
        }
        catch(Exception ex){logError(ex,"Error when writing the file", log);}
}

public ArrayList<String> getConfigValue(String name, String message, String heading) {
ArrayList<String> key = new ArrayList<>();
File f = new File(folder+name);
if (!f.exists())
  {
  String s = JOptionPane.showInputDialog(null, message, heading, JOptionPane.QUESTION_MESSAGE);
  if (s==null) {s = "N/A";}
  else if (s.equals("")) {s = "N/A";}
  writeFile(name, s);
  key.add(s);
  }  
else
  {Path p = f.toPath();
  Toolkit.ReadFileToArrayList rfsb = new Toolkit.ReadFileToArrayList(10000,p);
  key = rfsb.readProcess();   
  }
return key;
    }

public void writeLog() {
Utils ux = new Utils("./");
ux.writeFile("errorlog.txt", errorLog);
}

public void logError(Exception ex, String item, boolean type) {
if (type == false) {JOptionPane.showMessageDialog(null, "Caught Exception"+ex+item, "Error", JOptionPane.ERROR_MESSAGE);}
else {errorLog.append("Caught Exception"+ex+item+"\n");}
}

public String getValue (String message, String heading) {
String s = JOptionPane.showInputDialog(null, message, heading, JOptionPane.QUESTION_MESSAGE);
if (s==null) {s = "N/A";}
else if (s.equals("")) {s = "N/A";}
return s;
}

}