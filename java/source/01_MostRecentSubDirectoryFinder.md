<a name="topage"></a>

# 01_MostRecentSubDirectoryFinder.md

### source

```
package tests;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

public class MostRecentSubDirectoryFinder {

    public MostRecentSubDirectoryFinder() {
    	
        Path startPath = Paths.get("/home/yaron");

        try {
            Path latestDir = findMostRecentSubDirectory(startPath);

            if (latestDir != null) {
                System.out.println("Most Recent Subdirectory: " + latestDir);
                BasicFileAttributes attrs = Files.readAttributes(latestDir, BasicFileAttributes.class);

                LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                System.out.println("Last Modified Time: " + lastModifiedTime);

                try (Stream<Path> files = Files.list(latestDir)) {
                    long totalFiles = files.count();
                    System.out.println("Total Files: " + totalFiles);
                }

                try (Stream<Path> files = Files.list(latestDir)) {
                    System.out.println("Files Info:");
                    files.forEach(file -> {
                        try {
                            BasicFileAttributes fileAttrs = Files.readAttributes(file, BasicFileAttributes.class);
                            System.out.println("File: " + file.getFileName());
                            System.out.println("    Size: " + fileAttrs.size() + " bytes");
                            System.out.println("    Created: " + LocalDateTime.ofInstant(fileAttrs.creationTime().toInstant(), ZoneId.systemDefault()));
                            System.out.println("    Last Modified: " + LocalDateTime.ofInstant(fileAttrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
                        } catch (IOException e) {                       
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                System.out.println("No subdirectories found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path findMostRecentSubDirectory(Path startPath) throws IOException {
        try (Stream<Path> subPaths = Files.walk(startPath, 1)) {
            return subPaths
                    .filter(Files::isDirectory)
                    .filter(path -> !path.equals(startPath))
                    .max((path1, path2) -> {
                        try {
                            return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(null);
        }
    }
}
```

### pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.koskasmail.sample</groupId>
  <artifactId>sample</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>sample</name>
  <description>test zone</description>
  <dependencies>
  	<dependency>
  		<groupId>com.google.code.gson</groupId>
  		<artifactId>gson</artifactId>
  		<version>2.10.1</version>
  	</dependency>
  </dependencies>
</project>
```


----

<p align="right">(<a href="#topage">back to top</a>)</p>

<br/>
<br/>
