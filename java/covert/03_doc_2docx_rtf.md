
## 03_doc_2docx_rtf.md

##### copliot

Sure! Here's a Java 1.8-compatible example that demonstrates two different methods for converting old `.doc` (binary Word format) files into `.docx` and `.rtf` formats using Apache POI and docx4j libraries.

> ⚠️ **Note**: Apache POI can read `.doc` files but cannot write `.docx`. docx4j can write `.docx` but not read `.doc`. So we’ll use Apache POI to read `.doc`, and then convert the content using either docx4j or RTF APIs.

---

## 🛠 Prerequisites

Add these dependencies to your `pom.xml` if you're using Maven:

```xml
<dependencies>
  <!-- Apache POI for reading .doc -->
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-scratchpad</artifactId>
    <version>5.2.3</version>
  </dependency>

  <!-- docx4j for writing .docx -->
  <dependency>
    <groupId>org.docx4j</groupId>
    <artifactId>docx4j</artifactId>
    <version>11.4.8</version>
  </dependency>
</dependencies>
```

---

## 📄 Method 1: Convert `.doc` to `.docx` using Apache POI + docx4j

```java
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;

import java.io.*;

public class DocToDocxConverter {
    public static void convertDocToDocx(File docFile, File docxFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(docFile)) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            String text = extractor.getText();

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            ObjectFactory factory = new ObjectFactory();
            P paragraph = factory.createP();
            Text t = factory.createText();
            t.setValue(text);
            paragraph.getContent().add(factory.createR().withContent(t));
            wordMLPackage.getMainDocumentPart().getContent().add(paragraph);

            wordMLPackage.save(docxFile);
        }
    }
}
```

---

## 📄 Method 2: Convert `.doc` to `.rtf` using Apache POI + Java's built-in RTF support

```java
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;

public class DocToRtfConverter {
    public static void convertDocToRtf(File docFile, File rtfFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(docFile)) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            String text = extractor.getText();

            DefaultStyledDocument styledDoc = new DefaultStyledDocument();
            styledDoc.insertString(0, text, null);

            try (FileOutputStream fos = new FileOutputStream(rtfFile)) {
                RTFEditorKit rtfKit = new RTFEditorKit();
                rtfKit.write(fos, styledDoc, 0, styledDoc.getLength());
            }
        }
    }
}
```

---

## 🚀 How to Use

```java
public class Main {
    public static void main(String[] args) throws Exception {
        File inputDoc = new File("old.doc");
        File outputDocx = new File("converted.docx");
        File outputRtf = new File("converted.rtf");

        DocToDocxConverter.convertDocToDocx(inputDoc, outputDocx);
        DocToRtfConverter.convertDocToRtf(inputDoc, outputRtf);

        System.out.println("Conversion complete!");
    }
}
```

---

Would you like to add formatting or images from the original `.doc` file as well? That’s a bit trickier but doable with some extra effort.
