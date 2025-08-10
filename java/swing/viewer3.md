
-----

##### doc

To properly display the contents of a `.doc` file (Microsoft Word 97–2003 format) in a **Swing** environment dynamically, you’ll want to:

1. Ensure the document is read safely.
2. Avoid blocking the UI thread.
3. Handle encoding and layout gracefully.

Here’s a **fixed and improved version** of your `showDOC` method using Apache POI’s `HWPFDocument` and `WordExtractor`, with Swing best practices:

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.SwingWorker;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

private void showDOC(File file) {
    new SwingWorker<String, Void>() {
        @Override
        protected String doInBackground() throws Exception {
            try (FileInputStream fis = new FileInputStream(file);
                 HWPFDocument doc = new HWPFDocument(fis);
                 WordExtractor extractor = new WordExtractor(doc)) {
                return extractor.getText();
            }
        }

        @Override
        protected void done() {
            try {
                textArea.setText(get());
            } catch (Exception e) {
                textArea.setText("Failed to load document: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }.execute();
}
```

### ✅ Improvements:
- **`SwingWorker`**: Runs file reading in a background thread to keep the UI responsive.
- **Try-with-resources**: Ensures streams and document objects are closed properly.
- **Error handling**: Displays a message in the `textArea` if something goes wrong.

### 🧠 Bonus Tip:
If you want to support `.docx` files (Word 2007+), you’ll need to use `XWPFDocument` and `XWPFWordExtractor` from `poi-ooxml`.

Would you like me to add support for `.docx` as well?


-----

##### docx

To handle `.docx` files (Microsoft Word 2007 and newer) in Java, you’ll want to use **Apache POI’s `XWPFDocument`** class, which is part of the `poi-ooxml` module. This lets you extract text and work with modern Word documents.

Here’s how you can extend your Swing app to support `.docx` files dynamically:

---

### ✅ Step-by-step code to read `.docx` and display in a `JTextArea`:

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.SwingWorker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

private void showDOCX(File file) {
    new SwingWorker<String, Void>() {
        @Override
        protected String doInBackground() throws Exception {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument docx = new XWPFDocument(fis);
                 XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {
                return extractor.getText();
            }
        }

        @Override
        protected void done() {
            try {
                textArea.setText(get());
            } catch (Exception e) {
                textArea.setText("Failed to load .docx file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }.execute();
}
```

---

### 📦 Required Maven dependency (already in your `pom.xml`):

You're good to go with this:

```xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>5.4.1</version>
</dependency>
```

---

### 🧠 Bonus: Unified method for `.doc` and `.docx`

You can detect the file type by extension and call the appropriate method:

```java
private void showWordDocument(File file) {
    String name = file.getName().toLowerCase();
    if (name.endsWith(".docx")) {
        showDOCX(file);
    } else if (name.endsWith(".doc")) {
        showDOC(file);
    } else {
        textArea.setText("Unsupported file format.");
    }
}
```

Would you like to add formatting support too — like bold, italics, or tables? That’s a bit more advanced but totally doable.

-----

##### doc to docx

You got it! To convert a `.doc` file (older binary format) to `.docx` (modern XML format) in Java, you can use **Apache POI** to read the `.doc` content and write it into a new `.docx` file.

Here’s a complete function that does just that:

---

### 🔄 Java Function: Convert `.doc` to `.docx`

```java
import java.io.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public void convertDocToDocx(File docFile, File docxFile) throws IOException {
    try (FileInputStream fis = new FileInputStream(docFile);
         HWPFDocument doc = new HWPFDocument(fis);
         WordExtractor extractor = new WordExtractor(doc);
         XWPFDocument docx = new XWPFDocument();
         FileOutputStream out = new FileOutputStream(docxFile)) {

        String[] paragraphs = extractor.getParagraphText();
        for (String para : paragraphs) {
            XWPFParagraph p = docx.createParagraph();
            p.createRun().setText(para.trim());
        }

        docx.write(out);
        System.out.println("Conversion complete: " + docxFile.getAbsolutePath());
    }
}
```

---

### 📦 Maven Dependencies Required

Make sure your `pom.xml` includes:

```xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi</artifactId>
  <version>5.4.1</version>
</dependency>
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>5.4.1</version>
</dependency>
```

---

### 🧪 Example Usage

```java
File docFile = new File("input.doc");
File docxFile = new File("output.docx");
convertDocToDocx(docFile, docxFile);
```

---

This method preserves basic text and paragraph structure. If you need to retain **styles, tables, or images**, that would require a more advanced approach using POI’s low-level APIs.

Want to add support for batch conversion or a GUI button to trigger this? I can help wire that up too.

-----

##### xxx




