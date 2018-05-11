package API_tools;


/**
 * Write a description of GetCrossRef here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.json.JSONException;

public class TestGetCrossRef {

public TestGetCrossRef() throws JSONException {
copyExampleFiles(false);
}

public TestGetCrossRef(boolean overwrite) throws JSONException {
copyExampleFiles(overwrite);
}

private void copyExampleFiles(boolean overwrite) {
//copy files from GitHub if needed
Toolkit.Utils ut = new Toolkit.Utils("./example_files/");
File f1 = new File("./example_files/Crossref_input.csv");
if (!f1.exists()) {ut.copyFileFromGithub("Crossref_input.csv");}
}

public void testUseCrossRefAPI() throws IOException, JSONException {
StringBuilder cx = new StringBuilder();
String doi = "10.1037/0003-066X.59.1.29";
cx.append("DOI,Accepted Date,ePub Date, Pub Date"+"\n");
HashMap<String,String> headers = new HashMap<>();
headers.put("user-agent", "MWhitton_CrossRef_Tools/1.1 (mailto:mw2@soton.ac.uk)");
API_tools.GetCrossRef gcr = new API_tools.GetCrossRef(headers);
String dates = gcr.useCrossRefAPI(doi);
JOptionPane.showMessageDialog(null, dates, "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}

public void testGetCrossRef() {
File f = new File("./example_files/Crossref_input.csv");
UI.Task task = new UI.Task("crossref", f, true, "N/A");
task.doInBackground();
JOptionPane.showMessageDialog(null, "\"Output file has been saved to /output_files\"", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}
}
