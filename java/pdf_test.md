# pdf_test

## test #01
```
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

@SpringBootApplication
@RestController
public class PdfToHtmlService {

    public static void main(String[] args) {
        SpringApplication.run(PdfToHtmlService.class, args);
    }

    @GetMapping("/pdf-to-html")
    public String convertPdfToHtml(@RequestParam("pdfUrl") String pdfUrl) throws IOException {
        try (PDDocument document = PDDocument.load(new URL(pdfUrl))) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            // Convert text to HTML and CSS format
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "/* Your CSS styles here */\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div>\n" +
                    text +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

            return htmlContent;
        }
    }
}

```
