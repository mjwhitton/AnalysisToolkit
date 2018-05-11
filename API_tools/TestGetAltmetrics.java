package API_tools;


/**
 * Write a description of TestGetAltmetrics here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.File;
import javax.swing.JOptionPane;

public class TestGetAltmetrics {

public TestGetAltmetrics() {
copyExampleFiles(false);
}

public TestGetAltmetrics(boolean overwrite) {
copyExampleFiles(overwrite);
}
  
private void copyExampleFiles(boolean overwrite) {
//copy files from GitHub if needed
Toolkit.Utils ut = new Toolkit.Utils("./example_files/");
File f1 = new File("./example_files/Altmetric_input.csv");
if (!f1.exists() || overwrite==true) {ut.copyFileFromGithub("Altmetric_input.csv");}
}
  
  
public void testGetMetricsWithBadURL(){
String uri = "19.99999";
String type = "doi";  
API_tools.GetAltmetric ga = new API_tools.GetAltmetric();
String metrics = ga.getMetrics(uri, type);
JOptionPane.showMessageDialog(null, metrics, "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}

public void testGetMetrics () {
String uri = "10.1371/journal.pone.0081648";
String type = "doi";
API_tools.GetAltmetric ga = new API_tools.GetAltmetric(",", true);
String metrics = ga.getMetrics(uri, type);
JOptionPane.showMessageDialog(null, metrics, "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}

public void testGetAltmetrics() {
File f = new File("./example_files/Altmetric_input.csv");
UI.Task task = new UI.Task("altmetric", f, true, "N/A");
task.doInBackground();
JOptionPane.showMessageDialog(null, "\"Output file has been saved to /output_files\"", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
} 
  
}
