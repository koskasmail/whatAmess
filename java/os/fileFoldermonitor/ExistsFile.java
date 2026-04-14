package utilities;

import java.io.File;

public class ExistsFile {

	public ExistsFile() {
		
	}

	public boolean checkIfFileExists(String fileName) {
		File f = new File(fileName);
		
		if (f.exists()) {
			System.out.println("Exists");
			return true;
		}
		else {
		    System.out.println("Does not Exists");
		    return false;
		}
			
	}
	
	public static void main (String [] args) {
		ExistsFile ck = new ExistsFile();
		ck.checkIfFileExists("c:\\temp\\1.md");
	}
} //class
