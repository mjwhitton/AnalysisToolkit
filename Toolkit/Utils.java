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

public Utils() {
createFolder();
folder = "./output_files/";
}

public Utils(String fname) {
createFolder();
folder = fname;
}

private void createFolder() {
try{Files.createDirectories(Paths.get("./output_files"));}    
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when creating 'output_files' folder" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
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
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when creating 'output_files' folder" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
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
        catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when writing the file" +ex, "Error", JOptionPane.ERROR_MESSAGE);};}
    }