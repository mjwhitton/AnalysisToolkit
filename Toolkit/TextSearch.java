package Toolkit;
/**
 * Write a description of TextSearch here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TextSearch {
private StringBuilder sb;
private ArrayList<String> idList; 
private ArrayList<String> idList2; 
private ArrayList<String> searchResult; 
private StringBuilder sbTx;
private boolean atEnd;
private boolean inclStartStr;
private int offset;

public TextSearch(boolean includeStartString, int offsetnum) throws FileNotFoundException, IOException {
initialise();
inclStartStr = includeStartString;
offset = offsetnum;
}

public TextSearch() throws FileNotFoundException, IOException {
initialise();
inclStartStr = true;
offset = -1;
}

private void initialise(){
sb = new StringBuilder();
sbTx = new StringBuilder();
idList = new ArrayList<String>();
idList2 = new ArrayList<String>();
atEnd = false;    
searchResult = new ArrayList<String>();
}   
 
public void loadStringBuilder(StringBuilder s) {
sbTx = s; 
}

public int searchOne(String[] startEnd, int start) {
int next = -1;
int two = 0;
int max = sbTx.length();
String startST = "";
String end = "";
if(offset!=-1 || startEnd.length ==1) {startST=startEnd[0];}
else {startST=startEnd[0]; end = startEnd[1];}
int one = sbTx.indexOf(startST,start);
if (one!=-1 && offset==-1) {two = sbTx.indexOf(end, one+1);}
if (one!=-1 && offset!=-1) {two = one+startST.length()+offset;}
String result = "";
if (inclStartStr == false && one!=-1) {one+= startST.length();}
if (one!=-1 && two < sbTx.length()) {result = sbTx.substring(one, two); searchResult.add(result); next = one+result.length();}
return next;
}

public ArrayList<String> searchMany(String[] startEnd) {
int start = 0;
while (true){
  int result = searchOne(startEnd, start);
  if (result!=-1) {start= result+1;}
  else {break;}
  }
return searchResult;
}

public StringBuilder getSB() {
return sb;     
}

}
