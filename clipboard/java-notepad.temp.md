<a name="topage"></a>

# java-notepad.temp

### ...

```
import java.util.*;
import java.io.*;
class Notepad {
	public static void main(String[] args) {
		Runtime rs = Runtime.getRuntime();
		try {
			rs.exec("notepad");
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
```

### ...
```
ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "myfile.txt");
pb.start();
```

### ...

```
if (Desktop.isDesktopSupported()) {
    Desktop.getDesktop().edit(file);
} else {
    // I don't know, up to you to handle this
}
```

### ...

```
Runtime runtime = Runtime.getRuntime();
Process process = runtime.exec("C:\\path\\to\\notepad.exe C:\\path\\to\\file.txt");
```

### ...

```
package interestingJavaprograms;
import java.io.IOException;
 
public class NotepadJava {
 
    /**
     * @ www.instanceofjava.com
     * @ how to open a new notepad using java program
     */
public static void main(String[] args) {
       
          Runtime rt = Runtime.getRuntime();
          
try {
   runTime.exec("C:\\Windows\\System32\\notepad.exe E:\\Samplenotepad.txt");
}
 catch (IOException ex) {
 
 System.out.println(ex);
 
}  
 
}
 
}
```

### ...

```
package interestingJavaprograms;
import java.io.IOException;
 
public class NotepadJava {
 
    /**
     * @ www.instanceofjava.com
     * @ how to open a new notepad using java program
     */
public static void main(String[] args) throws InterruptedException, IOException {
        
Runtime runTime = Runtime.getRuntime();
System.out.println("Opening notepad");
Process process = runTime.exec("notepad");
          
try {
 
 
Thread.sleep(200); 

 process.destroy();
 System.out.println("Closing notepad");
 
}
 catch (Exception ex) {
 
 System.out.println(ex);
 
}  
 
}
 
}
```

### ... 

```
  try{
       Runtime.getRuntime().exec("notepad");
       Robot robot = new Robot();

       robot.delay(100);
       robot.keyPress(KeyEvent.VK_A);
       robot.keyPress(KeyEvent.VK_B);
       robot.keyPress(KeyEvent.VK_C);
      }catch(Exception ex) {}
    }
```

### ...

```
-------------------------------------------------------------------
https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
-------------------------------------------------------------------
https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
-------------------------------------------------------------------
http://www.beginwithjava.com/java/file-input-output/writing-text-file.html
-------------------------------------------------------------------
```

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
