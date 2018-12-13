/*
 * The MIT License
 *
 * Copyright 2018 mw2.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package API_tools;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mw2
 */
public class GetDOIs {
private String separator;
private String breaker;
private ArrayList<String> list;
private boolean finished;
private ArrayList<String> titleList;
private ArrayList<String> nameList;
private ArrayList<String> typeList;
private boolean log;
private Toolkit.Utils utl;

public GetDOIs() {
separator = "@"; 
finished = false;
breaker = ";";
copyConfigFile(false);
buildMaps();
initialise();
}

private void initialise() {
list = new ArrayList<>();
StringBuilder sb = new StringBuilder();
titleList.forEach((title) -> {
//Add title and a comma
StringBuilder append = sb.append(title).append(separator);
    });
list.add(sb.toString());
utl = new Toolkit.Utils();
log = false;
}

private void copyConfigFile(boolean overwrite) {
//copy files from GitHub if needed
Toolkit.Utils ut = new Toolkit.Utils("./config/");
File f = new File("./config/datacite_config.csv");
if (!f.exists() || overwrite==true) {ut.copyFileFromGithub("datacite_default_config.csv", "datacite_config.csv");}
}

private void buildMaps() {
//Parse config file to find out how many rows are needed.
try
  {
  Path p = Paths.get("config/datacite_config.csv");
  Toolkit.ReadProcessCsv cs = new Toolkit.ReadProcessCsv(",", ";", p, true, true);
  List<CSVRecord> records = cs.readProcess();
  int numHeaders = records.size();
  //Create three arrays to store the configuration information
  titleList = new ArrayList<>();
  typeList = new ArrayList<>();
  nameList = new ArrayList<>();
  //Parse the config file and populate the arrays
  for (int i=0; i<records.size(); i++) 
    {
    CSVRecord row = records.get(i);
    titleList.add(row.get("title"));
    nameList.add(row.get("name"));
    typeList.add(row.get("type"));
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

public void getDOIs(String request, String fname) {
int i = 0;
while(true)
  {
  getDOIData(request,100, i+1);
  if (finished == true) {break;}
  i+=1;
  }
//System.out.println(list);
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(list, separator, true);
JOptionPane.showMessageDialog(null, "\"Output file has been saved to /output_files\"", "Output file", JOptionPane.INFORMATION_MESSAGE);
ut.writeFile(fname, sb);
}

public void getDOIData(String request, int num, int page) {
String urlsuffix = request+"&page[size]="+num+"&page[number]="+page;
HashMap<String,String> headers = new HashMap<>();
headers.put("user-agent", "MWhitton_DOI_Tools/0.2 (mailto:mw2@soton.ac.uk)");
API_tools.HttpClientClass api = new API_tools.HttpClientClass("https://api.datacite.org/");
StringBuilder result = api.getUrl(urlsuffix, headers);
//StringBuilder result = getHttpClient(url, headName, headVal);
//System.out.println(result);
try
  {
  JSONObject json = new JSONObject(result.toString());
  JSONArray subjson1 = json.getJSONArray("data");
  System.out.println(page +"\n");
  //System.out.println(subjson1); 
  if (subjson1.length() > 0)
    {
    for (int i = 0; i < subjson1.length(); i++)
      {
      JSONObject subjson2 = subjson1.getJSONObject(i);
      JSONObject subjson3 = subjson2.getJSONObject("attributes");
      //System.out.println(subjson3); 
      String line = api.extractJson(subjson3, nameList, typeList, separator);
      //System.out.println(line); 
      list.add(line);
      }
    }
  else {finished = true;}
  }
catch (Exception ex1) {utl.logError(ex1,"",log);}
}

public void writeLog() {
utl.writeLog();
}
}
