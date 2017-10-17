package API_tools;


/**
 * Write a description of GetCrossRef here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class GetCrossRef extends Toolkit.ReadProcessCsv {
private JSONObject json;
String[] headName;
String[] headVal;
String dates;


public GetCrossRef(String[] headNm, String[] headVl) {
breaker = ";";
separator = "@"; 
list = new ArrayList<>();
returnAll = true;
int[] analysisCol = {0};
analysisColumn = analysisCol;
process="CrossRefAPI";
searchTerm="Dates";
headName = headNm;
headVal = headVl;
}

public void getCrossRefDates(Path p) {
try
  {
  path = p;
  readCsv();
//System.out.println(map);
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
//System.out.println(list);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(list, "", true);
ut.writeFile("CrossRefData.txt", sb);
}

@Override
protected void appendHeader(Map<String,Integer> map) {
StringBuilder sb = new StringBuilder();
map.keySet().forEach((c) ->
  {
  sb.append(c).append(separator);
  });
sb.append("Accepted Date").append(separator).append("ePub Date").append(separator).append("Pub Date");
list.add(sb.toString());
}

@Override
protected String processColumn(String entry) {
try{
dates = useCrossRefAPI(entry);
//System.out.println(doi);
}
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when writing the file" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
//System.out.println(dates);
//System.out.println(list);
return dates;
}

private String cleanDate(String date) {
StringBuilder sb = new StringBuilder();
for(int i = 0; i < date.length();i++)
  {
  {char x = date.charAt(i);
  if(x != '[' && x != ']' && x != ',') {sb.append(x);}
  else if (x == ',') {sb.append("-");}
  }
}
return sb.toString();
}

protected String getDates (String key, JSONObject json) throws JSONException {
StringBuilder sb = new StringBuilder();
if (json.has(key))
  {
  JSONObject subJson1 = json.getJSONObject(key); 
  JSONArray subJson2 = subJson1.getJSONArray("date-parts");
  //System.out.println(subJson2.length());
  if(subJson2.length()!=1)
  {sb.append(subJson2.toString()).append(separator);}
  else {
    sb.append(cleanDate(subJson2.getString(0))).append(separator);
  }
  }
else {sb.append("N/A").append(separator);}
return sb.toString();
}

public String useCrossRefAPI(String doi){
StringBuilder row = new StringBuilder();
String url = "https://api.crossref.org/works/" + doi;
API_tools.HttpGet api = new API_tools.HttpGet();
StringBuilder result = api.getHttpClient(url, headName, headVal);
//System.out.println(result);
System.out.println(doi);
System.out.println(result);
if (result.charAt(0) == '{') {row.append(extractJson(result));}
else {row.append(result.toString());}
//System.out.println(row);
return row.toString();
}

private String extractJson(StringBuilder result) {
StringBuilder sb = new StringBuilder();
try
  {
  json = new JSONObject(result.toString());
  if (json.has("message"))
    {
    JSONObject subJson = json.getJSONObject("message");
    sb.append(getDates("accepted", subJson));
    sb.append(getDates("published-online", subJson));
    sb.append(getDates("published-print", subJson));    
    }
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
return sb.toString();
}

}