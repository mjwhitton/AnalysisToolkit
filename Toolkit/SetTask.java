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
package Toolkit;

import API_tools.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.commons.csv.CSVRecord;

public abstract class SetTask extends SwingWorker<Void, Void> {
/*
* Main task. Executed in background thread.
*/
protected final String type;
protected final Path path;
protected int currentProgress;
protected int numberOfRecords;
protected long startTime;
protected long endTime;
protected String separator;
protected String fileName; 
protected String fileExtension;
protected API_tools.UI_RunTools ui;

public SetTask(String taskType, Path p) {
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
chooseType();
return null;
}

protected abstract void chooseType();

protected abstract void process(CSVRecord row);

protected void parseList(List<CSVRecord> csvList) {
numberOfRecords = csvList.size();
ui.appendTextarea("Found "+numberOfRecords+" items to analyse.");
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

protected void outputFile(ArrayList<String> list) {
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
 ui.resetButtons();
 endTime = System.currentTimeMillis();
 long timeTaken = (endTime-startTime)/1000;
 long minutes = timeTaken/60;
 long seconds = timeTaken%60;
 ui.appendTextarea("Finished analysing. Analysis took "+minutes+" minutes and "+seconds+" seconds");
 setProgress(0);
 JOptionPane.showMessageDialog(null, "Output file has been saved to /output_files", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }