package API_tools;


/**
 * Write a description of GetAltmetriv here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.io.File;
import java.io.PrintStream;
import java.util.*;
import java.nio.file.*;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

public class GetAltmetric extends Toolkit.ReadProcessCsv {
private JSONObject json;
private HashMap<String,String> headers;
private ArrayList<String> titleList;
private ArrayList<String> nameList;
private ArrayList<String> typeList;
private ArrayList<String> rdrNames;
private ArrayList<String> rdrTypes;
private boolean log;
private Toolkit.Utils utl;

public GetAltmetric() {
copyConfigFile(false);
initialise();
separator = ","; 
}

public GetAltmetric(String sep, boolean isTesting) {
copyConfigFile(false);
initialise();
separator = sep; 
}

private void copyConfigFile(boolean overwrite) {
//copy files from GitHub if needed
Toolkit.Utils ut = new Toolkit.Utils("./config/");
File f = new File("./config/altmetric_config.csv");
if (!f.exists() || overwrite==true) {ut.copyFileFromGithub("altmetric_default_config.csv", "altmetric_config.csv");}
}

private void initialise() {
breaker = ";";
list = new ArrayList<>();
returnAll = true;
returnList = true;
int[] analysisCol = {0};
analysisColumn = analysisCol;
process="AltmetricAPI";
searchTerm="ExtractMetrics";
headers = new HashMap<>();
headers.put("user-agent", "MWhitton_AltMetric_Tools/1.1 (mailto:mw2@soton.ac.uk)");
//Import config files into Arrays
utl = new Toolkit.Utils();
log = false;
buildMaps();
}

private void buildMaps() {
//Parse config file to find out how many rows are needed.
try
  {
  Path p = Paths.get("config/altmetric_config.csv");
  Toolkit.ReadProcessCsv cs = new Toolkit.ReadProcessCsv(",", ";", p, true, true);
  List<CSVRecord> records = cs.readProcess();
  int numHeaders = records.size();
  //Create three arrays to store the configuration information
  titleList = new ArrayList<>();
  typeList = new ArrayList<>();
  nameList = new ArrayList<>();
  rdrTypes = new ArrayList<>();
  rdrNames = new ArrayList<>();
  //Parse the config file and populate the arrays
  for (int i=0; i<records.size(); i++) 
    {
    CSVRecord row = records.get(i);
    if(row.get("type").startsWith("readers."))
      {
    rdrTypes.add(row.get("type").substring(8));
    rdrNames.add(row.get("name"));    
      }
    else
      {
      nameList.add(row.get("name"));
      typeList.add(row.get("type"));
      }
    titleList.add(row.get("title"));
    }
  }
catch (Exception ex1)
  {utl.logError(ex1,"Error when loading the config file",log);
//If an error is present add a message to the arrays
titleList.add(0,"error");
typeList.add(0,"error");
nameList.add(0,"error");
  }
}

public List<CSVRecord> getAltmetrics(Path p) {
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
//Iterate over the title list
for (String title : titleList)
  {
  //Add title and a comma
  StringBuilder append = sb.append(title).append(separator);
  }
list.add(sb.toString());
}

@Override
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
String metrics = "error";
try {metrics = getMetrics(record.get(0), record.get(1));}
catch(Exception ex){utl.logError(ex,"",log);}
row.append(metrics);
list.add(row.toString());
}

public String getMetrics (String uri, String type) {
String metrics="";
//Make sure type is lowercase
type = type.toLowerCase();
//If no error
if (!nameList.get(0).equals("error")) {
// Extract metrics
metrics = useAltMetricApi(uri, type);}
//If an error add a message
else {metrics = "Error - required column headings in config file are missing";}
// Return Output StringBuilder
return metrics;   
}
  
private String appendDQ(String str) {
//Add double quotes to the input string
return "\"" + str + "\"";
}
  
public String useAltMetricApi(String uri, String type)
{
String metrics=""; 
//Create the uri to pass to the api
String urlsuffix = type + "/" + uri;
API_tools.HttpClientClass api = new API_tools.HttpClientClass("http://api.altmetric.com/v1/");
StringBuilder result = api.getUrl(urlsuffix, headers);
//System.out.println(result);
//System.out.println(doi);
if (result.charAt(0) == '{')
  {
  try
    {
    json = new JSONObject(result.toString());
    metrics = api.extractJson(json, nameList, typeList, separator);
    metrics = metrics + api.extractNestedJson(json, "readers", rdrNames, rdrTypes, separator);
    //metrics = extractJson(json);
    }
  catch(Exception ex){utl.logError(ex,"",log);}
  }
else {metrics = result.toString();}
 
return metrics;
}

public void writeLog() {
utl.writeLog();
}
}