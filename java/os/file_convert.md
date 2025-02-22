<a name="topage"></a>

# file_convert

java read pdf, mht, doc, docx, xls, xlsx, html, and convert it into pdf format
ChatGPT
To read various document formats (PDF, MHT, DOC, DOCX, XLS, XLSX, HTML) and convert them into PDF format in Java, you can use libraries and tools like Apache PDFBox, Apache POI (for Microsoft Office formats), and Flying Saucer (for HTML and MHT). Below, I'll provide you with a basic example of how to achieve this using these libraries:

Add the necessary dependencies to your Java project. You can use tools like Maven or Gradle to manage dependencies. Here are the dependencies you'll need:
xml
Copy code
<!-- Apache PDFBox -->
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.30</version>
</dependency>

<!-- Apache POI (for DOC, DOCX, XLS, XLSX) -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.0.0</version>
</dependency>

<!-- Flying Saucer (for HTML and MHT) -->
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.1.23</version>
</dependency>
Create a Java program to convert various document formats to PDF:
java
Copy code
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTextExtractor;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

public class DocumentToPDFConverter {
    public static void main(String[] args) throws Exception {
        // Input file path
        String inputFile = "input.docx"; // Replace with your input file

        // Output PDF file path
        String outputFile = "output.pdf"; // Replace with your desired output file

        // Determine the file type and convert accordingly
        if (inputFile.endsWith(".pdf")) {
            // No need to convert, it's already a PDF
            // You can copy the input file to the output file if needed
            copyFile(inputFile, outputFile);
        } else if (inputFile.endsWith(".doc")) {
            convertDocToPdf(inputFile, outputFile);
        } else if (inputFile.endsWith(".docx")) {
            convertDocxToPdf(inputFile, outputFile);
        } else if (inputFile.endsWith(".xls") || inputFile.endsWith(".xlsx")) {
            // Add code to convert Excel to PDF here
        } else if (inputFile.endsWith(".html") || inputFile.endsWith(".mht")) {
            convertHtmlToPdf(inputFile, outputFile);
        } else {
            System.out.println("Unsupported file format.");
        }
    }

    // Helper method to copy a file
    private static void copyFile(String source, String destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        fis.close();
        fos.close();
    }

    // Convert DOC to PDF
    private static void convertDocToPdf(String docFile, String pdfFile) throws Exception {
        FileInputStream fis = new FileInputStream(docFile);
        HWPFDocument doc = new HWPFDocument(fis);
        WordExtractor extractor = new WordExtractor(doc);
        String text = extractor.getText();
        PDDocument pdfDoc = new PDDocument();
        PDPage page = new PDPage();
        pdfDoc.addPage(page);
        PDStream contentStream = new PDStream(pdfDoc);
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.extractStreams = true;
        textStripper.setStartPage(0);
        textStripper.setEndPage(1);
        textStripper.processStream(page, contentStream.getContents());
        contentStream.addCompression();
        pdfDoc.save(pdfFile);
        pdfDoc.close();
        fis.close();
    }

    // Convert DOCX to PDF
    private static void convertDocxToPdf(String docxFile, String pdfFile) throws Exception {
        FileInputStream fis = new FileInputStream(docxFile);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFTextExtractor textExtractor = new XWPFTextExtractor(document);
        String text = textExtractor.getText();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(text);
        renderer.layout();
        FileOutputStream fos = new FileOutputStream(pdfFile);
        renderer.createPDF(fos);
        fos.close();
        fis.close();
    }

    // Convert HTML/MHT to PDF
    private static void convertHtmlToPdf(String htmlFile, String pdfFile) throws Exception {
        String url = new File(htmlFile).toURI().toURL().toString();
        OutputStream os = new FileOutputStream(pdfFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }
}
In this example, we've provided methods to convert DOC and DOCX files to PDF using Apache POI, and HTML/MHT to PDF using Flying Saucer. You can add additional functionality to handle XLS and XLSX files using Apache POI as needed.

Make sure to replace "input.docx" and "output.pdf" with your input and output file paths as necessary.

Please note that handling various document formats in Java can be complex, and this example covers only basic conversion scenarios. You may need to fine-tune and adapt the code to your specific requirements and handle edge cases for different document formats.


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
