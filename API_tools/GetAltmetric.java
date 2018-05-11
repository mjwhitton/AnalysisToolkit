package API_tools;


/**
 * Write a description of GetAltmetriv here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import Toolkit.ReadWebpageSaveFile;
import java.io.PrintStream;
import java.util.*;
import java.nio.file.*;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAltmetric extends Toolkit.ReadProcessCsv {
private JSONObject json;
private HashMap<String,String> headers;
private PrintStream errorLog;
private String[] titleList;
private String[] nameList;
private String[] typeList;


public GetAltmetric() {
initialise();
separator = ","; 
// Set Error and System logs to go to Errors.txt
try{errorLog = new PrintStream("Errors.txt");}
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when creating errors.txt" +ex, "Error", JOptionPane.ERROR_MESSAGE);} 
System.setOut(errorLog);
System.setErr(errorLog);  
}

public GetAltmetric(String sep, boolean isTesting) {
initialise();
separator = sep; 
if (isTesting==false)
  {
  try{errorLog = new PrintStream("Errors.txt");}
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when creating errors.txt" +ex, "Error", JOptionPane.ERROR_MESSAGE);} 
  System.setOut(errorLog);
  System.setErr(errorLog);
  }
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
buildMaps();
}

private void copyFile(String file, String folder) {
copyFile(file, file, folder);
}

private void copyFile(String file, String fileName, String folder) {
try
  {
  Files.createDirectories(Paths.get(folder));
  String rep = "https://raw.githubusercontent.com/mjwhitton/AnalysisToolkit/master/example_files/";
  ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048, folder+"/");
  rwsf.setFile(rep+file, fileName);
  rwsf.readProcess(0);
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when saving examples and config files" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
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
  titleList = new String[numHeaders];
  typeList = new String[numHeaders];
  nameList = new String[numHeaders];
  //Parse the config file and populate the arrays
  for (int i=0; i<records.size(); i++) 
    {
    CSVRecord row = records.get(i);
    titleList[i] = row.get("title");
    nameList[i] = row.get("name");
    typeList[i] = row.get("type");
    }
  }
catch (Exception ex1)
  {
  JOptionPane.showMessageDialog(null, "Error when loading the config file" +ex1, "Error", JOptionPane.ERROR_MESSAGE);
//If an error is present add a message to the arrays
titleList = new String[]{"error"};
typeList = new String[]{"error"};
nameList = new String[]{"error"};
  }
}

public List<CSVRecord> getAltmetrics(Path p) {
try
  {
  path = p;
  readCsv();
//System.out.println(map);
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
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
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
row.append(metrics);
list.add(row.toString());
}

public String getMetrics (String uri, String type) {
String metrics;
//Make sure type is lowercase
type = type.toLowerCase();
//If no error
if (!nameList.equals("error")) {
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
  
private String extractNestedJson(JSONObject json, String name) throws JSONException
{
StringBuilder sb = new StringBuilder();
//Extract the sub-json object
JSONObject subJson = json.getJSONObject(name);
//iterate over the nameList array
for (int i = 0; i < nameList.length; i++)
  {//Get the corrosponding type and name
  String curtype = typeList[i];
  String CurName = nameList[i];
  //Append int value and a comma, only if it is part of the sub-object. These values should have a curtype of [subobject name].type
  if (subJson.has(CurName) && curtype.equals(name+".int")) {sb.append(subJson.getInt(CurName)).append(separator);}
  //Append n/a and a comma for any missing values that should be in the sub-object
  else if (curtype.startsWith(name)) {sb.append("n/a,");}    
  }
return sb.toString();
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
    metrics = extractJson(json);
    }
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }
else {metrics = result.toString();}
 
return metrics;
}

private String extractJson(JSONObject json) throws JSONException {
//Create StringBuilder 
StringBuilder sb = new StringBuilder();
//Iterate over the nameList array of all the metrics to look for
for (int i=0; i < nameList.length; i++)
  {//Get the corrosponding type and name
  String curtype = typeList[i];
  String curName = nameList[i];
  //Get string values and append them with double quotes, and add a comma
  if (json.has(curName) && curtype.equals("string")) {sb.append(appendDQ(json.getString(curName))).append(separator);}
  // If url was not found (error json recieved) append Error and a comma
  else if (json.has("Error")) {sb.append("Error"+",");}
  //Get double values and append them and a comma
  else if (json.has(curName) && curtype.equals("double")) {sb.append(json.getDouble(curName)).append(separator);}
  //Get integer values and append them and a comma
  else if (json.has(curName) && curtype.equals("int")) {sb.append(json.getInt(curName)).append(separator);}
  //Append n/a and a comma if that metric was not found. Unless it's part of the readers sub-object
  else if (!curtype.startsWith("readers")) {sb.append("n/a,");}
  }
//Extract readers sub-object
if(json.has("readers")){sb.append(extractNestedJson(json, "readers"));}
//Add an end of row
return sb.toString();
  }

}