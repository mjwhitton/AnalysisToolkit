/*
 * The MIT License
 *
 * Copyright 2017 mw2.
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

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.commons.csv.CSVRecord;


class MyCustomFilter extends javax.swing.filechooser.FileFilter {
@Override
public boolean accept(File file) {
// Allow only directories, or files with ".txt" extension
return file.isDirectory() || file.getAbsolutePath().endsWith(".csv");
}

@Override
public String getDescription() {
// This description will be displayed in the dialog,
// hard-coded = ugly, should be done via I18N
return "CSV documents (*.csv)";
}
} 

/**
 *
 * @author mw2
 */
public class UI_RunTools extends javax.swing.JFrame implements PropertyChangeListener {

private File file;
private final String scopusApiKey;
private final File workingDirectory;
private Task task;

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

public Task(String taskType, Path p) {
type = taskType;
path = p;
}
  
@Override
public Void doInBackground() {
startTime = System.currentTimeMillis();
if(type.equals("crossref")) {crossRefTask();}
else if(type.equals("scopus")) {scopusTask();}
return null;
}

private String getSeparator() {
String s = separatorTextArea.getText();
if (s==null) {s="@";}
else if (s.equals("")) {s="@";}
return s;
}

private String getFileName() {
String f = file.getName();
int i = f.lastIndexOf(".");
if (i != -1) {f=f.substring(0, i);}
return f;
}

private void crossRefTask() {
String[] headName = {"User-Agent"};
String[] headVal = {"MWhitton_CrossRef_Tools/1.1 (mailto:mw2@soton.ac.uk)"};
String separator = getSeparator();
API_tools.GetCrossRef gcr = new API_tools.GetCrossRef(headName, headVal, separator);
appendTextarea("Starting to Analyse "+file.getName()+"\n");
List<CSVRecord> csvList =  gcr.getCrossRefDates(path);
numberOfRecords = csvList.size();
appendTextarea("Found "+numberOfRecords+" items to analyse."+"\n");
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  gcr.parseCSVRecord(row);
  currentProgress +=1;
  textarea.append(row.get(0)+"\n");
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(textarea, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }  
ArrayList<String> list = gcr.getList();
Toolkit.Utils ut = new Toolkit.Utils();
StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = getFileName()+".txt";
ut.writeFile(fname, sb);
}

private void scopusTask() {
String separator = getSeparator();
API_tools.GetScopus gs = new API_tools.GetScopus(scopusApiKey, separator);
appendTextarea("Starting to Analyse "+file.getName()+"\n");
Toolkit.Utils ut = new Toolkit.Utils();
List<CSVRecord> csvList = gs.getScopusDOIs(path);
numberOfRecords = csvList.size();
appendTextarea("Found "+numberOfRecords+" items to analyse."+"\n");
currentProgress = 0;
for (int i=0; i<csvList.size(); i++) 
  {
  CSVRecord row = csvList.get(i);
  gs.parseCSVRecord(row);
  currentProgress +=1;
  textarea.append(row.get(0)+"\n");
  setProgress(100*currentProgress/numberOfRecords);
  if(this.isCancelled() == true) {break;}
  else try {TimeUnit.SECONDS.sleep(1);}
  catch(Exception ex){JOptionPane.showMessageDialog(textarea, "Error when delaying" +ex, "Error", JOptionPane.ERROR_MESSAGE);}
  }  
ArrayList<String> list = gs.getList();
StringBuilder sb = ut.arrayListToString(list, "", true);
String fname = getFileName()+".txt";
ut.writeFile(fname, sb);
}

        /*
         * Executed in event dispatching thread
         */
 @Override
 public void done() {
 endTime = System.currentTimeMillis();
 java.awt.Toolkit.getDefaultToolkit().beep();
 crossRefButton.setEnabled(true);
 scopusButton.setEnabled(true);
 chooseFile.setEnabled(true);
 cancelButton.setEnabled(false);
 setCursor(null); //turn off the wait cursor
 long timeTaken = (endTime-startTime)/1000;
 long minutes = timeTaken/60;
 long seconds = timeTaken%60;
 textarea.append("Finished analysing. Analysis took "+minutes+" minutes and "+seconds+" seconds \n");
 setProgress(0);
 JOptionPane.showMessageDialog(textarea, "Output file has been saved to /output_files", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
  
  /**
   * Creates new form UI_RunTools
   */
  public UI_RunTools() {
    initComponents();
    scopusApiKey = getScopusApiKey();
    workingDirectory = new File(System.getProperty("user.dir"));
    fileTextarea.setText("None");
    cancelButton.setEnabled(false);
  }
  
private String getScopusApiKey() {
String key = "N/A";
File f = new File("./apikey/scopus.txt");
if (!f.exists())
  {
  try {Files.createDirectories(Paths.get("./apikey"));}
  catch(Exception ex1){JOptionPane.showMessageDialog(null, "Error when creating the apikey folder" +ex1, "Error", JOptionPane.ERROR_MESSAGE);}
  String s = JOptionPane.showInputDialog(null, "Cannot load Scopus API key. Please enter this in the box below. Enter 'N/A' if you do not want to use this feature.", "Enter Scopus API Key", JOptionPane.QUESTION_MESSAGE);
  //System.out.println(s);
  if (s==null) {s = "N/A";}
  else if (s.equals("")) {s = "N/A";}
  Toolkit.Utils ut = new Toolkit.Utils("./apikey/");
  ut.writeFile("scopus.txt", s);
  key = s;
  }  
else
  {Path p = f.toPath();
  Toolkit.ReadFileToArrayList rfsb = new Toolkit.ReadFileToArrayList(10000,p);
  ArrayList<String> list = rfsb.readProcess();   
  key = list.get(0);
    }
textarea.append("Scopus API key "+key+" has been loaded"+"\n");
return key;
}

private void appendTextarea(String text) {
textarea.append(text+"\n");
}

@Override
public void propertyChange(PropertyChangeEvent evt) {
if ("progress".equals(evt.getPropertyName()))
  {
  int progress = (Integer) evt.getNewValue();
  progressBar.setValue(progress);
  //textarea.append(String.format(
  //"Completed %d%% of task.\n", task.getProgress()));
  } 
 }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jFileChooser1 = new javax.swing.JFileChooser();
    jScrollPane1 = new javax.swing.JScrollPane();
    fileTextarea = new javax.swing.JTextArea();
    chooseFile = new javax.swing.JButton();
    fileLabel = new javax.swing.JLabel();
    crossRefButton = new javax.swing.JButton();
    scopusButton = new javax.swing.JButton();
    titleLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    saveLog = new javax.swing.JButton();
    clearLog = new javax.swing.JButton();
    textareaScrollPane = new java.awt.ScrollPane();
    textareaScrollPane1 = new javax.swing.JScrollPane();
    textarea = new javax.swing.JTextArea();
    cancelButton = new javax.swing.JButton();
    separatorLabel = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    separatorTextArea = new javax.swing.JTextArea();

    jFileChooser1.setAcceptAllFileFilterUsed(false);
    jFileChooser1.setCurrentDirectory(workingDirectory);
    jFileChooser1.setDialogTitle("Select a file ...");
    jFileChooser1.setFileFilter(new MyCustomFilter());

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    fileTextarea.setEditable(false);
    fileTextarea.setColumns(20);
    fileTextarea.setLineWrap(true);
    fileTextarea.setRows(2);
    fileTextarea.setPreferredSize(new java.awt.Dimension(20, 40));
    jScrollPane1.setViewportView(fileTextarea);

    chooseFile.setText("Choose File");
    chooseFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        chooseFileActionPerformed(evt);
      }
    });

    fileLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    fileLabel.setText("File Currently Selected");

    crossRefButton.setText("Analyse in CrossRef");
    crossRefButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        crossRefButtonActionPerformed(evt);
      }
    });

    scopusButton.setText("Analyse in Scopus");
    scopusButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        scopusButtonActionPerformed(evt);
      }
    });

    titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    titleLabel.setText("API Tools for Use of the CrossRef and Scopus APIs ");

    progressBar.setStringPainted(true);

    saveLog.setText("Save log to text");
    saveLog.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveLogActionPerformed(evt);
      }
    });

    clearLog.setText("Clear log");
    clearLog.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearLogActionPerformed(evt);
      }
    });

    textareaScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    textareaScrollPane1.setAutoscrolls(true);

    textarea.setEditable(false);
    textarea.setColumns(20);
    textarea.setRows(2);
    textarea.setPreferredSize(null);
    textareaScrollPane1.setViewportView(textarea);

    textareaScrollPane.add(textareaScrollPane1);

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    separatorLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    separatorLabel.setText("Separator");

    separatorTextArea.setColumns(20);
    separatorTextArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    separatorTextArea.setLineWrap(true);
    separatorTextArea.setRows(2);
    separatorTextArea.setText("@");
    separatorTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    separatorTextArea.setMinimumSize(new java.awt.Dimension(20, 25));
    separatorTextArea.setPreferredSize(null);
    jScrollPane2.setViewportView(separatorTextArea);
    separatorTextArea.getAccessibleContext().setAccessibleParent(jScrollPane1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(34, 34, 34)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(textareaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(titleLabel)
          .addGroup(layout.createSequentialGroup()
            .addComponent(chooseFile)
            .addGap(19, 19, 19)
            .addComponent(crossRefButton)
            .addGap(36, 36, 36)
            .addComponent(scopusButton)
            .addGap(44, 44, 44)
            .addComponent(cancelButton))
          .addGroup(layout.createSequentialGroup()
            .addComponent(saveLog)
            .addGap(35, 35, 35)
            .addComponent(clearLog))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(fileLabel)
              .addComponent(separatorLabel))
            .addGap(28, 28, 28)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap(38, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(titleLabel)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(separatorLabel)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(fileLabel)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(26, 26, 26)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(chooseFile)
          .addComponent(crossRefButton)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(scopusButton)
            .addComponent(cancelButton)))
        .addGap(15, 15, 15)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(19, 19, 19)
        .addComponent(textareaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(saveLog)
          .addComponent(clearLog))
        .addGap(22, 22, 22))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void chooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFileActionPerformed
int returnVal = jFileChooser1.showOpenDialog(this);
if (returnVal == JFileChooser.APPROVE_OPTION)
  {
  file = jFileChooser1.getSelectedFile();
  fileTextarea.setText(file.getAbsolutePath());
  }
else {fileTextarea.setText("File access cancelled by user.");}
  }//GEN-LAST:event_chooseFileActionPerformed
   
  private void crossRefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crossRefButtonActionPerformed
if (file != null)
  {Path path = file.toPath();
  crossRefButton.setEnabled(false);
  scopusButton.setEnabled(false);
  chooseFile.setEnabled(false);
  cancelButton.setEnabled(true);
  setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
  task = new Task("crossref", path);
  task.addPropertyChangeListener(this);
  task.execute();
  }
else {JOptionPane.showMessageDialog(textarea, "No file has been selected", "Error", JOptionPane.ERROR_MESSAGE);}
  }//GEN-LAST:event_crossRefButtonActionPerformed

  private void scopusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scopusButtonActionPerformed
if (scopusApiKey.equals("N/A")) {JOptionPane.showMessageDialog(textarea, "No Scopus API Key is loaded", "Error", JOptionPane.ERROR_MESSAGE);}
else if (file != null)
  {Path path = file.toPath();
  crossRefButton.setEnabled(false);
  scopusButton.setEnabled(false);
  chooseFile.setEnabled(false);
  cancelButton.setEnabled(true);
  setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
  task = new Task("scopus", path);
  task.addPropertyChangeListener(this);
  task.execute();
  }
else {JOptionPane.showMessageDialog(textarea, "No file has been selected", "Error", JOptionPane.ERROR_MESSAGE);}
  }//GEN-LAST:event_scopusButtonActionPerformed

  private void saveLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLogActionPerformed
String log = textarea.getText();
Toolkit.Utils ut = new Toolkit.Utils();
ut.writeFile("log.txt", log);
JOptionPane.showMessageDialog(textarea, "Log has been saved to /output_files", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
  }//GEN-LAST:event_saveLogActionPerformed

  private void clearLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearLogActionPerformed
textarea.setText("");
JOptionPane.showMessageDialog(textarea, "Log has been cleared.", "Log refreshed", JOptionPane.INFORMATION_MESSAGE);
  }//GEN-LAST:event_clearLogActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
task.cancel(true);
crossRefButton.setEnabled(true);
scopusButton.setEnabled(true);
cancelButton.setEnabled(false);
textarea.append("Analysis cancelled"+"\n");
  }//GEN-LAST:event_cancelButtonActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(UI_RunTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(UI_RunTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(UI_RunTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(UI_RunTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new UI_RunTools().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JButton chooseFile;
  private javax.swing.JButton clearLog;
  private javax.swing.JButton crossRefButton;
  private javax.swing.JLabel fileLabel;
  private javax.swing.JTextArea fileTextarea;
  private javax.swing.JFileChooser jFileChooser1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JButton saveLog;
  private javax.swing.JButton scopusButton;
  private javax.swing.JLabel separatorLabel;
  private javax.swing.JTextArea separatorTextArea;
  private javax.swing.JTextArea textarea;
  private java.awt.ScrollPane textareaScrollPane;
  private javax.swing.JScrollPane textareaScrollPane1;
  private javax.swing.JLabel titleLabel;
  // End of variables declaration//GEN-END:variables
}
