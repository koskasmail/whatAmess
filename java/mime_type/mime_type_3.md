```java
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.Tika;

public class RealMimeType {

    public static void main(String[] args) {
        String filePath = "path/to/your/file"; // Replace with your file path

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        try {
            // Using Apache Tika for content-based MIME type detection
            Tika tika = new Tika();
            String mimeType = tika.detect(file);

            System.out.println("Real MIME Type (Tika): " + mimeType);

            //Alternative using java.nio.file.Files which also reads the content.
            Path path = Paths.get(filePath);
            String robustMimeType = Files.probeContentType(path);
            System.out.println("Robust MIME Type (nio.file.Files): " + robustMimeType);

            //Example using buffered input stream.
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                String mimeTypeStream = tika.detect(bis);
                System.out.println("Real MIME Type (Tika Stream): " + mimeTypeStream);
            }

        } catch (IOException e) {
            System.err.println("Error detecting MIME type: " + e.getMessage());
        }
    }
}
```

**Key changes and explanations:**

1.  **Apache Tika:**
    * This code uses the Apache Tika library, a powerful tool for content analysis and MIME type detection. It analyzes the file's content to determine its actual type, regardless of the file extension.
    * **Add Tika dependency:** You'll need to add the Apache Tika dependency to your project. If you're using Maven, add this to your `pom.xml`:

    ```xml
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-core</artifactId>
        <version>2.9.0</version> </dependency>
    ```

    If you are using gradle, add this to your `build.gradle` file.

    ```gradle
    implementation 'org.apache.tika:tika-core:2.9.0' //or latest version
    ```

2.  **`Tika.detect(File file)`:**
    * The `Tika.detect(File file)` method reads the file's content and returns its MIME type.
3.  **`Tika.detect(InputStream stream)`:**
    * Added example using an input stream. This is useful when you have a stream of data instead of a physical file.
4.  **`Files.probeContentType(Path path)`:**
    * This method is also very good at guessing the mime type based on the file's content.
5.  **No Extension Reliance:**
    * The code no longer relies on the file extension. It focuses solely on the file's content.
6.  **Error Handling:**
    * Kept the error handling from the previous example, which is very important.

**How to use:**

1.  **Add Apache Tika dependency:** Make sure you add the Apache Tika dependency to your project.
2.  **Replace `"path/to/your/file"`:** Change this placeholder to the actual path of your file.
3.  **Compile and run:** Compile and run the Java code. The output will display the real MIME type of the file, as determined by its content.
