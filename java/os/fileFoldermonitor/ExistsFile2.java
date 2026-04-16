package your.package.name;

import java.io.File;

public class ExistsFile2 {

    private String fileName;

    public ExistsFile2() {
    }

    public ExistsFile2(String fileName) {
        this.fileName = fileName;
    }

    // Getter
    public String getFileName() {
        return fileName;
    }

    // Setter
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // Check if file exists
    public boolean checkIfFileExists() {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("File name is empty");
            return false;
        }

        File f = new File(fileName);

        if (f.exists()) {
            System.out.println("Exists");
            return true;
        } else {
            System.out.println("Does not exist");
            return false;
        }
    }

    // Optional: static helper method
    public static boolean check(String fileName) {
        return new File(fileName).exists();
    }

    public static void main(String[] args) {
        ExistsFile2 ck = new ExistsFile2();
        ck.setFileName("c:\\temp\\1.md");
        ck.checkIfFileExists();
    }
}
