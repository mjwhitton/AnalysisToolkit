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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.commons.csv.CSVRecord;

class Task extends SwingWorker<Void, Void> {
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
private String fileName; 
private String fileExtension;
private API_tools.UI_RunTools ui;

public Task(String taskType, Path p) {
ui = new API_tools.UI_RunTools();
type = taskType;
path = p;
separator = ui.getSeparator();
fileName = ui.getFileName(); 
fileExtension = ui.getFileExtension();
}
  
@Override
public Void doInBackground() {
startTime = System.currentTimeMillis();
if(type.equals("crossref")) {crossRefTask();}
else if(type.equals("scopus")) {scopusTask("to be added");}
else if(type.equals("altmetric")) {altmetricTask();}
return null;
}

private void crossRefTask() {
HashMap<String,String> headers = new HashMap<>();
headers.put("user-agent", "MWhitton_CrossRef_Tools/1.1 (mailto:mw2@soton.ac.uk)");
API_tools.GetCrossRef gcr = new API_tools.GetCrossRef(headers, separator);
ui.appendText("Starting to Analyse "+fileName);
List<CSVRecord> csvList =  gcr.getCrossRefDates(path);
numberOfRecords = csvList.size();
ui.appendText("Found "+numberOfRecords+" items to analyse.");
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  gcr.parseCSVRecord(row);
  currentProgress +=1;
  ui.appendText(row.get(0));
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }  
ArrayList<String> list = gcr.getList();
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = fileName+"."+fileExtension;
ut.writeFile(fname, sb);
}

private void scopusTask(String scopusApiKey) {
API_tools.GetScopus gs = new API_tools.GetScopus(scopusApiKey, separator);
ui.appendText("Starting to Analyse "+fileName);
Toolkit.Utils ut = new Toolkit.Utils();
List<CSVRecord> csvList = gs.getScopusDOIs(path);
numberOfRecords = csvList.size();
ui.appendText("Found "+numberOfRecords+" items to analyse.");
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  gs.parseCSVRecord(row);
  currentProgress +=1;
  ui.appendText(row.get(0));
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }  
ArrayList<String> list = gs.getList();
StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = fileName+"."+fileExtension;
ut.writeFile(fname, sb);
}

private void altmetricTask() {
API_tools.GetAltmetric ga = new API_tools.GetAltmetric(separator, true);
ui.appendText("Starting to Analyse "+fileName);
Toolkit.Utils ut = new Toolkit.Utils();
List<CSVRecord> csvList =  ga.getAltmetrics(path);
numberOfRecords = csvList.size();
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  ga.parseCSVRecord(row);
  currentProgress +=1;
  ui.appendText(row.get(0));
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }
ArrayList<String> list = ga.getList();

StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = fileName+"."+fileExtension;
ut.writeFile(fname, sb);
}
        /*
         * Executed in event dispatching thread
         */
 @Override
 public void done() {
 ui.resetButtons();
 endTime = System.currentTimeMillis();
 long timeTaken = (endTime-startTime)/1000;
 long minutes = timeTaken/60;
 long seconds = timeTaken%60;
 ui.appendText("Finished analysing. Analysis took "+minutes+" minutes and "+seconds+" seconds");
 setProgress(0);
 JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }