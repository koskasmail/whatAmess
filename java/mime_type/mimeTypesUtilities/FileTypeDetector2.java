package mimeTypesUtilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeDetector2 {

    // Mapping of magic numbers to file types
    private static final Map<String, String> FILE_SIGNATURES = new HashMap<>();

    static {
        FILE_SIGNATURES.put("FFD8FF", "JPEG Image");
        FILE_SIGNATURES.put("89504E47", "PNG Image");
        FILE_SIGNATURES.put("47494638", "GIF Image");
        FILE_SIGNATURES.put("25504446", "PDF Document");
        FILE_SIGNATURES.put("504B0304", "ZIP Archive");
        FILE_SIGNATURES.put("4D5A", "Executable File"); // Example: Windows .exe

        // Additional file types
        FILE_SIGNATURES.put("3C68746D", "HTML File");       // HTML
        FILE_SIGNATURES.put("3C21444F", "MHT File");        // MHT (HTML archive)
        FILE_SIGNATURES.put("D0CF11E0", "DOC File");        // DOC (Microsoft Word older format)
        FILE_SIGNATURES.put("504B0304", "DOCX File");       // DOCX (Microsoft Word XML-based)
        FILE_SIGNATURES.put("504B0304", "XLSX File");       // XLSX (Microsoft Excel XML-based)
        FILE_SIGNATURES.put("D0CF11E0", "XLS File");        // XLS (Microsoft Excel older format)
        FILE_SIGNATURES.put("D0CF11E0", "PPT File");        // PPT (Microsoft PowerPoint older format)
        FILE_SIGNATURES.put("504B0304", "PPTS File");       // PPTX (Microsoft PowerPoint XML-based)
        FILE_SIGNATURES.put("EFBBBF", "TXT File");          // TXT (UTF-8 with BOM)
        FILE_SIGNATURES.put("3C737667", "SVG File");        // SVG (Scalable Vector Graphics)
    
    }

    public static String detectFileType(File file) throws IOException {
        // Get a sample (magic number) from the file
        String magicNumber = getMagicNumber(file);

        // Match the sample to a known file type
        return FILE_SIGNATURES.getOrDefault(magicNumber, "Unknown File Type");
    }

    private static String getMagicNumber(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] sampleBytes = new byte[4]; // Read the first 4 bytes (or more if needed)
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

    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java FileTypeDetector <file-path>");
//            return;
//        }

    	String path = "/home/yaron/tests/";
    	String file1 = path + "101_pdf.txt";
    	String file2 = path + "102_html.html.txt";
    	String file3 = path + "102_mht.txt";
    	String file4 = path + "103_document.doc.txt";
    	String file5 = path + "104_Docx_.docx.txt";
    	String file6 = path + "105_XLS_.xls.txt";
    	String file7 = path + "122_PPS_.pps.txt";
    	
        File file = new File(file1);
        if (!file.exists() || !file.isFile()) {
            System.out.println("The provided path is not a valid file: " + file.getAbsolutePath());
            return;
        }

        try {
            String fileType = detectFileType(file);
            System.out.println("File type: " + fileType);
        } catch (IOException e) {
            System.err.println("Error detecting file type: " + e.getMessage());
        }
    }
}
