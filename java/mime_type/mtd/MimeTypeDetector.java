package mtd;

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
//        if (args.length != 1) {
//            System.out.println("Usage: java MimeTypeDetector <file-path>");
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
    			
    	String stFileName = file7
    			; 

//    String folderPath = args[0];
    File folder = new File(path);

    if (!(folder.exists() && folder.isDirectory())) {
    	System.out.println("The folder does not exist or is not a directory.");
    } else {
    	System.out.println("The folder exists: " + folder.getAbsolutePath());
        
    }
    	
//    	File file = new File(args[0]);
    	File file = new File(stFileName);
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getPath());
            return;
        }
        else {
        	System.out.println("File exist: " + file.getPath());
        }

        try {
            String mimeType = getMimeType(file);
            System.out.println("MIME type: " + mimeType);
        } catch (IOException e) {
            System.err.println("Error detecting MIME type: " + e.getMessage());
        }
    }
}
