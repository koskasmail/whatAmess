
# myviewer_ver_1


#### pom.xml
```
```

-----

#### FileViewer.java
```
package jaron;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;

public class FileViewer extends JFrame {

    private JTextArea textArea = new JTextArea();
    private JLabel imageLabel = new JLabel();

    public FileViewer(File file) {
        setTitle("File Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.NORTH);

        try {
            String name = file.getName().toLowerCase();
            if (name.endsWith(".pdf")) {
                showPDF(file);
            } else if (name.endsWith(".doc")) {
                showDOC(file);
            } else if (name.endsWith(".docx")) {
                showDOCX(file);
            } else if (name.endsWith(".xls")) {
                showExcel(new HSSFWorkbook(new FileInputStream(file)));
            } else if (name.endsWith(".xlsx")) {
                showExcel(new XSSFWorkbook(new FileInputStream(file)));
            } else if (name.endsWith(".ppt")) {
                showPPT(new HSLFSlideShow(new FileInputStream(file)));
            } else if (name.endsWith(".pps")) {
                showPPT(new HSLFSlideShow(new FileInputStream(file)));
            } else if (name.endsWith(".mht")) {
                showMHT(file);
            } else {
                textArea.setText("Unsupported file format.");
            }
        } catch (Exception e) {
            textArea.setText("Error reading file: " + e.getMessage());
        }
    }

    private void showPDF(File file) throws IOException {
    	try {
    		
//    	(PDDocument document = Loader.loadPDF(file)) {
//            PDFRenderer renderer = new PDFRenderer(document);
//            BufferedImage image = renderer.renderImageWithDPI(0, 150);
//            imageLabel.setIcon(new ImageIcon(image));
            
        PDDocument document = Loader.loadPDF(file);
        
//        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 150);
        imageLabel.setIcon(new ImageIcon(image));
 
        document.close();
    	}
    	finally {
			System.out.println("finally");
		}
    }

    private void showDOC(File file) throws IOException {
        HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
        WordExtractor extractor = new WordExtractor(doc);
        textArea.setText(extractor.getText());
    }

    private void showDOCX(File file) throws IOException {
        XWPFDocument docx = new XWPFDocument(new FileInputStream(file));
        XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
        textArea.setText(extractor.getText());
    }

    private void showExcel(Workbook workbook) {
        StringBuilder sb = new StringBuilder();
        for (Sheet sheet : workbook) {
            sb.append("Sheet: ").append(sheet.getSheetName()).append("\n");
            for (Row row : sheet) {
                for (Cell cell : row) {
                    sb.append(cell.toString()).append("\t");
                }
                sb.append("\n");
            }
        }
        textArea.setText(sb.toString());
    }

    private void showPPT(HSLFSlideShow ppt) {
        StringBuilder sb = new StringBuilder();
        List<? extends Slide<?, ?>> slides = ppt.getSlides();
        for (Slide<?, ?> slide : slides) {
            sb.append("Slide: ").append(slide.getTitle()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void showMHT(File file) throws IOException {
        String html = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        textArea.setText(doc.text());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                FileViewer viewer = new FileViewer(chooser.getSelectedFile());
                viewer.setVisible(true);
            }
        });
    }
}
```

-----
