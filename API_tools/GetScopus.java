package API_tools;


/**
 * Write a description of TestGetScopus here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVRecord;

public class GetScopus extends Toolkit.ReadProcessCsv {
private String apiKey;
private String doi;

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
}

public List<CSVRecord> getScopusDOIs(Path p) {
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
protected String processColumn(String entry) {
String[] headName = {"Accept"};
String[] headVal = {"application/json"}; 
try{
doi = extractScopusDOI("2-s2.0-"+entry, headName, headVal);
//System.out.println(doi);
}
catch(Exception ex){doi="Error when writing the file "+ex;}
return doi;
}

public String extractScopusDOI(String eid, String[] headName, String[] headVal) throws JSONException {
String url = "https://api.elsevier.com/content/search/scopus?query=EID("+eid.trim()+")&apiKey="+apiKey;
API_tools.HttpGet api = new API_tools.HttpGet();
StringBuilder result = api.getHttpClient(url, headName, headVal);
//StringBuilder result = getHttpClient(url, headName, headVal);
//System.out.println(result);
JSONObject json = new JSONObject(result.toString());
JSONObject subjson1 = json.getJSONObject("search-results");
String totalResults = "";
if(subjson1.has("opensearch:totalResults")) {totalResults = subjson1.getString("opensearch:totalResults");}
//System.out.println(totalResults);
JSONArray subjson2 = subjson1.getJSONArray("entry");
JSONObject subjson3 = subjson2.getJSONObject(0);
doi = "";
if(subjson3.has("prism:doi")) {doi = subjson3.getString("prism:doi");}
if(doi.equals("") && !totalResults.equals("1")) {doi = "Scopus ID not found";}
if(doi.equals("") && totalResults.equals("1")) {doi = "DOI not found";}
return doi;
}
    }
