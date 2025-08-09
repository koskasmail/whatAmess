

#### java create get pdf/doc/doc/xls/xlsx/mht/html and show it on gui like swing with ,toolbar ,menubar, exit, print, export, display, routate, save, etc, basic function, convert from type to type with option to read base64 data, or regular file

Creating a Java application that can **open, display, and convert documents (PDF, DOC, DOCX, XLS, XLSX, MHT, HTML)** with a **Swing GUI** featuring a **toolbar, menubar, and basic functions** like **exit, print, export, rotate, save**, and support for **Base64 or regular file input** is a fairly advanced but achievable task. Here's a breakdown of how you can approach this:

---

## 🧰 Libraries You’ll Need

To handle various document types and conversions, you’ll need third-party libraries:

| Format       | Library Suggestions                                  |
|--------------|------------------------------------------------------|
| PDF          | [Apache PDFBox](https://pdfbox.apache.org/)         |
| DOC/DOCX     | [Apache POI](https://poi.apache.org/)               |
| XLS/XLSX     | [Apache POI](https://poi.apache.org/)               |
| MHT/HTML     | [jsoup](https://jsoup.org/) or [Flying Saucer](https://github.com/flyingsaucerproject/flyingsaucer) |
| Base64 Input | Java's built-in `Base64` class                      |

---

## 🖥️ GUI with Swing

You’ll build a Swing GUI with:
- **JFrame** for the main window
- **JMenuBar** for File/Export/Exit
- **JToolBar** for quick actions (Print, Rotate, Save)
- **JPanel** or **JEditorPane** for displaying content

---

## 🧩 Core Features to Implement

### 1. **Load File or Base64**
```java
byte[] data;
if (isBase64) {
    data = Base64.getDecoder().decode(base64String);
} else {
    data = Files.readAllBytes(Paths.get(filePath));
}
```

### 2. **Display Document**
Use different viewers depending on the file type:
- PDF: PDFBox to render pages as images
- DOC/DOCX: Apache POI to extract text
- XLS/XLSX: Apache POI to extract tables
- HTML/MHT: Jsoup or JEditorPane

### 3. **Convert Between Formats**
You’ll need logic to convert:
- DOCX → PDF (via POI + PDFBox)
- HTML → PDF (via Flying Saucer)
- XLSX → CSV or PDF (via POI)

### 4. **Print Support**
```java
PrinterJob job = PrinterJob.getPrinterJob();
job.setPrintable(myPrintableComponent);
job.print();
```

### 5. **Save/Export**
Use `FileOutputStream` to write converted data to disk.

### 6. **Rotate (PDF Pages)**
PDFBox allows page rotation:
```java
PDPage page = document.getPage(0);
page.setRotation(90); // Rotate 90 degrees
```

---

## 🧪 Sample Skeleton Code

```java
public class DocumentViewer extends JFrame {
    private JEditorPane editorPane;
    private JFileChooser fileChooser;

    public DocumentViewer() {
        setTitle("Document Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        JButton printButton = new JButton("Print");
        JButton saveButton = new JButton("Save");
        JButton rotateButton = new JButton("Rotate");
        toolBar.add(printButton);
        toolBar.add(saveButton);
        toolBar.add(rotateButton);
        add(toolBar, BorderLayout.NORTH);

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        openItem.addActionListener(e -> openFile());
        exitItem.addActionListener(e -> System.exit(0));
    }

    private void openFile() {
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Detect type and display accordingly
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DocumentViewer().setVisible(true));
    }
}
```

---

## 🚀 Next Steps

Would you like me to help you:
- Build a full working example for one format (e.g., PDF)?
- Add Base64 input support?
- Implement conversion between formats?

Let’s pick one feature to build out first and expand from there.


-----

#### Can you provide a full example for one format?


Absolutely! Let's start with a **full working example for PDF files**, using **Apache PDFBox** and **Swing**. This app will:

- Load a PDF from file or Base64
- Display it as images in a scrollable panel
- Include a toolbar with **Open**, **Save**, **Rotate**, and **Print**
- Include a menubar with **Exit**

---

## 🧩 Prerequisites

Add **Apache PDFBox** to your project. If you're using Maven:

```xml
<dependency>
  <groupId>org.apache.pdfbox</groupId>
  <artifactId>pdfbox</artifactId>
  <version>2.0.29</version>
</dependency>
```

---

## 🖥️ Full Java Example: PDF Viewer with Swing

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.print.*;

public class PDFViewer extends JFrame {
    private JPanel imagePanel;
    private PDDocument document;
    private PDFRenderer renderer;
    private int rotation = 0;

    public PDFViewer() {
        setTitle("PDF Viewer");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        JButton openButton = new JButton("Open");
        JButton base64Button = new JButton("Open Base64");
        JButton saveButton = new JButton("Save");
        JButton rotateButton = new JButton("Rotate");
        JButton printButton = new JButton("Print");

        toolBar.add(openButton);
        toolBar.add(base64Button);
        toolBar.add(saveButton);
        toolBar.add(rotateButton);
        toolBar.add(printButton);
        add(toolBar, BorderLayout.NORTH);

        // Image Panel
        imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        openButton.addActionListener(e -> openPDF());
        base64Button.addActionListener(e -> openBase64PDF());
        saveButton.addActionListener(e -> savePDF());
        rotateButton.addActionListener(e -> rotatePDF());
        printButton.addActionListener(e -> printPDF());
    }

    private void openPDF() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                loadPDF(Files.readAllBytes(file.toPath()));
            } catch (IOException ex) {
                showError("Failed to open PDF.");
            }
        }
    }

    private void openBase64PDF() {
        String base64 = JOptionPane.showInputDialog(this, "Paste Base64 PDF data:");
        if (base64 != null && !base64.isEmpty()) {
            try {
                byte[] data = Base64.getDecoder().decode(base64);
                loadPDF(data);
            } catch (IllegalArgumentException | IOException ex) {
                showError("Invalid Base64 or failed to load.");
            }
        }
    }

    private void loadPDF(byte[] data) throws IOException {
        if (document != null) document.close();
        document = PDDocument.load(data);
        renderer = new PDFRenderer(document);
        renderPages();
    }

    private void renderPages() throws IOException {
        imagePanel.removeAll();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 150);
            if (rotation != 0) {
                image = rotateImage(image, rotation);
            }
            JLabel label = new JLabel(new ImageIcon(image));
            imagePanel.add(label);
        }
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private BufferedImage rotateImage(BufferedImage img, int angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage rotated = new BufferedImage(h, w, img.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(Math.toRadians(angle), h / 2.0, h / 2.0);
        g2d.drawImage(img, 0, -w, null);
        g2d.dispose();
        return rotated;
    }

    private void rotatePDF() {
        rotation = (rotation + 90) % 360;
        try {
            renderPages();
        } catch (IOException ex) {
            showError("Failed to rotate.");
        }
    }

    private void savePDF() {
        if (document == null) return;
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                document.save(chooser.getSelectedFile());
            } catch (IOException ex) {
                showError("Failed to save PDF.");
            }
        }
    }

    private void printPDF() {
        if (document == null) return;
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new org.apache.pdfbox.printing.PDFPageable(document));
        try {
            job.print();
        } catch (PrinterException ex) {
            showError("Failed to print.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PDFViewer().setVisible(true));
    }
}
```

---

## ✅ Features Included

- Load PDF from file or Base64
- Display pages as images
- Rotate pages
- Save to disk
- Print via system printer
- Simple GUI with toolbar and menubar

---

Would you like to expand this to support DOCX or XLSX next? Or add format conversion (e.g., PDF → image or DOCX → PDF)?

------

#### Can you explain how to handle errors in this example?

Absolutely! Handling errors gracefully is crucial for a good user experience. In the PDF viewer example, we already use some basic error handling with `try-catch` blocks and `JOptionPane` dialogs. Let’s break down how error handling works in this context and how you can improve it further.

---

## 🛠️ Error Handling Strategy

### 1. **Use `try-catch` Blocks Around Risky Operations**

Operations like file I/O, Base64 decoding, PDF rendering, and printing can throw exceptions. We wrap these in `try-catch` blocks to prevent the app from crashing.

**Example:**
```java
try {
    byte[] data = Files.readAllBytes(file.toPath());
    loadPDF(data);
} catch (IOException ex) {
    showError("Failed to open PDF.");
}
```

### 2. **Centralized Error Display**

We use a helper method `showError(String message)` to display errors consistently:

```java
private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
}
```

This keeps the UI clean and avoids repeating `JOptionPane` code everywhere.

---

## 🔍 Common Error Scenarios and Handling

| Scenario                     | Exception Type        | Handling Approach                          |
|-----------------------------|-----------------------|--------------------------------------------|
| File not found              | `IOException`         | Show error message, suggest retry          |
| Invalid Base64 input        | `IllegalArgumentException` | Notify user of malformed input         |
| PDF rendering failure       | `IOException`         | Show error, possibly reload document       |
| Print job failure           | `PrinterException`    | Notify user, suggest checking printer      |
| Save failure                | `IOException`         | Show error, suggest checking permissions   |
| Null document access        | `NullPointerException`| Check for null before operations           |

---

## ✅ Best Practices to Improve Error Handling

### 🔒 Validate Inputs Early
Before decoding Base64 or loading files, check if the input is empty or null.

```java
if (base64 == null || base64.trim().isEmpty()) {
    showError("Base64 input is empty.");
    return;
}
```

### 🧹 Clean Up Resources
Always close documents after use to avoid memory leaks.

```java
if (document != null) {
    try {
        document.close();
    } catch (IOException ex) {
        showError("Failed to close document.");
    }
}
```

### 🧪 Log Errors for Debugging
You can log errors to the console or a file for developers:

```java
ex.printStackTrace(); // Or use a logging framework
```

### 🧭 Guide the User
Instead of just saying “Failed to open PDF,” you can be more helpful:

```java
showError("Failed to open PDF. Please check if the file is valid and not corrupted.");
```

---

## 🧼 Optional: Add a Status Bar

You can add a status bar at the bottom of the window to show non-blocking messages (e.g., “Loading PDF…” or “Saved successfully”).

```java
JLabel statusBar = new JLabel("Ready");
add(statusBar, BorderLayout.SOUTH);
statusBar.setText("Loading PDF...");
```

---

Would you like me to refactor the example to include a status bar and more robust error handling? Or add logging to a file for debugging?

------


####

------


####

------
