# file_monitor


#### #1

Here’s a clean, production‑ready Java example that does exactly what you described:

- Monitors a **network path** for the latest updated file  
- Copies that file to a **local directory**  
- Reads a **JSON config** at startup to load keywords/templates  
- Continues monitoring the network path for new updates  
- Scans each copied file for the configured keywords  

I’ll give you a full working example with structure, classes, and explanations so you can adapt it to your environment.

---

# ✅ High‑Level Architecture

### Components
| Component | Responsibility |
|----------|----------------|
| `ConfigLoader` | Loads keywords/templates from a JSON file at startup |
| `DirectoryMonitor` | Watches a network directory for new/updated files |
| `FileProcessor` | Copies the file locally and scans for keywords |
| `Main` | Wires everything together and runs the loop |

---

# 📁 Example Project Structure

```
src/
 ├── Main.java
 ├── config.json
 ├── ConfigLoader.java
 ├── DirectoryMonitor.java
 └── FileProcessor.java
```

---

# 📌 Example `config.json`

```json
{
  "keywords": [
    "ERROR",
    "CRITICAL",
    "Template123"
  ]
}
```

---

# 🧩 Java Implementation

## **ConfigLoader.java**
Loads keywords from JSON using Jackson.

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    public static List<String> loadKeywords(String jsonPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonPath));

        List<String> keywords = new ArrayList<>();
        for (JsonNode node : root.get("keywords")) {
            keywords.add(node.asText());
        }
        return keywords;
    }
}
```

---

## **DirectoryMonitor.java**
Monitors a directory and returns the latest modified file.

```java
import java.io.File;

public class DirectoryMonitor {

    private final File directory;

    public DirectoryMonitor(String path) {
        this.directory = new File(path);
    }

    public File getLatestFile() {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) return null;

        File latest = files[0];
        for (File f : files) {
            if (f.lastModified() > latest.lastModified()) {
                latest = f;
            }
        }
        return latest;
    }
}
```

---

## **FileProcessor.java**
Copies the file and scans for keywords.

```java
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileProcessor {

    private final List<String> keywords;

    public FileProcessor(List<String> keywords) {
        this.keywords = keywords;
    }

    public File copyFile(File source, String localDir) throws IOException {
        File dest = new File(localDir, source.getName());
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return dest;
    }

    public void scanFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;

            while ((line = reader.readLine()) != null) {
                for (String keyword : keywords) {
                    if (line.contains(keyword)) {
                        System.out.println("Match found: " + keyword +
                                " in file " + file.getName() +
                                " at line " + lineNum);
                    }
                }
                lineNum++;
            }
        }
    }
}
```

---

## **Main.java**
Main loop: loads config, monitors directory, processes new files.

```java
import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        String networkPath = "\\\\SERVER\\SharedFolder";  // network UNC path
        String localPath = "C:\\LocalCopy";
        String configPath = "config.json";

        List<String> keywords = ConfigLoader.loadKeywords(configPath);
        DirectoryMonitor monitor = new DirectoryMonitor(networkPath);
        FileProcessor processor = new FileProcessor(keywords);

        File lastProcessed = null;

        System.out.println("Monitoring started...");

        while (true) {
            File latest = monitor.getLatestFile();

            if (latest != null && !latest.equals(lastProcessed)) {
                System.out.println("New file detected: " + latest.getName());

                File copied = processor.copyFile(latest, localPath);
                processor.scanFile(copied);

                lastProcessed = latest;
            }

            Thread.sleep(3000); // poll every 3 seconds
        }
    }
}
```

---

# 🚀 How It Works

### 1. On startup:
- Reads `config.json`
- Loads keywords into memory

### 2. Every 3 seconds:
- Checks the network directory for the newest file
- If it’s new:
  - Copies it to the local directory
  - Reads it line‑by‑line
  - Prints any keyword matches

---

# 🧠 Want enhancements?

I can extend this with:

- Real-time file system events (WatchService)
- Logging to a database
- Multi-threaded processing
- Email/SMS alerts when keywords are found
- Regex template matching instead of simple keywords
- Tracking processed files in a SQLite DB

Just tell me what direction you want to take it.

-----

#### #2

You’re building something closer to a **real‑time log tailer + pattern detector + Markdown reporter**, not a simple “copy latest file” script.  
Let’s engineer this properly so it works **continuously**, even when the log file is updated every second.

Below is a **clean, scalable Java solution** that:

---

# ✅ What This Version Does

### ✔ Continuously monitors a network directory  
- Always finds the **most recently modified log file**  
- Detects when that file is still being updated (tailing behavior)

### ✔ Reads a JSON config at startup  
- Loads **keywords / regex templates**

### ✔ Scans the log file in real time  
- Reads **new lines only** (like `tail -f`)  
- For each match:
  - Creates/updates a Markdown file  
  - Filename format: `yyyy-MM-ddTHHmmssSSS.md`  
  - Adds:
    - The keyword/template found  
    - The matching line  
    - The line immediately **after** it  
    - An emoji (you can customize)

---

# 📁 JSON Example (`config.json`)

```json
{
  "patterns": [
    "ERROR",
    "CRITICAL",
    "User .* logged in"
  ],
  "emoji": "⚡"
}
```

---

# 🧠 Key Design Choices

### **1. We tail the file instead of re-reading it**
We track:
- last file processed  
- last byte offset read  

### **2. We detect new lines as they appear**
Using `RandomAccessFile` for efficient tailing.

### **3. We generate a Markdown file per match**
Filename format:

```
2025-01-12T153045123.md
```

---

# 🧩 Full Java Implementation

## **ConfigLoader.java**

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    public static class Config {
        public List<String> patterns;
        public String emoji;
    }

    public static Config load(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(path));

        Config cfg = new Config();
        cfg.patterns = new ArrayList<>();
        for (JsonNode n : root.get("patterns")) {
            cfg.patterns.add(n.asText());
        }
        cfg.emoji = root.get("emoji").asText();

        return cfg;
    }
}
```

---

## **DirectoryMonitor.java**

```java
import java.io.File;

public class DirectoryMonitor {

    private final File dir;

    public DirectoryMonitor(String path) {
        this.dir = new File(path);
    }

    public File getMostRecentFile() {
        File[] files = dir.listFiles((d, name) -> name.endsWith(".log"));
        if (files == null || files.length == 0) return null;

        File latest = files[0];
        for (File f : files) {
            if (f.lastModified() > latest.lastModified()) {
                latest = f;
            }
        }
        return latest;
    }
}
```

---

## **MarkdownWriter.java**

```java
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarkdownWriter {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmssSSS");

    public static void writeMatch(String emoji, String pattern, String line, String nextLine) throws IOException {
        String fileName = FMT.format(LocalDateTime.now()) + ".md";

        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write("# Match Found " + emoji + "\n");
            fw.write("**Pattern:** `" + pattern + "`\n\n");
            fw.write("**Line:**\n```\n" + line + "\n```\n\n");
            fw.write("**Next Line:**\n```\n" + nextLine + "\n```\n\n");
            fw.write("---\n\n");
        }
    }
}
```

---

## **LogTailer.java**

```java
import java.io.*;
import java.util.List;

public class LogTailer {

    private long lastPosition = 0;
    private File currentFile;

    private final List<String> patterns;
    private final String emoji;

    public LogTailer(List<String> patterns, String emoji) {
        this.patterns = patterns;
        this.emoji = emoji;
    }

    public void switchToFile(File f) {
        currentFile = f;
        lastPosition = 0; // restart reading
    }

    public void process() throws Exception {
        if (currentFile == null) return;

        try (RandomAccessFile raf = new RandomAccessFile(currentFile, "r")) {
            raf.seek(lastPosition);

            String line;
            String nextLine;

            while ((line = raf.readLine()) != null) {
                long beforeNext = raf.getFilePointer();
                nextLine = raf.readLine();
                if (nextLine == null) nextLine = "";

                for (String p : patterns) {
                    if (line.matches(".*" + p + ".*")) {
                        MarkdownWriter.writeMatch(emoji, p, line, nextLine);
                    }
                }

                raf.seek(beforeNext);
            }

            lastPosition = raf.getFilePointer();
        }
    }
}
```

---

## **Main.java**

```java
public class Main {

    public static void main(String[] args) throws Exception {

        String networkPath = "\\\\SERVER\\Logs";
        String configPath = "config.json";

        ConfigLoader.Config cfg = ConfigLoader.load(configPath);

        DirectoryMonitor monitor = new DirectoryMonitor(networkPath);
        LogTailer tailer = new LogTailer(cfg.patterns, cfg.emoji);

        File lastFile = null;

        System.out.println("Real-time log monitoring started...");

        while (true) {
            File newest = monitor.getMostRecentFile();

            if (newest != null && !newest.equals(lastFile)) {
                System.out.println("Switching to new log file: " + newest.getName());
                tailer.switchToFile(newest);
                lastFile = newest;
            }

            tailer.process();
            Thread.sleep(500); // check twice per second
        }
    }
}
```

---

# 🚀 What This Gives You

### ✔ Real-time monitoring  
### ✔ Pattern detection  
### ✔ Markdown reporting  
### ✔ Handles constantly growing log files  
### ✔ Automatically switches to the newest log file  
### ✔ Reads JSON config for patterns + emoji  

---

# Want to enhance it?

I can add:

- Regex groups extraction  
- Multi-threaded processing  
- A rolling Markdown file per day  
- A SQLite DB for storing matches  
- A web dashboard  
- A REST API  
- A Windows service wrapper  

Just tell me where you want to take it next.


------

#### #3



------



------



------


------

------

------

------
