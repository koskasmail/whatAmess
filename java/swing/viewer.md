# viewer

#### create a java program that read a pdf/doc/docx/excel/excelx/ppt/pps/mht file and show it inside java swing window note you can use other viewer if wish but not awt

Creating a Java Swing application that can read and display multiple file formats like PDF, DOC/DOCX, XLS/XLSX, PPT/PPS, and MHT is a complex task, as each format requires different libraries for parsing and rendering. Below is a **modular approach** using popular open-source libraries, all integrated into a **Swing-based viewer**.

---

### 🛠 Required Libraries

You'll need the following libraries:

| Format     | Library Used                          |
|------------|----------------------------------------|
| PDF        | [PDFBox](https://pdfbox.apache.org/)   |
| DOC/DOCX   | [Apache POI](https://poi.apache.org/)  |
| XLS/XLSX   | Apache POI                             |
| PPT/PPS    | Apache POI                             |
| MHT        | [jsoup](https://jsoup.org/)            |

---

### 📦 Maven Dependencies

Add these to your `pom.xml`:

```xml
<dependencies>
    <!-- PDFBox -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.29</version>
    </dependency>

    <!-- Apache POI for DOC/DOCX/XLS/XLSX/PPT/PPS -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>5.2.3</version>
    </dependency>
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version>
    </dependency>

    <!-- Jsoup for MHT -->
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.16.1</version>
    </dependency>
</dependencies>
```

---

### 🖥️ Java Swing Viewer

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

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
        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 150);
        imageLabel.setIcon(new ImageIcon(image));
        document.close();
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

---

### ✅ Notes

- This viewer shows **PDF as image**, others as **text**.
- For full rendering (e.g., formatted DOCX or PPT), consider embedding a browser component like **JCEF** or **JavaFX WebView**, but that would require JavaFX.
- MHT is treated as HTML and parsed with jsoup.

Would you like to extend this to support **searching**, **printing**, or **exporting** the content?

-----

#### extend this to support searching, printing, or exporting


Great! Let's enhance the viewer with these features:

---

## 🔍 1. **Search Functionality**

We'll add a search bar to highlight matching text in the `JTextArea`.

### ✅ Changes:
- Add a `JTextField` for input
- Add a `JButton` to trigger search
- Highlight matches using `Highlighter`

### 🔧 Code Snippet:

```java
private void addSearchBar() {
    JPanel searchPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField();
    JButton searchButton = new JButton("Search");

    searchButton.addActionListener(e -> highlightText(searchField.getText()));
    searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.add(searchButton, BorderLayout.EAST);

    add(searchPanel, BorderLayout.SOUTH);
}

private void highlightText(String pattern) {
    Highlighter highlighter = textArea.getHighlighter();
    highlighter.removeAllHighlights();
    String text = textArea.getText().toLowerCase();
    pattern = pattern.toLowerCase();

    int index = text.indexOf(pattern);
    while (index >= 0) {
        try {
            highlighter.addHighlight(index, index + pattern.length(),
                    new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
            index = text.indexOf(pattern, index + pattern.length());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
```

Call `addSearchBar()` in the constructor after initializing components.

---

## 🖨️ 2. **Print Support**

Use `PrinterJob` to print the content of the `JTextArea`.

### 🔧 Code Snippet:

```java
private void addPrintOption() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem printItem = new JMenuItem("Print");

    printItem.addActionListener(e -> {
        try {
            boolean done = textArea.print();
            if (!done) JOptionPane.showMessageDialog(this, "Printing canceled.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error printing: " + ex.getMessage());
        }
    });

    fileMenu.add(printItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);
}
```

Call `addPrintOption()` in the constructor.

---

## 📤 3. **Export to TXT**

Allow users to export the displayed content to a `.txt` file.

### 🔧 Code Snippet:

```java
private void addExportOption() {
    JMenuBar menuBar = getJMenuBar();
    JMenu fileMenu = menuBar.getMenu(0); // Assuming "File" is first

    JMenuItem exportItem = new JMenuItem("Export to TXT");
    exportItem.addActionListener(e -> {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("export.txt"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this, "Exported successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting: " + ex.getMessage());
            }
        }
    });

    fileMenu.add(exportItem);
}
```

Call `addExportOption()` after `addPrintOption()`.

---

## 🧩 Final Integration

Update your constructor like this:

```java
public FileViewer(File file) {
    // ... existing setup
    addPrintOption();
    addExportOption();
    addSearchBar();
}
```

---

## 🚀 Optional Enhancements

- Export to PDF using [iText](https://itextpdf.com/)
- Add a toolbar with icons for search/print/export
- Support printing images (PDF rendering) via `Printable`

Would you like me to help you add **PDF export** or a **toolbar UI** next?

-----

#### Can you provide code for PDF export functionality?

Absolutely! Let's add a feature to **export the displayed content to a PDF file** using the [**iText 7**](https://itextpdf.com/) library.

---

## 📦 Step 1: Add iText Dependency

If you're using Maven, include this in your `pom.xml`:

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
    <type>pom</type>
</dependency>
```

If you're not using Maven, download the iText 7 jars from [iText's GitHub](https://github.com/itext/itext7) or [official site](https://itextpdf.com/en/resources/downloads).

---

## 🧾 Step 2: Add Export to PDF Option

Add this method to your `FileViewer` class:

```java
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

private void addPDFExportOption() {
    JMenuBar menuBar = getJMenuBar();
    JMenu fileMenu = menuBar.getMenu(0); // Assuming "File" is first

    JMenuItem exportPDFItem = new JMenuItem("Export to PDF");
    exportPDFItem.addActionListener(e -> {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("export.pdf"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String content = textArea.getText();
                PdfWriter writer = new PdfWriter(chooser.getSelectedFile());
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);
                document.add(new Paragraph(content));
                document.close();
                JOptionPane.showMessageDialog(this, "PDF exported successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting PDF: " + ex.getMessage());
            }
        }
    });

    fileMenu.add(exportPDFItem);
}
```

---

## 🧩 Step 3: Integrate It

Call this method in your constructor:

```java
addPrintOption();
addExportOption();
addPDFExportOption();  // 👈 Add this line
addSearchBar();
```

---

## ✅ Result

Your app now supports:
- Viewing multiple file formats
- Searching text
- Printing
- Exporting to TXT
- Exporting to PDF 🎉

---

#### Would you like to export **PDF with styled formatting**, or include **images (like rendered PDF pages)** in the exported file?

-----

#### Can you provide a sample with styled formatting?

Absolutely! Let's enhance the PDF export to include **styled formatting** using **iText 7**. We'll apply:

- Custom fonts and sizes
- Bold headings
- Line spacing
- Page margins

---

## 🖋️ Styled PDF Export with iText 7

Here’s a complete method to export styled content from your `JTextArea`:

```java
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.kernel.colors.*;

private void addStyledPDFExportOption() {
    JMenuBar menuBar = getJMenuBar();
    JMenu fileMenu = menuBar.getMenu(0); // Assuming "File" is first

    JMenuItem exportStyledPDFItem = new JMenuItem("Export Styled PDF");
    exportStyledPDFItem.addActionListener(e -> {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("styled_export.pdf"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                PdfWriter writer = new PdfWriter(chooser.getSelectedFile());
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc, PageSize.A4);
                document.setMargins(40, 40, 40, 40);

                // Title
                Paragraph title = new Paragraph("Exported Document")
                        .setFontSize(18)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(title);

                // Content
                String[] lines = textArea.getText().split("\n");
                for (String line : lines) {
                    Paragraph p = new Paragraph(line)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(5)
                            .setLineSpacing(1.2f);
                    document.add(p);
                }

                document.close();
                JOptionPane.showMessageDialog(this, "Styled PDF exported successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting styled PDF: " + ex.getMessage());
            }
        }
    });

    fileMenu.add(exportStyledPDFItem);
}
```

---

## ✨ Styling Features Used

| Element     | Style Applied                      |
|-------------|------------------------------------|
| Title       | Bold, centered, larger font        |
| Paragraphs  | Line spacing, margin, font size    |
| Page        | A4 size, custom margins            |

---

## 🧩 Integration

Call this in your constructor:

```java
addStyledPDFExportOption();  // 👈 Add this line
```

---

## 🧠 Want More?

You can also:
- Add **headers/footers**
- Use **custom fonts** (TTF)
- Add **tables or images**
- Highlight search terms in the PDF

Would you like to include **images or tables** in the exported PDF next?

-----



-----



-----



-----



-----




-----



-----
