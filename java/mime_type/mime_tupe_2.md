```java
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.activation.MimetypesFileTypeMap;

public class FileTypeInfo {

    public static void main(String[] args) {
        String filePath = "path/to/your/file.txt"; // Replace with your file path

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        // Get MIME type
        String mimeType = getMimeType(file);
        System.out.println("MIME Type: " + mimeType);

        // Get extension
        String extension = getFileExtension(file);
        System.out.println("File Extension: " + extension);

        // More robust mime type detection using java.nio.file.Files
        try {
            Path path = Paths.get(filePath);
            String robustMimeType = Files.probeContentType(path);
            System.out.println("Robust MIME Type: " + robustMimeType);

             //Get file size and creation time as example of using BasicFileAttributes.
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("File Size: " + attributes.size() + " bytes");
            System.out.println("Creation Time: " + attributes.creationTime());

        } catch (IOException e) {
            System.err.println("Error probing MIME type: " + e.getMessage());
        }



    }

    public static String getMimeType(File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        return mimeTypesMap.getContentType(file);
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return ""; // No extension found
        }
    }
}
```

**Key improvements and explanations:**

1.  **Error Handling:**
    * The code now checks if the file exists before attempting to process it, preventing `FileNotFoundException` errors.
    * It includes a `try-catch` block around the `Files.probeContentType()` call to handle potential `IOExceptions` during MIME type detection.
2.  **Robust MIME Type Detection:**
    * It uses `java.nio.file.Files.probeContentType()` which is generally more reliable and accurate than `MimetypesFileTypeMap` for modern file types.
3.  **File Extension Extraction:**
    * The `getFileExtension()` method now handles cases where a file might not have an extension or may have hidden files with a leading dot.
4.  **BasicFileAttributes Example:**
    * Added an example of using `BasicFileAttributes` to retrieve file size and creation time. This shows how to get more file metadata.
5.  **Clearer Output:**
    * The output is now labeled more clearly, indicating which MIME type detection method was used.
6.  **Path object usage:**
    * The code now uses `Path` objects, which are the modern way to represent file paths in Java.
7. **Complete and runnable:** The code is a complete and runnable example. Just change the filepath.

**How to use:**

1.  **Replace `"path/to/your/file.txt"`:** Change this placeholder to the actual path of the file you want to analyze.
2.  **Compile and run:** Compile the Java code and run it. The output will display the MIME type and file extension.

**Dependencies:**

* This code uses standard Java libraries, so no external dependencies are required.
