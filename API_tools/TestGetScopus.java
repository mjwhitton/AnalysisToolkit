package API_tools;


/**
 * Write a description of TestGetScopus here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVRecord;

public class TestGetScopus {
private String breaker;
private String separator;
private String apiKey;

public TestGetScopus() throws JSONException {
getScopusKey();
copyExampleFiles(false);
}

public TestGetScopus(boolean overwrite) throws JSONException {
getScopusKey();
copyExampleFiles(overwrite);
}

private void getScopusKey(){
Toolkit.Utils ut = new Toolkit.Utils("./config/");
String message = "Cannot load Scopus API key. Please enter this in the box below."; 
String heading = "Enter Scopus API Key";
ArrayList<String> key = ut.getConfigValue("scopus.txt", message, heading);
String keyval = key.get(0);
if(keyval==null) {keyval="N/A";}
apiKey =  keyval;
JOptionPane.showMessageDialog(null, "Scopus API key "+apiKey+" has been loaded", "Scopus API key", JOptionPane.INFORMATION_MESSAGE);
}

private void copyExampleFiles(boolean overwrite) {
//copy files from GitHub if needed
Toolkit.Utils ut = new Toolkit.Utils("./example_files/");
File f1 = new File("./example_files/Scopus_input.csv");
if (!f1.exists() || overwrite==true) {ut.copyFileFromGithub("Scopus_input.csv");}
}

public void testGetScopusDOIs() throws IOException, JSONException {
File f = new File("./example_files/Scopus_input.csv");
UI.Task task = new UI.Task("scopus", f, true, apiKey);
task.doInBackground();
JOptionPane.showMessageDialog(null, "\"Output file has been saved to /output_files\"", "Result of the Test", JOptionPane.INFORMATION_MESSAGE);
}
    }
