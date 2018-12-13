package API_tools;


/**
 * Write a description of GetCrossRef here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;
import java.nio.file.*;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

public class GetCrossRef extends Toolkit.ReadProcessCsv {
private JSONObject json;
private JSONObject subJson;
private final HashMap<String,String> headers;
private ArrayList<String> dateNames;
private ArrayList<String> dateTypes;
private ArrayList<String> nameList;
private boolean log;
private Toolkit.Utils utl;

public GetCrossRef(HashMap<String,String> hders) {
initialise();
separator = "@"; 
headers = hders;
}

public GetCrossRef(HashMap<String,String> hders, String sep) {
initialise();
separator = sep; 
headers = hders;
}

private void initialise() {
breaker = ";";
list = new ArrayList<>();
returnAll = true;
returnList = true;
int[] analysisCol = {0};
analysisColumn = analysisCol;
process="CrossRefAPI";
searchTerm="Dates";
utl = new Toolkit.Utils();
log = false;
dateNames = new ArrayList<>(Arrays.asList("date-parts"));
dateTypes = new ArrayList<>(Arrays.asList("array"));
nameList = new ArrayList<>(Arrays.asList("accepted","published-online","published-print"));
}

public List<CSVRecord> getCrossRefDates(Path p) {
try
  {
  path = p;
  readCsv();
//System.out.println(map);
  }
catch(Exception ex){utl.logError(ex,"",log);}
return csvList;
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
String dates = "";
try{
dates = useCrossRefAPI(entry);
//System.out.println(doi);
}
catch(Exception ex){dates="Error when writing the file"+ex;}
//System.out.println(dates);
//System.out.println(list);
return dates;
}

public String useCrossRefAPI(String doi){
StringBuilder row = new StringBuilder();
String urlsuffix = "works/" + doi.trim();
API_tools.HttpClientClass api = new API_tools.HttpClientClass("https://api.crossref.org/");
StringBuilder result = api.getUrl(urlsuffix, headers);
//System.out.println(result);
//System.out.println(result);
if (result.charAt(0) == '{')
  {
  try
    {
    json = new JSONObject(result.toString());
    if (json.has("message")) {subJson = json.getJSONObject("message");}
    for (int i=0;i < nameList.size();i++)
      {
      String date = api.extractNestedJson(subJson, nameList.get(i), dateNames, dateTypes, separator);
      //System.out.println(date);
      date = date.replaceAll("(\\[)(\\d+)(\\,)(\\d+)(\\,)(\\d+)(\\])", "$6/$4/$2");
      date = date.replaceAll("(\\[)(\\d+)(\\,)(\\d+)(\\])", "$4/$2");
      row.append(date);
      }
    }
  catch(Exception ex){return "Error: "+ex;}
  }
else {row.append(result.toString());}
//System.out.println(row);
return row.toString();
}

public void writeLog() {
utl.writeLog();
}

}