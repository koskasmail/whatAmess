
# 05_convert_1

-----

# Java backend to convert office and PDF files to high‑fidelity HTML

You want server-side conversion of DOC, DOCX, XLS, XLSX, PPT, PPTX, and PDF into HTML that preserves layout: page breaks, headers/footers, fonts, images, and styling. Below are two solid approaches with production-ready code.

- Open-source stack (good, not perfect fidelity): LibreOffice + JODConverter for Office; pdf2htmlEX for PDF.
- Commercial stack (best fidelity): Aspose for Words/Cells/Slides/PDF using “fixed” HTML, which preserves pagination and headers/footers very well.

I’ll show both, wrapped in a Spring Boot API that returns HTML or renders it inline in the browser based on a “purpose” parameter.

---

## Option A: Open‑source stack (LibreOffice + pdf2htmlEX)

### What this gives you
- Good conversion for DOC/DOCX/XLS/XLSX/PPT/PPTX via LibreOffice headless.
- Excellent PDF-to-HTML via pdf2htmlEX (preserves layout, embedded fonts, vector graphics).
- Runs entirely on your server. Requires installing LibreOffice and pdf2htmlEX binaries.

### Project setup (Maven)

```xml
<!-- pom.xml -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>doc2html</artifactId>
  <version>1.0.0</version>
  <properties>
    <java.version>17</java.version>
    <spring.boot.version>3.3.0</spring.boot.version>
  </properties>

  <dependencies>
    <!-- Spring Boot Web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>${spring.boot.version}</version>
    </dependency>

    <!-- JODConverter Local (LibreOffice) -->
    <dependency>
      <groupId>org.jodconverter</groupId>
      <artifactId>jodconverter-local</artifactId>
      <version>4.4.7</version>
    </dependency>

    <!-- Apache Tika for MIME detection (optional but helpful) -->
    <dependency>
      <groupId>org.apache.tika</groupId>
      <artifactId>tika-core</artifactId>
      <version>2.9.2</version>
    </dependency>

    <!-- Lombok (optional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.32</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
      <version>${spring.boot.version}</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring.boot.version}</version>
      </plugin>
    </plugins>
  </build>
</project>
```

### Install required system packages
- LibreOffice (headless): e.g., `apt-get install libreoffice` or `yum install libreoffice`.
- pdf2htmlEX: install from your package manager or build from source.
- Ensure both `soffice` and `pdf2htmlEX` are on PATH for the app’s runtime user.

### Spring Boot application

```java
// src/main/java/com/example/doc2html/Doc2HtmlApplication.java
package com.example.doc2html;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Doc2HtmlApplication {
  public static void main(String[] args) {
    SpringApplication.run(Doc2HtmlApplication.class, args);
  }
}
```

### Conversion service

```java
// src/main/java/com/example/doc2html/service/ConversionService.java
package com.example.doc2html.service;

import org.apache.tika.Tika;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;

@Service
public class ConversionService implements AutoCloseable {

  private final LocalOfficeManager officeManager;
  private final DocumentConverter converter;
  private final Tika tika = new Tika();

  private static final Set<String> OFFICE_MIME = Set.of(
      "application/msword",                         // .doc
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
      "application/vnd.ms-excel",                   // .xls
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
      "application/vnd.ms-powerpoint",              // .ppt
      "application/vnd.openxmlformats-officedocument.presentationml.presentation" // .pptx
  );

  public ConversionService() throws OfficeException {
    // Start an internal LibreOffice instance
    this.officeManager = LocalOfficeManager.builder()
        .install()
        .build();
    this.officeManager.start();
    this.converter = JodConverter
        .builder()
        .officeManager(officeManager)
        .build();
  }

  public String detectMime(byte[] data, String filename) {
    try {
      String fromContent = tika.detect(data);
      if (fromContent != null) return fromContent;
    } catch (Exception ignored) {}
    return tika.detect(filename);
  }

  public byte[] convertToHtml(byte[] input, String filename) throws Exception {
    String mime = detectMime(input, filename);
    if ("application/pdf".equals(mime)) {
      return convertPdfToHtml(input);
    } else if (OFFICE_MIME.contains(mime)) {
      return convertOfficeToHtml(input, filename);
    } else {
      throw new IllegalArgumentException("Unsupported MIME type: " + mime);
    }
  }

  private byte[] convertOfficeToHtml(byte[] input, String filename) throws Exception {
    File in = Files.createTempFile("upload-", "-" + filename).toFile();
    File out = Files.createTempFile("out-", ".html").toFile();
    Files.write(in.toPath(), input);

    try {
      converter.convert(in).to(out).execute();
      // Read produced HTML; JODConverter may generate a folder for assets alongside
      String html = Files.readString(out.toPath(), StandardCharsets.UTF_8);
      // Optionally wrap with page CSS to visualize page breaks
      return wrapWithPageStyles(html).getBytes(StandardCharsets.UTF_8);
    } finally {
      in.delete();
      out.delete();
      // If asset directory created (e.g., out_.html_files), consider copying or inlining resources
    }
  }

  private byte[] convertPdfToHtml(byte[] input) throws Exception {
    File in = Files.createTempFile("upload-", ".pdf").toFile();
    File out = Files.createTempFile("out-", ".html").toFile();
    Files.write(in.toPath(), input);

    // Call pdf2htmlEX to preserve layout and fonts
    ProcessBuilder pb = new ProcessBuilder(
        "pdf2htmlEX",
        "--embed", "cfijo",   // embed CSS, fonts, images, JS, outlines
        "--optimize-text", "1",
        "--zoom", "1.0",
        in.getAbsolutePath(),
        out.getAbsolutePath()
    );
    pb.redirectErrorStream(true);
    Process p = pb.start();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      while (br.readLine() != null) { /* consume logs */ }
    }
    int exit = p.waitFor();
    if (exit != 0) throw new RuntimeException("pdf2htmlEX failed with exit code " + exit);

    try {
      String html = Files.readString(out.toPath(), StandardCharsets.UTF_8);
      return wrapWithPageStyles(html).getBytes(StandardCharsets.UTF_8);
    } finally {
      in.delete();
      out.delete();
    }
  }

  private String wrapWithPageStyles(String bodyHtml) {
    String css = """
      <style>
        /* Visual page frames if converter emits page boxes */
        .page, .sheet, .slide { 
          margin: 1rem auto; 
          box-shadow: 0 0 4px rgba(0,0,0,.2); 
          background: white;
        }
        @media print {
          .page-break { page-break-after: always; }
          body { margin: 0; }
        }
        body { background: #f2f2f7; }
      </style>
    """;
    if (bodyHtml.contains("<html")) return bodyHtml; // already a full document
    return "<!doctype html><html><head><meta charset=\"utf-8\">" + css + "</head><body>" + bodyHtml + "</body></html>";
  }

  @Override
  public void close() throws Exception {
    officeManager.stop();
  }
}
```

### REST controller

```java
// src/main/java/com/example/doc2html/api/ConvertController.java
package com.example.doc2html.api;

import com.example.doc2html.service.ConversionService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/convert")
public class ConvertController {

  private final ConversionService service;

  public ConvertController(ConversionService service) {
    this.service = service;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<byte[]> convert(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "purpose") @NotBlank String purpose // "inline" or "download"
  ) throws Exception {

    byte[] html = service.convertToHtml(file.getBytes(), file.getOriginalFilename());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);
    String disp = "inline".equalsIgnoreCase(purpose) ? "inline" : "attachment";
    headers.set(HttpHeaders.CONTENT_DISPOSITION, disp + "; filename=\"" + baseName(file.getOriginalFilename()) + ".html\"");
    return ResponseEntity.ok().headers(headers).body(html);
  }

  private String baseName(String filename) {
    if (filename == null) return "document";
    int dot = filename.lastIndexOf('.');
    return dot > 0 ? filename.substring(0, dot) : filename;
  }
}
```

### Notes for production hardening
- **Asset handling:** LibreOffice may output an HTML plus a “_files” folder (images, CSS). For portability, post-process to inline assets as data URIs, or serve the asset folder at a temporary URL.
- **Fonts:** Install needed fonts on the server to improve fidelity. Consider fontconfig updates and embedding options.
- **Concurrency:** Start a pool of LibreOffice processes via `LocalOfficeManager.builder().officeHome(...).portNumbers(...)`.
- **Security:** Validate MIME and extension, limit max file size, sanitize output, strip active content (macros aren’t executed here).
- **Performance:** For large/complex files, consider async processing and caching.

---

## Option B: Commercial stack (Aspose) for maximum fidelity

Aspose libraries produce “fixed” HTML (absolutely positioned), preserving headers/footers, columns, shapes, charts, and page boundaries closely to the source. Licensing is required, but fidelity is outstanding, especially for enterprise needs.

### Dependencies
- Aspose.Words for Java (DOC/DOCX)
- Aspose.Cells for Java (XLS/XLSX)
- Aspose.Slides for Java (PPT/PPTX)
- Aspose.PDF for Java (PDF)

Add the vendor’s Maven repositories and dependencies per product. Example usage below assumes you’ve added them.

### Service implementation (sketch)

```java
// src/main/java/com/example/doc2html/service/AsposeConversionService.java
package com.example.doc2html.service;

import com.aspose.words.*;
import com.aspose.cells.*;
import com.aspose.slides.*;
import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.Document;
import org.apache.tika.Tika;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AsposeConversionService {

  private final Tika tika = new Tika();

  public byte[] convert(byte[] input, String filename) throws Exception {
    String mime = tika.detect(input);
    if (mime == null && filename != null) mime = tika.detect(filename);

    if (mime == null) throw new IllegalArgumentException("Unknown file type");

    if (mime.equals("application/pdf")) {
      return convertPdf(input);
    }
    if (mime.contains("wordprocessingml") || mime.equals("application/msword")) {
      return convertWord(input);
    }
    if (mime.contains("spreadsheetml") || mime.equals("application/vnd.ms-excel")) {
      return convertExcel(input);
    }
    if (mime.contains("presentationml") || mime.equals("application/vnd.ms-powerpoint")) {
      return convertPowerPoint(input);
    }
    throw new IllegalArgumentException("Unsupported type: " + mime);
  }

  private byte[] convertWord(byte[] input) throws Exception {
    LoadOptions lo = new LoadOptions();
    com.aspose.words.Document doc = new com.aspose.words.Document(new ByteArrayInputStream(input), lo);
    HtmlFixedSaveOptions opt = new HtmlFixedSaveOptions();
    opt.setExportEmbeddedCss(true);
    opt.setExportEmbeddedFonts(true);
    opt.setExportEmbeddedImages(true);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    doc.save(out, opt);
    return out.toByteArray();
  }

  private byte[] convertExcel(byte[] input) throws Exception {
    Workbook wb = new Workbook(new ByteArrayInputStream(input));
    HtmlSaveOptions opt = new HtmlSaveOptions(SaveFormat.HTML);
    opt.setExportActiveWorksheetOnly(false);
    opt.setExportGridLines(true);
    opt.setPresentationPreference(true);
    opt.setEncoding(com.aspose.cells.Encoding.getUTF8());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    wb.save(out, opt);
    return out.toByteArray();
  }

  private byte[] convertPowerPoint(byte[] input) throws Exception {
    Presentation pres = new Presentation(new ByteArrayInputStream(input));
    com.aspose.slides.HtmlOptions opt = new com.aspose.slides.HtmlOptions();
    opt.getEmbedAllFontsHtmlController().setEmbedFonts(true);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    pres.save(out, com.aspose.slides.SaveFormat.Html, opt);
    return out.toByteArray();
  }

  private byte[] convertPdf(byte[] input) throws Exception {
    Document pdf = new Document(new ByteArrayInputStream(input));
    HtmlSaveOptions opt = new HtmlSaveOptions();
    opt.setFixedLayout(true); // maintain page layout
    opt.setSplitIntoPages(true);
    opt.setEmbedFonts(true);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    pdf.save(out, opt);
    return out.toByteArray();
  }
}
```

Use the same `ConvertController` from Option A to return the HTML inline or as a download. For Word files, `HtmlFixedSaveOptions` keeps pagination and headers/footers; for PDF, `FixedLayout + splitIntoPages` yields page-separated HTML. Assets can be embedded to produce a single self-contained HTML file.

---

## Frontend usage

- Upload via a form to POST /api/convert with fields:
  - file: the chosen document
  - purpose: “inline” to preview in-browser, “download” to force save
- For inline display, you can:
  - Open the response in a new tab by setting the form’s target.
  - Or fetch via XHR and inject into an iframe or a sandboxed container.

---

## Practical fidelity tips

- Install the same fonts used in documents on the server; for Hebrew or RTL content, install full Unicode fonts and enable complex text layout support.
- Keep conversions deterministic by pinning the LibreOffice version across environments.
- For spreadsheets with multiple sheets, decide whether to export all sheets or only active ones; add switches accordingly.
- If you need selectable text and exact geometry, prefer “fixed HTML” outputs or pdf2htmlEX with embedded fonts.

---

## Quick start checklist

- Choose stack:
  - Open-source (LibreOffice + pdf2htmlEX): zero licensing, great quality, more ops work.
  - Aspose: license cost, highest fidelity, simplest code path.
- Wire the service into the provided Spring controller.
- Handle assets (embed or serve alongside).
- Add file-size limits, MIME validation, and logging.
- Containerize with fonts installed; test with your real documents.

If you tell me your preference (open-source vs. commercial), deployment OS, and expected file sizes, I’ll tailor the exact configuration and Dockerfile for you.


-----



