import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileTypeDetector {

    public static void main(String[] args) {
        // ... (main method remains the same) ...
    }

    private static byte[] getFileSignature(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] signature = new byte[16]; // Increased signature length
            int bytesRead = fis.read(signature);
            if (bytesRead <= 0) {
                return new byte[0]; // Empty file or read error
            }
            return signature;
        }
    }

    private static String detectMimeType(byte[] signature, String filePath) {
        Map<String, byte[]> signatures = new HashMap<>();

        // Expanded file signatures
        signatures.put("image/jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
        signatures.put("image/png", new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A});
        signatures.put("image/gif", new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38});
        signatures.put("application/pdf", new byte[]{(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46});
        signatures.put("application/zip", new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04});
        signatures.put("application/x-rar-compressed", new byte[]{(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x00});
        signatures.put("audio/mpeg", new byte[]{(byte) 0x49, (byte) 0x44, (byte) 0x33});
        signatures.put("audio/wav", new byte[]{(byte) 0x52, (byte) 0x49, (byte) 0x46, (byte) 0x46});
        signatures.put("video/mp4", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70, (byte)0x6d, (byte)0x70, (byte)0x34});
        signatures.put("application/msword", new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}); // .doc
        signatures.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x06, (byte) 0x00}); // .docx
        signatures.put("image/bmp", new byte[]{(byte) 0x42, (byte) 0x4D}); // .bmp
        signatures.put("image/tiff", new byte[]{(byte) 0x49, (byte) 0x49, (byte) 0x2A, (byte) 0x00}); // .tiff (little-endian)
        signatures.put("image/tiff", new byte[]{(byte) 0x4D, (byte) 0x4D, (byte) 0x00, (byte) 0x2A}); // .tiff (big-endian)
        signatures.put("application/vnd.ms-excel", new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}); // .xls
        signatures.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x06, (byte) 0x00}); // .xlsx
        signatures.put("application/vnd.ms-powerpoint", new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}); // .ppt
        signatures.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x06, (byte) 0x00}); // .pptx
        signatures.put("application/vnd.ms-powerpoint", new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}); // .pps
        signatures.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x06, (byte) 0x00}); // .ppsx
        signatures.put("image/vnd.dwg", new byte[]{(byte) 0x41, (byte) 0x43, (byte) 0x31, (byte) 0x30}); // .dwg
        signatures.put("application/vnd.ms-outlook", new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}); // .msg
        signatures.put("message/rfc822", new byte[]{(byte) 0x52, (byte) 0x65, (byte) 0x63, (byte) 0x65, (byte) 0x69, (byte) 0x76, (byte) 0x65, (byte) 0x64, (byte) 0x3A}); // .eml (very basic, more complex patterns exist)

        for (Map.Entry<String, byte[]> entry : signatures.entrySet()) {
            byte[] expectedSignature = entry.getValue();
            if (startsWith(signature, expectedSignature)) {
                return entry.getKey();
            }
        }
        return null;
    }
