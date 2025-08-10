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
