Sure thing! Here's a simple Java program that detects the MIME type from the content of a file without using the Apache Tika library. Instead, it uses a file signature (also known as magic number) approach to identify common file types:

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MimeTypeDetector {
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("FFD8FF", "image/jpeg");
        MIME_TYPES.put("89504E47", "image/png");
        MIME_TYPES.put("47494638", "image/gif");
        MIME_TYPES.put("25504446", "application/pdf");
        MIME_TYPES.put("504B0304", "application/zip");
        // Add more MIME types here as needed
    }

    public static String getMimeType(File file) throws IOException {
        String magicNumber = getMagicNumber(file);
        return MIME_TYPES.getOrDefault(magicNumber, "application/octet-stream");
    }

    private static String getMagicNumber(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[4];
            if (fis.read(bytes) != 4) {
                throw new IOException("Could not read file header");
            }
            return bytesToHex(bytes);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MimeTypeDetector <file-path>");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getPath());
            return;
        }

        try {
            String mimeType = getMimeType(file);
            System.out.println("MIME type: " + mimeType);
        } catch (IOException e) {
            System.err.println("Error detecting MIME type: " + e.getMessage());
        }
    }
}
```

This program reads the first few bytes of the file and compares them against a predefined list of magic numbers to determine the file's MIME type. You can expand the `MIME_TYPES` map to include more file types as needed. Feel free to customize and enhance it according to your needs!

What other type of programs or projects are you working on?
