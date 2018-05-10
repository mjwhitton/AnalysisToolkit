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
package UI;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.commons.csv.CSVRecord;

public class Task extends SwingWorker<Void, Void> {
/*
* Main task. Executed in background thread.
*/
private final String type;
private final Path path;
private int currentProgress;
private int numberOfRecords;
private long startTime;
private long endTime;
private String separator;
private final String fileName; 
private String fileExtension;
private String scopusApiKey;
private final boolean testMode;
private API_tools.GetCrossRef gcr;
private API_tools.GetScopus gs;
private API_tools.GetAltmetric ga;

public Task(String taskType, File f) {
type = taskType.toLowerCase();
path = f.toPath();
fileName = getFileName(f); 
getData();
testMode = false;
}

public Task(String taskType, File f, boolean testing, String scopusAPI) {
type = taskType.toLowerCase();
path = f.toPath();
fileName = getFileName(f);
testMode = testing;
scopusApiKey = scopusAPI;
if(testMode == false) {getData();}
else {separator = ","; fileExtension="csv";}
}

private void getData() {
separator = RunTools.getSeparator();
fileExtension = RunTools.getFileExtension();
scopusApiKey = RunTools.getScopusApiKey();
}

private String getFileName(File file) {
String f = file.getName();
int i = f.lastIndexOf(".");
if (i != -1) {f=f.substring(0, i);}
return f;
}
  
@Override
public Void doInBackground() {
startTime = System.currentTimeMillis();
printMessage("Starting to Analyse "+fileName);
switch(type)
  {
  case "crossref":
    crossRefTask();
    break;
  case "scopus":
    scopusTask();
    break;
  case "altmetric":
    altmetricTask();
    break;
  default:
    break;
  }
return null;
}

private void printMessage(String message) {
if(testMode==false) {RunTools.appendTextarea(message);}
else {System.out.println(message);}
}

private void crossRefTask() {
HashMap<String,String> headers = new HashMap<>();
headers.put("user-agent", "MWhitton_CrossRef_Tools/1.1 (mailto:mw2@soton.ac.uk)");
gcr = new API_tools.GetCrossRef(headers, separator);
List<CSVRecord> csvList =  gcr.getCrossRefDates(path);
parseList(csvList);
ArrayList<String> list = gcr.getList();
outputFile(list);
}

private void scopusTask() {
gs = new API_tools.GetScopus(scopusApiKey, separator);
List<CSVRecord> csvList = gs.getScopusDOIs(path);
parseList(csvList);
ArrayList<String> list = gs.getList();
outputFile(list);
}

private void altmetricTask() {
ga = new API_tools.GetAltmetric(separator, true);
List<CSVRecord> csvList =  ga.getAltmetrics(path);
parseList(csvList);
ArrayList<String> list = ga.getList();
outputFile(list);
}

private void parseList(List<CSVRecord> csvList) {
numberOfRecords = csvList.size();
RunTools.appendTextarea("Found "+numberOfRecords+" items to analyse.");
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  process(row);
  currentProgress +=1;
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }  
}

private void process(CSVRecord row){
printMessage(row.get(0));
switch(type)
  {
  case "crossref":
    gcr.parseCSVRecord(row);
    break;
  case "scopus":
    gs.parseCSVRecord(row);
    break;
  case "altmetric":
    ga.parseCSVRecord(row);
    break;
  default:
    break;
  }
}

private void outputFile(ArrayList<String> list) {
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = fileName+"."+fileExtension;
ut.writeFile(fname, sb);
}
        /*
         * Executed in event dispatching thread
         */
 @Override
 public void done() {
 RunTools.resetButtons();
 endTime = System.currentTimeMillis();
 long timeTaken = (endTime-startTime)/1000;
 long minutes = timeTaken/60;
 long seconds = timeTaken%60;
 RunTools.appendTextarea("Finished analysing. Analysis took "+minutes+" minutes and "+seconds+" seconds");
 setProgress(0);
 JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }