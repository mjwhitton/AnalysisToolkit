package Toolkit;
/**
 * Write a description of Utils here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Utils {

public void writeFile (String fname, StringBuilder sb) throws FileNotFoundException, IOException {
PrintWriter output = new PrintWriter(new File("./output_files/"+fname));
  output.write(sb.toString());
  output.close();
}

public StringBuilder arrayListToString (ArrayList<String> list, String separator, boolean newLine) {
StringBuilder sb = new StringBuilder();

for (int i =0; i < list.size(); i++)
  {String s = list.get(i);
  sb.append(s+separator);
  if(newLine==true) {sb.append("\n");}
  }
return sb;    
}

public void writeFile2(String fname, StringBuilder sb) throws FileNotFoundException, IOException {
        try {
            File f = new File("./output_files/"+fname);
            FileOutputStream is = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(sb.toString());
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file "+fname);
        }
    }

}