package Toolkit;


/**
 * This method reads in a CSV file and manipulates it.
 * 
 * @author (Michael Whitton) 
 * @version (13/10/17)
 */

import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ReadProcessCsv {
protected String breaker;
protected String separator;
protected String process;
protected String searchTerm; 
protected int[] analysisColumn;
protected Path path;
protected ArrayList<String> list;
protected int start;
protected Map<String,Integer> map;
protected boolean returnAll;
protected boolean returnList;
protected List<CSVRecord> csvList;

protected ReadProcessCsv() {
//Default constructor to allow classes to extend
list = new ArrayList<>();
} 

public ReadProcessCsv(String breakerText, String separatorText, Path p) {
path = p;
breaker = breakerText;
separator = separatorText;  
list = new ArrayList<>();
returnAll = false;
returnList = false;
}

public ReadProcessCsv(String breakerText, String separatorText, Path p, boolean retAll) {
path = p;
breaker = breakerText;
separator = separatorText;  
list = new ArrayList<>();
returnAll = retAll;
returnList = false;
}

public ReadProcessCsv(String breakerText, String separatorText, Path p, boolean retAll, boolean retList) {
path = p;
breaker = breakerText;
separator = separatorText;  
list = new ArrayList<>();
returnAll = retAll;
returnList = retList;
}

public ArrayList<String> readProcess(String proc, String search, int[] analysisCol) throws IOException {
setTerms(proc, search, analysisCol);
readProcess(0);
return this.getList();
}

public void readProcess(int startnum) throws IOException {
start = startnum;
readCsv();
}

public void setTerms(String proc, String search, int[] analysisCol) {
process = proc;
searchTerm = search;
analysisColumn = analysisCol;
}

protected String processColumn(String entry) {
if(process.equals("Simple Search") && entry.equals(searchTerm)) {return entry;}    

else if(process.equals("Extract Text"))
  {
  int pos1 = entry.indexOf(searchTerm);
  int pos2 = entry.indexOf(breaker, pos1);
  if (pos2 == -1) {pos2 = entry.length();}
  if (pos1 != -1) {return entry.substring(pos1,pos2);}
  }
return breaker;
}
     
protected void readCsv() throws IOException {
Reader rd = new FileReader(path.toFile());
CSVParser parser = new CSVParser(rd, CSVFormat.DEFAULT.withHeader());
map = parser.getHeaderMap();
appendHeader(map);

if (returnList == true) {csvList = parser.getRecords();}

else
  {
  for (CSVRecord record : parser)
    {
    parseCSVRecord(record);
    }
  }
parser.close();
}

public void parseCSVRecord (CSVRecord record) {
StringBuilder row = new StringBuilder();
  String[] cm = new String[map.size()];
  int i = 0;
  for (String c : map.keySet())
    {
    String cell = record.get(c);
    cm[i] = cell.replaceAll("\n", "");
    i+=1;
    }
  if(returnAll=true) {row = appendAllCols(cm);}
  for (int k=0; k < analysisColumn.length; k++)
  {String newCol = processColumn(record.get(analysisColumn[k]));
  row.append(newCol).append(separator); 
  }
  list.add(row.toString());
}

protected StringBuilder appendAllCols(String[] cm) {
StringBuilder sb = new StringBuilder();
for (String cm1 : cm)
  {
  sb.append(cm1).append(separator);
  }
return sb;
}

protected void appendHeader(Map<String,Integer> map) {
StringBuilder sb = new StringBuilder();
map.keySet().forEach((c) ->
  {
  sb.append(c).append(separator);
  });
sb.append(process).append(breaker).append(searchTerm);
list.add(sb.toString());
}

public Map<String,Integer> getMap() {
return map;
}

public ArrayList<String> getList() {
return  list;
}

public List<CSVRecord> getCsvRecords() {
return csvList;
}

}
