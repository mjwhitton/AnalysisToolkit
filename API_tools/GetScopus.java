package API_tools;


/**
 * Write a description of TestGetScopus here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVRecord;

public class GetScopus extends Toolkit.ReadProcessCsv {
private String apiKey;
private String doi;
private boolean log;
private Toolkit.Utils utl;
private ArrayList<String> searchNames;
private ArrayList<String> searchTypes;
private ArrayList<String> entryNames;
private ArrayList<String> entryTypes;

public GetScopus(String key) {
initialise();
apiKey = key;
separator = "@"; 
}

public GetScopus(String key, String sep) {
initialise();
apiKey = key;
separator = sep; 
}

private void initialise() {
breaker = ";";
list = new ArrayList<>();
returnAll = true;
int[] analysisCol = {0};
analysisColumn = analysisCol;
process="ScopusAPI";
searchTerm="DOI";
returnList = true;
utl = new Toolkit.Utils();
log = false;
searchNames = new ArrayList<>(Arrays.asList("opensearch:totalResults"));
searchTypes = new ArrayList<>(Arrays.asList("int"));
entryNames = new ArrayList<>(Arrays.asList("prism:doi"));
entryTypes = new ArrayList<>(Arrays.asList("string"));
}

public List<CSVRecord> getScopusDOIs(Path p) {
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
protected String processColumn(String entry) {
HashMap<String,String> headers = new HashMap<>();
headers.put("Accept", "application/json");
try{
doi = extractScopusDOI("2-s2.0-"+entry, headers);
//System.out.println(doi);
}
catch(Exception ex){doi="Error when writing the file "+ex;}
return doi;
}

public String extractScopusDOI(String eid, HashMap<String,String> headers) throws JSONException {
String urlsuffix = "content/search/scopus?query=EID("+eid.trim()+")&apiKey="+apiKey;
API_tools.HttpClientClass api = new API_tools.HttpClientClass("https://api.elsevier.com/");
StringBuilder result = api.getUrl(urlsuffix, headers);
JSONObject json = new JSONObject(result.toString());
JSONObject subjson1 = json.getJSONObject("search-results");
String totalResults = api.extractJson(subjson1, searchNames, searchTypes, "");
if(!totalResults.equals("1")) {return "Found "+totalResults+" Scopus records matching this EID";}
JSONArray subjson2 = subjson1.getJSONArray("entry");
JSONObject subjson3 = subjson2.getJSONObject(0);
doi = api.extractJson(subjson3, entryNames, entryTypes, "");
if(doi.equals("") || doi == null) {return "DOI not found";}
return doi;
}

public void writeLog() {
utl.writeLog();
}
}
