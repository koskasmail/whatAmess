
## 04_doc_2docx_rtf.md

###### chatgpt

Got it 👍
You want a **Java 1.8 program** that can take **old Microsoft Word `.doc` (binary/complex format)** and convert it into:

1. **DOCX** (Office Open XML format)
2. **RTF** (Rich Text Format)

using **two different methods**.

Because `.doc` is the old binary Word format, you’ll need libraries like **Apache POI (HWPF/XWPF)** or **docx4j**. Below are two approaches:

---

## **Method 1: Apache POI (HWPF → XWPF & RTF)**

```java
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class DocConverterPOI {

    // Convert DOC -> DOCX
    public static void convertDocToDocx(String docPath, String docxPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(docPath);
             HWPFDocument doc = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(doc);
             XWPFDocument docx = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(docxPath)) {

            String text = extractor.getText();
            docx.createParagraph().createRun().setText(text);
            docx.write(fos);
        }
    }

    // Convert DOC -> RTF
    public static void convertDocToRtf(String docPath, String rtfPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(docPath);
             HWPFDocument doc = new HWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(rtfPath);
             PrintWriter writer = new PrintWriter(fos)) {

            WordExtractor extractor = new WordExtractor(doc);
            String text = extractor.getText();

            writer.println("{\\rtf1\\ansi\\deff0");
            writer.println(text.replace("\n", "\\par "));
            writer.println("}");
        }
    }

    public static void main(String[] args) throws Exception {
        String inputDoc = "input.doc";
        convertDocToDocx(inputDoc, "output.docx");
        convertDocToRtf(inputDoc, "output.rtf");

        System.out.println("Conversion finished!");
    }
}
```

✅ Pros: Works with Java 1.8, uses Apache POI (common library).
⚠️ Cons: Formatting (styles, tables, images) may be lost – it mainly extracts text.

---

## **Method 2: docx4j + JODConverter (via LibreOffice/OpenOffice)**

If you need **better fidelity (full formatting, tables, images)**, the robust way is to use **docx4j** with LibreOffice headless mode.

```java
import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;

import java.io.File;

public class DocConverterLibre {

    // Convert DOC -> DOCX or RTF using LibreOffice
    public static void convert(String inputPath, String outputPath) throws Exception {
        LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {
            officeManager.start();

            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);

            LocalConverter.make()
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();

        } finally {
            officeManager.stop();
        }
    }

    public static void main(String[] args) throws Exception {
        String inputDoc = "input.doc";

        convert(inputDoc, "output.docx");
        convert(inputDoc, "output.rtf");

        System.out.println("Conversion finished!");
    }
}
```

📦 Required dependencies:

* `org.jodconverter:jodconverter-local`
* LibreOffice installed on the system.

✅ Pros: Preserves layout, formatting, tables, images.
⚠️ Cons: Requires LibreOffice/OpenOffice installed.

---

👉 So you now have **two approaches**:

1. **Pure Java/Apache POI** → lightweight but text-only.
2. **JODConverter + LibreOffice** → full-feature conversion.

Do you want me to also give you the **Maven `pom.xml` dependencies** for both methods so you can run them immediately in Java 1.8?
