To merge two PDF files into a single PDF using Java, you can use the Apache PDFBox library. Here is a step-by-step guide to achieve this:

Add PDFBox to your project: If you are using Maven, add the following dependency to your pom.xml file:

xml
```
Copy code
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.27</version>
</dependency>
```
Create a Java program to merge the PDFs: Below is the code to merge two PDF files into one.

java
Copy code
```
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PDFMerger {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PDF Merger");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            JButton mergeButton = new JButton("Merge PDFs");
            panel.add(mergeButton);
            frame.add(panel, BorderLayout.CENTER);

            mergeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setMultiSelectionEnabled(true);

                    int result = fileChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File[] selectedFiles = fileChooser.getSelectedFiles();
                        if (selectedFiles.length != 2) {
                            JOptionPane.showMessageDialog(frame, "Please select exactly 2 PDF files.");
                            return;
                        }

                        File mergedFile = new File("merged.pdf");
                        try {
                            mergePDFs(selectedFiles[0], selectedFiles[1], mergedFile);
                            JOptionPane.showMessageDialog(frame, "PDFs merged successfully: " + mergedFile.getAbsolutePath());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Error merging PDFs: " + ex.getMessage());
                        }
                    }
                }
            });

            frame.setVisible(true);
        });
    }

    private static void mergePDFs(File pdf1, File pdf2, File output) throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.addSource(pdf1);
        pdfMerger.addSource(pdf2);
        pdfMerger.setDestinationFileName(output.getAbsolutePath());
        pdfMerger.mergeDocuments(null);
    }
}
```

Explanation of the code:

Import statements: Import necessary classes from org.apache.pdfbox and javax.swing.
PDFMerger class: This class contains the main method and other methods to create and display the Swing application.
Swing application setup: The main method sets up a Swing JFrame with a JButton to trigger the PDF merge process.
File chooser: A JFileChooser is used to let the user select two PDF files.
Merge PDFs: The mergePDFs method uses PDFBox's PDFMergerUtility to merge the selected PDF files into a single output file.
Button action listener: When the merge button is clicked, the selected files are validated, and if two PDF files are selected, they are merged into a file named merged.pdf.
Running the application:

Compile and run the PDFMerger class.
When the application starts, click the "Merge PDFs" button.
Select exactly two PDF files using the file chooser.
After selecting the files, the application will merge them and save the output as merged.pdf in the current directory, displaying a success message.
This program uses a simple Swing interface to select PDF files and merge them using PDFBox. You can enhance the application by adding more features, such as selecting the output file location or handling more than two files.








