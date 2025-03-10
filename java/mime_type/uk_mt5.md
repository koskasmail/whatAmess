Here’s a different way to detect file types in Java by reading the magic numbers from the file without hardcoding file signatures in the code. Instead, you can use a **configuration file** containing a list of magic numbers and corresponding file types. This makes the program easier to maintain and expand.

---

### Java Program: Dynamic Configuration for Magic Numbers

#### Configuration File (`filetypes.config`)
Create a configuration file (`filetypes.config`) that contains magic numbers and corresponding file types. Each entry is in the format: `MAGIC_NUMBER=FILE_TYPE`.

Example content:
```
FFD8FF=JPEG Image
89504E47=PNG Image
47494638=GIF Image
25504446=PDF Document
504B0304=ZIP Archive
3C68746D=HTML File
3C737667=SVG File
49492A00=TIFF Image
424D=BMP Image
```

You can add additional file types here as needed.

---

#### Java Code

```java
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigurableFileTypeDetector {

    private static final Map<String, String> FILE_SIGNATURES = new HashMap<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ConfigurableFileTypeDetector <file-path>");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.out.println("The provided path is not a valid file: " + file.getAbsolutePath());
            return;
        }

        try {
            loadFileSignatures("filetypes.config");
            String fileType = detectFileType(file);
            System.out.println("Detected file type: " + fileType);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void loadFileSignatures(String configPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines or comments
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    FILE_SIGNATURES.put(parts[0], parts[1]);
                }
            }
        }
    }

    private static String detectFileType(File file) throws IOException {
        String magicNumber = getMagicNumber(file);

        // Match the magic number against the loaded signatures
        return FILE_SIGNATURES.getOrDefault(magicNumber, "Unknown File Type");
    }

    private static String getMagicNumber(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] sampleBytes = new byte[4]; // Read the first 4 bytes
            if (fis.read(sampleBytes) != 4) {
                throw new IOException("Unable to read enough bytes from the file.");
            }
            return bytesToHex(sampleBytes);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
```

---

### Explanation of How It Works:
1. **Configuration File**:
   - Stores magic numbers and file types in a separate file (`filetypes.config`).
   - Makes the program easier to maintain by avoiding hardcoding magic numbers in the Java code.

2. **Dynamic Loading**:
   - The program reads the configuration file at runtime and populates the `FILE_SIGNATURES` map with the entries.

3. **File Type Detection**:
   - Reads the first 4 bytes of the file and converts them into a hexadecimal string.
   - Matches the magic number against the entries in `FILE_SIGNATURES`.

---

### How to Use:
1. Create a `filetypes.config` file with the required magic numbers and their corresponding file types.
2. Compile and run the program:
   ```bash
   javac ConfigurableFileTypeDetector.java
   java ConfigurableFileTypeDetector <file-path>
   ```
3. Provide the path to a valid file when running the program.

---

### Benefits:
- **Ease of Maintenance**: Magic numbers and file types can be updated without modifying the Java code.
- **Scalable**: Add new file types by simply appending to the configuration file.
- **Flexible**: The program can handle a wide range of file types without growing too complex.

Let me know if you'd like to dive deeper into this approach or try a completely different method! 🚀
