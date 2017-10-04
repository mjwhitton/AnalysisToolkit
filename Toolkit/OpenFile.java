package Toolkit;
/**
 * Write a description of OpenFile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFile extends JPanel {
private JFileChooser fc;
private JFrame frame;
boolean testMode;
private String filterDesc;
private String filterExt;

public OpenFile(String filterDescription, String filterExtentions) throws FileNotFoundException, IOException{
testMode = false;
filterDesc = filterDescription;
filterExt = filterExtentions;
createFrame();
            }

public OpenFile(String filterDescription, String filterExtentions, boolean isTesting) throws FileNotFoundException, IOException {
filterDesc = filterDescription;
filterExt = filterExtentions;
if (isTesting == true) {testMode = true;} else {testMode = false;}
createFrame();
            }
            
private void createFrame() {
//Create a file chooser
fc = new JFileChooser();
//Add the CSV File Filter
FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, filterExt);
fc.addChoosableFileFilter(filter);
fc.setAcceptAllFileFilterUsed(false);
//Create a JPanel
JPanel buttonPanel = new JPanel(); //use FlowLayout   
}
            
public File getFile() throws FileNotFoundException, IOException {
int returnVal = fc.showOpenDialog(OpenFile.this);
File file = null;
if (returnVal == JFileChooser.APPROVE_OPTION) {
  file = fc.getSelectedFile();
  System.out.println("Opening: " + file.getName() + "." + "\n");}
else {System.out.println("Open command cancelled by user." + "\n");}
return file;  
    }
    
}
