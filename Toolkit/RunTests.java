/*
 * This file is used to run the test files for the Toolkit package.
 * It also created the example_files folder and copies them from GitHub.
 *
 * @author (Michael Whitton) 
 * @version (9/10/17)
 */
package Toolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mw2
 */
public class RunTests extends javax.swing.JFrame {

    /**
     * Creates new form RunTests
     */
    public RunTests() {
        initComponents();
        copyExampleFiles();
        copyGeneralFiles();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testSelect = new javax.swing.JComboBox<>();
        runTest = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("frame"); // NOI18N

        testSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TestReadCsv - testExtractFileSelect", "TestReadCsv - testExtractText", "TestReadFileWebpage - testSaveImage", "TestReadFileWebpage - testReadAndSave_Images", "TestReadFileWebpage - testReadFileToArrayList", "TestReadFileWebpage - testGetWebpage", "TestReadFileWebpage - testReadFileToStringBuilder", "TestTextSearch - testSearchManyStartEndKeywords", "TestTextSearch - testSearchManyStarkKeywordPlusOffset", "[Combo Box Error message]" }));
        testSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testSelectActionPerformed(evt);
            }
        });

        runTest.setText("Run Test");
        runTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTestActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Run Toolkit Package Tests");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(runTest)
                    .addComponent(testSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(testSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(runTest)
                .addContainerGap(156, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void testSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testSelectActionPerformed
    test = testSelect.getSelectedItem().toString();
    }//GEN-LAST:event_testSelectActionPerformed

    private void runTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTestActionPerformed
    System.out.println(test);
        try {
            run(test);
        } catch (Exception ex) {
            Logger.getLogger(RunTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_runTestActionPerformed

private void run(String test) throws IOException, Exception{
    if(test == null) {test = testSelect.getSelectedItem().toString();}
    
    if (test.equals("TestReadCsv - testExtractFileSelect"))
    {Toolkit.TestReadCsv trc = new Toolkit.TestReadCsv();
    trc.testExtractFileSelect();
    }   
    else if (test.equals("TestReadCsv - testExtractText"))
    {Toolkit.TestReadCsv trc = new Toolkit.TestReadCsv();
    trc.testExtractText();
    }  
    else if (test.equals("TestReadFileWebpage - testSaveImage"))
    {Toolkit.TestReadFileWebpage trc = new Toolkit.TestReadFileWebpage();
    trc.testSaveImage();
    }   
    else if (test.equals("TestReadFileWebpage - testReadAndSave_Images"))
    {Toolkit.TestReadFileWebpage trc = new Toolkit.TestReadFileWebpage();
    trc.testReadAndSave_Images();
    }  
    else if (test.equals("TestReadFileWebpage - testReadFileToArrayList"))
    {Toolkit.TestReadFileWebpage trc = new Toolkit.TestReadFileWebpage();
    trc.testReadFileToArrayList();
    }  
    else if (test.equals("TestReadFileWebpage - testGetWebpage"))
    {Toolkit.TestReadFileWebpage trc = new Toolkit.TestReadFileWebpage();
    trc.testGetWebpage();
    }  
    else if (test.equals("TestReadFileWebpage - testReadFileToStringBuilder"))
    {Toolkit.TestReadFileWebpage trc = new Toolkit.TestReadFileWebpage();
    trc.testReadFileToStringBuilder();
    } 
    else if (test.equals("TestTextSearch - testSearchManyStartEndKeywords"))
    {Toolkit.TestTextSearch trc = new Toolkit.TestTextSearch();
    trc.testSearchManyStartEndKeywords();
    }  
    else if (test.equals("TestTextSearch - testSearchManyStarkKeywordPlusOffset"))
    {Toolkit.TestTextSearch trc = new Toolkit.TestTextSearch();
    trc.testSearchManyStarkKeywordPlusOffset();
    }  
       
    else {
   JOptionPane.showMessageDialog(null, "Test not configured", "Error", JOptionPane.ERROR_MESSAGE);}
}

private void copyExampleFiles() {
try{
    Files.createDirectories(Paths.get("./example_files"));
String rep = "https://github.com/mjwhitton/AnalysisToolkit/tree/master/example_files/";
String[] urls = {"image_urls.txt", "scopus-search-results-Takaaki-Kajita.csv", "search1.csv", "ComputerScienceArticles.htm"};
for (int i=0; i<urls.length; i++){
ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048, "./example_files/");
  String destinationFile = rwsf.autoName(rep+urls[i]);
  rwsf.setFile(rep+urls[i], destinationFile);
  rwsf.readProcess(0);}
   }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when saving examples files" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
}

private void copyGeneralFiles() {
try{
    Files.createDirectories(Paths.get("./example_files"));
String rep = "https://github.com/mjwhitton/AnalysisToolkit/tree/master/";
String[] urls = {"License.txt", "README.TXT"};
for (int i=0; i<urls.length; i++){
ReadWebpageSaveFile rwsf = new ReadWebpageSaveFile(2048, "./");
  String destinationFile = rwsf.autoName(rep+urls[i]);
  rwsf.setFile(rep+urls[i], destinationFile);
  rwsf.readProcess(0);}
   }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error when saving examples files" +ex, "Error", JOptionPane.ERROR_MESSAGE);};
}

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
            java.util.logging.Logger.getLogger(RunTests.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunTests.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunTests.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunTests.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RunTests().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton runTest;
    private javax.swing.JComboBox<String> testSelect;
    // End of variables declaration//GEN-END:variables
    private String test;
}
