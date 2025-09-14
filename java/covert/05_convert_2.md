Absolutely! Here's a simplified version of your document-to-HTML converter using **Java 1.8**, triggered by a **Swing GUI** instead of a Spring service. This example focuses on converting a `.docx` or `.pdf` file to HTML when the user clicks a button in a basic Swing window.

---

## 🛠 Requirements

Before running this code, make sure:
- You have **LibreOffice** installed and accessible via system path.
- You have `pdf2htmlEX` installed for PDF conversion.
- You include the following libraries in your project:
  - `jodconverter-local`
  - `tika-core`

---

## 🖥️ Java 1.8 + Swing Example

```java
import org.apache.tika.Tika;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;

public class DocToHtmlApp {

    private static final Set<String> OFFICE_MIME = Set.of(
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private static final Tika tika = new Tika();
    private static LocalOfficeManager officeManager;
    private static DocumentConverter converter;

    public static void main(String[] args) throws Exception {
        officeManager = LocalOfficeManager.install();
        officeManager.start();
        converter = JodConverter.builder().officeManager(officeManager).build();

        SwingUtilities.invokeLater(DocToHtmlApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Doc to HTML Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);

        JButton convertButton = new JButton("Choose File and Convert");
        convertButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    byte[] input = Files.readAllBytes(file.toPath());
                    String mime = detectMime(input, file.getName());
                    byte[] html = convertToHtml(input, file.getName(), mime);
                    showHtml(html);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Conversion failed: " + ex.getMessage());
                }
            }
        });

        frame.getContentPane().add(convertButton, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static String detectMime(byte[] data, String filename) {
        try {
            String fromContent = tika.detect(data);
            if (fromContent != null) return fromContent;
        } catch (Exception ignored) {}
        return tika.detect(filename);
    }

    private static byte[] convertToHtml(byte[] input, String filename, String mime) throws Exception {
        if ("application/pdf".equals(mime)) {
            return convertPdfToHtml(input);
        } else if (OFFICE_MIME.contains(mime)) {
            return convertOfficeToHtml(input, filename);
        } else {
            throw new IllegalArgumentException("Unsupported MIME type: " + mime);
        }
    }

    private static byte[] convertOfficeToHtml(byte[] input, String filename) throws Exception {
        File in = Files.createTempFile("upload-", "-" + filename).toFile();
        File out = Files.createTempFile("out-", ".html").toFile();
        Files.write(in.toPath(), input);

        try {
            converter.convert(in).to(out).execute();
            String html = new String(Files.readAllBytes(out.toPath()), StandardCharsets.UTF_8);
            return html.getBytes(StandardCharsets.UTF_8);
        } finally {
            in.delete();
            out.delete();
        }
    }

    private static byte[] convertPdfToHtml(byte[] input) throws Exception {
        File in = Files.createTempFile("upload-", ".pdf").toFile();
        File out = Files.createTempFile("out-", ".html").toFile();
        Files.write(in.toPath(), input);

        ProcessBuilder pb = new ProcessBuilder(
            "pdf2htmlEX",
            "--embed", "cfijo",
            "--optimize-text", "1",
            "--zoom", "1.0",
            in.getAbsolutePath(),
            out.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while (br.readLine() != null) {}
        }
        int exit = p.waitFor();
        if (exit != 0) throw new RuntimeException("pdf2htmlEX failed with exit code " + exit);

        try {
            String html = new String(Files.readAllBytes(out.toPath()), StandardCharsets.UTF_8);
            return html.getBytes(StandardCharsets.UTF_8);
        } finally {
            in.delete();
            out.delete();
        }
    }

    private static void showHtml(byte[] htmlBytes) {
        String html = new String(htmlBytes, StandardCharsets.UTF_8);
        JTextArea area = new JTextArea(html);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(area);

        JFrame htmlFrame = new JFrame("Converted HTML");
        htmlFrame.setSize(600, 400);
        htmlFrame.add(scrollPane);
        htmlFrame.setVisible(true);
    }
}
```

---

## 🧪 What You Get

- A simple Swing UI with a button to choose a file.
- Automatic MIME detection using Tika.
- Conversion to HTML using LibreOffice or pdf2htmlEX.
- Display of the resulting HTML in a scrollable text area.

---

Want to add a preview pane with a real HTML renderer or save the output to disk? I can help you extend it further.
