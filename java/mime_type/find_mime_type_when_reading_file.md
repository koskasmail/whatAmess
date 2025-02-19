```java
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MimeTypeDetector {

    public static String detectMimeType(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.probeContentType(path);
    }

    public static String detectMimeTypeUsingInputStream(String filePath) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] header = new byte[1024]; // Read a chunk of the file
            int bytesRead = bis.read(header);
            if (bytesRead == -1) {
                return "application/octet-stream"; // Empty file
            }

            // Simple magic number based detection (very limited).
            // For robust detection, use a library like Apache Tika.
            if (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8) {
                return "image/jpeg";
            } else if (header[0] == (byte) 0x89 && header[1] == (byte) 0x50 && header[2] == (byte) 0x4E
                    && header[3] == (byte) 0x47) {
                return "image/png";
            } else if (header[0] == (byte) 0x47 && header[1] == (byte) 0x49 && header[2] == (byte) 0x46) {
                return "image/gif";
            } else if (header[0] == (byte) 0x25 && header[1] == (byte) 0x50 && header[2] == (byte) 0x44
                    && header[3] == (byte) 0x46) {
                return "application/pdf";
            } else if (header[0] == (byte) 0x50 && header[1] == (byte) 0x4B && header[2] == (byte) 0x03
                    && header[3] == (byte) 0x04) {
                return "application/zip"; // Or more specific zip types
            } else {
                // Fallback to octet-stream or try more advanced detection
                return "application/octet-stream";
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "path/to/your/file.txt"; // Replace with your file path

        try {
            String mimeType = detectMimeType(filePath);
            System.out.println("MIME type (Files.probeContentType): " + mimeType);

            String mimeTypeInputStream = detectMimeTypeUsingInputStream(filePath);
            System.out.println("MIME type (InputStream - basic detection): " + mimeTypeInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

**Explanation and Improvements:**

1.  **`Files.probeContentType()` (Recommended):**
    * This is the most reliable and efficient way to determine a file's MIME type in modern Java.
    * It uses the operating system's built-in MIME type detection mechanisms, which are generally accurate and up-to-date.
    * It is the first method in the example because it is the best method.
    * It handles many file types.

2.  **`detectMimeTypeUsingInputStream()` (Basic Magic Numbers):**
    * This method demonstrates a basic approach using "magic numbers" (the first few bytes of a file that identify its type).
    * It's included for educational purposes but is *not* a robust solution.
    * It only recognizes a limited set of file types.
    * It uses a `BufferedInputStream` for efficiency.
    * It includes a default return of "application/octet-stream".
    * It is important to note that this method is not comprehensive and should not be used in production code.

3.  **Error Handling:**
    * The code includes `try-catch` blocks to handle potential `IOExceptions`.
    * It's good practice to handle file-related exceptions gracefully.

4.  **Resource Management:**
    * The `try-with-resources` statement is used in `detectMimeTypeUsingInputStream()` to ensure that the `BufferedInputStream` is closed automatically, even if an exception occurs.

5.  **Robust MIME Type Detection (Apache Tika):**
    * For production-level applications, use Apache Tika. It's a powerful and widely used library for MIME type detection and content extraction.
    * To use it, add the Apache Tika dependency to your project (e.g., in Maven or Gradle).
    * Example using Tika:

```java
import org.apache.tika.Tika;

// ... inside your class

public static String detectMimeTypeTika(String filePath) throws IOException {
    Tika tika = new Tika();
    return tika.detect(Paths.get(filePath));
}
```

**How to Compile and Run:**

1.  **Save:** Save the code as `MimeTypeDetector.java`.
2.  **Compile:** `javac MimeTypeDetector.java`
3.  **Run:** `java MimeTypeDetector` (Replace `"path/to/your/file.txt"` with the actual path to your file).
4. **Apache Tika:** If you want to use the Apache Tika portion of the code, you will need to add the dependency to your project. If using maven add the following to your pom.xml file within the dependencies tag.

```xml
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>2.9.1</version> </dependency>
```
Remember to replace `"path/to/your/file.txt"` with the actual path to your file.
