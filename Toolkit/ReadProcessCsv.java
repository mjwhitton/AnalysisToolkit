package Toolkit;


/**
 * This method reads in a CSV file and manipulates it.
 * 
 * @author (Michael Whitton) 
 * @version (9/10/17)
 */

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ReadProcessCsv extends ReadAndProcess {
private String breaker;
private String separator;
private String process;
private String searchTerm; 
private int[] returnColumn;

public ReadProcessCsv(String[] ColumnHeaders, String breakerText, String separatorText, Path p) {
path = p;
columns = ColumnHeaders;
breaker = breakerText;
separator = separatorText;  
list = new ArrayList<String>();
    
}

public ArrayList<String> readProcess(String proc, String search, int[] retColumn) throws IOException {
setTerms(proc, search, retColumn);
readProcess(0);
return this.getList();
}

public void readProcess(int startnum) throws IOException {
start = startnum;
readCsv();

}

protected void processCsv(String[] cm) {
String x = processColumn(cm[0], searchTerm, process);
if(x!=breaker) {cm[0] = x;
  StringBuilder sb = new StringBuilder();
  for (int k = 0; k < returnColumn.length; k++) {
    int n = returnColumn[k];
    sb.append(cm[n]+separator);
    }
  list.add(sb.toString());
}    

}

public void setTerms(String proc, String search, int[] retColumn) {
process = proc;
searchTerm = search;
returnColumn = retColumn;
      }

private String processColumn(String entry, String searchTerm, String process) {
if(process.equals("Simple Search") && entry.equals(searchTerm)) {return entry;}    

else if(process.equals("Extract Text")) {
  int pos1 = entry.indexOf(searchTerm);
  int pos2 = entry.indexOf(breaker, pos1);
  if (pos2 == -1) {pos2 = entry.length();}
  if (pos1 != -1) {return entry.substring(pos1,pos2);}
    }
return breaker;
     }
     
    }
