import java.util.Scanner;

public class FileExtensionFinder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Prompt the user to enter a file name
        System.out.print("Enter the file name: ");
        String fileName = scanner.nextLine();
        
        // Get the file extension
        String extension = getFileExtension(fileName);
        
        // Display the result
        if (extension.isEmpty()) {
            System.out.println("No extension found.");
        } else {
            System.out.println("File extension: " + extension);
        }
        
        scanner.close();
    }

    // Method to extract the file extension
    public static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1 || index == fileName.length() - 1) {
            return ""; // No extension found
        }
        return fileName.substring(index + 1);
    }
}
