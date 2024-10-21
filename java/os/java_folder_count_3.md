<a name="topage"></a>

```java
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInfo {
    public static void main(String[] args) {
        String[] paths = {"path1", "path2", "path3", "path4", "path5"};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String currentDate = dateFormat.format(new Date());

        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                long totalSize = getTotalSize(file);
                int totalFiles = countFiles(file);

                System.out.println("Current Date: " + currentDate);
                System.out.println("Current Hour: " + dateFormat.format(new Date()).substring(11));
                System.out.println("Path: " + path);
                System.out.println("Total Files: " + totalFiles);
                System.out.println("Total Size (MB): " + totalSize / (1024 * 1024));
                System.out.println();
            } else {
                System.out.println("Path " + path + " does not exist.");
            }
        }
    }

    private static long getTotalSize(File file) {
        if (file.isFile()) {
            return file.length();
        } else if (file.isDirectory()) {
            long totalSize = 0;
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    totalSize += getTotalSize(f);
                }
            }
            return totalSize;
        }
        return 0;
    }

    private static int countFiles(File file) {
        if (file.isFile()) {
            return 1;
        } else if (file.isDirectory()) {
            int totalFiles = 0;<a name="topage"></a>
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    totalFiles += countFiles(f);
                }
            }
            return totalFiles;
        }
        return 0;
    }
}
```

This code first defines a `String` array `paths` containing the five paths to be checked. It then creates a `SimpleDateFormat` object to format the current date and time.

The code iterates over each path in the array and checks if the corresponding file or directory exists. If it exists, it calculates the total size and number of files using the `getTotalSize` and `countFiles` methods, respectively. Finally, it prints the current date, current hour, path, total files, and total size in megabytes.

If a path does not exist, the code simply prints a message indicating that the path is invalid.


----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
