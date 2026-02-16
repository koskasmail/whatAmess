
# fileMonitory.md

Here’s a clean, production‑ready Java example that does exactly what you described — and I’ll walk you through the logic so you can adapt it easily.

---

# ✅ **Goal**
1. Read a JSON file (`error.json`) containing fields:
   - `"Severity"`
   - `"Error Message"`
2. Find the **most recently updated log file** in a folder.
3. Scan that log file for any line containing the `"Error Message"` text.
4. For every match:
   - Copy the **entire line**, including date/time.
   - Include the **log file name**.
5. Write a **beautiful `output.md`** report.

---

# 📌 **Example `error.json`**
```json
{
  "Severity": "High",
  "Error Message": "Connection failed"
}
```

---

# 🧠 **Java Implementation**

Below is a complete Java program using:
- `java.nio.file.*` for file handling
- `com.fasterxml.jackson.databind.ObjectMapper` for JSON parsing

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LogScanner {

    public static void main(String[] args) throws Exception {

        Path folder = Paths.get("logs");          // folder containing log files
        Path jsonFile = Paths.get("error.json");  // JSON file with keywords
        Path outputFile = Paths.get("output.md"); // output markdown

        // 1. Read JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile.toFile());

        String severity = root.get("Severity").asText();
        String errorMessage = root.get("Error Message").asText();

        // 2. Select the most recently updated log file
        Path newestLog = Files.list(folder)
                .filter(Files::isRegularFile)
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()))
                .orElseThrow(() -> new RuntimeException("No log files found"));

        // 3. Read log file and find matching lines
        List<String> matchedLines = Files.lines(newestLog)
                .filter(line -> line.contains(errorMessage))
                .collect(Collectors.toList());

        // 4. Write beautiful Markdown output
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            writer.write("# 🔍 Error Report\n\n");
            writer.write("## Error Details\n");
            writer.write("- **Severity:** " + severity + "\n");
            writer.write("- **Error Message:** `" + errorMessage + "`\n");
            writer.write("- **Log File:** `" + newestLog.getFileName() + "`\n\n");

            writer.write("## 📄 Matching Log Entries\n");

            if (matchedLines.isEmpty()) {
                writer.write("> No matching entries found.\n");
            } else {
                for (String line : matchedLines) {
                    writer.write("### Match\n");
                    writer.write("**File:** `" + newestLog.getFileName() + "`\n\n");
                    writer.write("```\n" + line + "\n```\n\n");
                }
            }
        }

        System.out.println("output.md created successfully.");
    }
}
```

---

# ✨ **What the output looks like (`output.md`)**

```markdown
# 🔍 Error Report

## Error Details
- **Severity:** High
- **Error Message:** `Connection failed`
- **Log File:** `server-2026-02-16.log`

## 📄 Matching Log Entries
### Match
**File:** `server-2026-02-16.log`

```
2026-02-16 18:22:11 ERROR Connection failed for user admin
```
```

---

# Want enhancements?
I can add:

- Multiple log file scanning  
- Regex matching  
- Highlighting the error message in Markdown  
- Sorting results by timestamp  
- Packaging this as a reusable class or Spring Boot service  

Just tell me what direction you want to take this.

-----


Got it — you want to scan **every log file**, starting from the **newest**, and continue processing older files until no more files remain.  
Every match becomes a **new line** in the final `output.md`, and each line includes:

- The **file name**
- The **full log line** (with date/time)
- The **error message** that triggered the match

Let me give you a clean, robust Java solution that does exactly that.

---

# ✅ Updated Requirements (as I understand them)
### The program must:
- Read `error.json` → extract `"Error Message"` and `"Severity"`.
- List **all log files** in a folder.
- Sort them by **last modified date (newest → oldest)**.
- For each file:
  - Read all lines.
  - For each line containing the `"Error Message"`:
    - Add a new entry to `output.md`.
- Stop only when **all files have been processed**.
- Output a **beautiful Markdown report**.

---

# 🧠 Full Java Implementation

This version processes **all files**, not just the newest.

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LogScannerAllFiles {

    public static void main(String[] args) throws Exception {

        Path folder = Paths.get("logs");          // folder containing log files
        Path jsonFile = Paths.get("error.json");  // JSON file with keywords
        Path outputFile = Paths.get("output.md"); // output markdown

        // 1. Read JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile.toFile());

        String severity = root.get("Severity").asText();
        String errorMessage = root.get("Error Message").asText();

        // 2. Get ALL log files sorted by last modified (newest first)
        List<Path> logFiles = Files.list(folder)
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparingLong((Path f) -> f.toFile().lastModified()).reversed())
                .collect(Collectors.toList());

        if (logFiles.isEmpty()) {
            throw new RuntimeException("No log files found in folder: " + folder);
        }

        // 3. Prepare Markdown output
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            writer.write("# 🔍 Error Report\n\n");
            writer.write("## Error Details\n");
            writer.write("- **Severity:** " + severity + "\n");
            writer.write("- **Error Message:** `" + errorMessage + "`\n\n");

            writer.write("## 📄 Matching Log Entries\n\n");

            boolean foundAny = false;

            // 4. Process each log file
            for (Path logFile : logFiles) {

                List<String> lines = Files.readAllLines(logFile);

                for (String line : lines) {
                    if (line.contains(errorMessage)) {
                        foundAny = true;

                        writer.write("### Match\n");
                        writer.write("**File:** `" + logFile.getFileName() + "`\n\n");
                        writer.write("```\n" + line + "\n```\n\n");
                    }
                }
            }

            if (!foundAny) {
                writer.write("> No matching entries found in any log file.\n");
            }
        }

        System.out.println("output.md created successfully.");
    }
}
```

---

# 📌 What This Version Does Better
### ✔ Processes **all files**, not just the newest  
### ✔ Continues until **no more files**  
### ✔ Writes **each match as a new Markdown block**  
### ✔ Includes **file name** for every match  
### ✔ Beautiful, readable `output.md`  

---

# 📄 Example Output

```markdown
# 🔍 Error Report

## Error Details
- Severity: High
- Error Message: `Connection failed`

## 📄 Matching Log Entries

### Match
**File:** `server-2026-02-16.log`

```
2026-02-16 18:22:11 ERROR Connection failed for user admin
```

### Match
**File:** `server-2026-02-15.log`

```
2026-02-15 09:14:55 ERROR Connection failed: timeout
```
```

---

# Want enhancements?
I can add:

- Parallel file scanning  
- Regex support  
- Highlighting the error message in Markdown  
- Grouping results by file  
- Sorting matches by timestamp  

Just tell me what direction you want to take this.
