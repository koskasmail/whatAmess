I'll update your `FM01` program to:

1. Monitor `C:\json` recursively for file creation, modification, and deletion.
2. Record the file's last-modified date and time, plus the date and time the event was detected, in the format `yyyyMMdd'T'HHmmssSSS` (for example, `20261022T190812345`).
3. Export all monitoring events as JSON.
4. Create a new JSON file whenever the current log reaches approximately 1 MB, then continue writing to the next file.
5. Include the file path, event type, file size, previous size, current size, and timestamps where available.

I'll also fix the compilation errors in your current code, including the invalid `private final Path` declaration inside `main()` and the undefined `path` variable.

## Complete Java code — `FM01.java`

This version uses only standard Java libraries, so you don't need Gson or another JSON dependency. Each output file is a valid JSON array, and log files are stored outside the monitored folder to avoid monitoring the monitor's own output.

```

package os;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class FM01 implements Runnable {

    private static final long MAX_FILE_SIZE = 1024L * 1024L; // 1 MB

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS");

    private final Path rootPath;
    private final Path outputPath;
    private final WatchService watchService;

    private final Map<WatchKey, Path> keyMap = new HashMap<>();
    private final Map<Path, FileInfo> fileStates = new HashMap<>();

    private final JsonLogWriter jsonWriter;

    private static class FileInfo {
        final long size;
        final Instant lastModified;

        FileInfo(BasicFileAttributes attrs) {
            this.size = attrs.size();
            this.lastModified = attrs.lastModifiedTime().toInstant();
        }

        FileInfo(Path file) throws IOException {
            this(Files.readAttributes(file, BasicFileAttributes.class));
        }
    }

    public FM01(Path rootPath, Path outputPath) throws IOException {
        this.rootPath = rootPath.toAbsolutePath().normalize();
        this.outputPath = outputPath.toAbsolutePath().normalize();

        if (!Files.isDirectory(this.rootPath)) {
            throw new IOException(
                    "Monitor folder does not exist: " + this.rootPath);
        }

        if (this.outputPath.startsWith(this.rootPath)) {
            throw new IOException(
                    "Output folder must be outside the monitored folder.");
        }

        Files.createDirectories(this.outputPath);

        this.watchService = FileSystems.getDefault().newWatchService();
        this.jsonWriter = new JsonLogWriter(this.outputPath);

        scanInitialFiles(this.rootPath);
        registerAllSubFolders(this.rootPath);
    }

    // Scan existing files and record their initial state.
    private void scanInitialFiles(Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(
                    Path file, BasicFileAttributes attrs) {

                if (attrs.isRegularFile()) {
                    Path normalized = file.toAbsolutePath().normalize();
                    FileInfo info = new FileInfo(attrs);
                    fileStates.put(normalized, info);

                    logEvent("EXISTING", normalized, info, null);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(
                    Path file, IOException error) {
                System.err.println("Cannot scan " + file + ": "
                        + error.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("Initial file state saved.");
    }

    private void registerAllSubFolders(Path start) throws IOException {
        try (Stream<Path> paths = Files.walk(start)) {
            java.util.Iterator<Path> iterator =
                    paths.filter(Files::isDirectory).iterator();

            while (iterator.hasNext()) {
                registerFolder(iterator.next());
            }
        }
    }

    private void registerFolder(Path folder) {
        try {
            WatchKey key = folder.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            keyMap.put(key, folder);
            System.out.println("Watching folder: " + folder);

        } catch (IOException e) {
            System.err.println("Cannot watch folder " + folder
                    + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("File monitor started: " + rootPath);
        System.out.println("JSON logs saved to: " + outputPath);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                WatchKey key;

                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (ClosedWatchServiceException e) {
                    break;
                }

                Path dir = keyMap.get(key);

                if (dir != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                            System.err.println(
                                    "WARNING: Some file events may be lost.");
                            continue;
                        }

                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> pathEvent =
                                (WatchEvent<Path>) event;

                        Path file = dir.resolve(pathEvent.context())
                                .toAbsolutePath().normalize();

                        try {
                            if (event.kind()
                                    == StandardWatchEventKinds.ENTRY_CREATE) {
                                handleCreate(file);

                            } else if (event.kind()
                                    == StandardWatchEventKinds.ENTRY_MODIFY) {
                                handleModify(file);

                            } else if (event.kind()
                                    == StandardWatchEventKinds.ENTRY_DELETE) {
                                handleDelete(file);
                            }

                        } catch (IOException e) {
                            System.err.println("Could not inspect " + file
                                    + ": " + e.getMessage());
                        }
                    }
                }

                if (!key.reset()) {
                    keyMap.remove(key);
                    if (keyMap.isEmpty()) {
                        break;
                    }
                }
            }
        } finally {
            try {
                jsonWriter.close();
            } catch (IOException e) {
                System.err.println("Cannot close JSON writer: "
                        + e.getMessage());
            }

            try {
                watchService.close();
            } catch (IOException ignored) {
            }

            System.out.println("File monitor stopped.");
        }
    }

    private void handleCreate(Path file) throws IOException {
        if (Files.isDirectory(file)) {
            registerAllSubFolders(file);
            return;
        }

        if (!Files.isRegularFile(file)) {
            return;
        }

        FileInfo current = new FileInfo(file);
        FileInfo previous = fileStates.put(file, current);

        logEvent("CREATED", file, current, previous);
        printEvent("NEW FILE", file, current);
    }

    private void handleModify(Path file) throws IOException {
        if (!Files.isRegularFile(file)) {
            return;
        }

        FileInfo current = new FileInfo(file);
        FileInfo previous = fileStates.get(file);

        if (previous == null) {
            fileStates.put(file, current);
            logEvent("DISCOVERED", file, current, null);
            printEvent("FILE DISCOVERED", file, current);
            return;
        }

        if (previous.size != current.size
                || !previous.lastModified.equals(current.lastModified)) {

            fileStates.put(file, current);
            logEvent("MODIFIED", file, current, previous);

            System.out.println("\n=== FILE CHANGED ===");
            System.out.println("File: " + file);
            System.out.println("Before size: " + previous.size + " bytes");
            System.out.println("After size:  " + current.size + " bytes");
            System.out.println("Difference:  "
                    + (current.size - previous.size) + " bytes");
        }
    }

    private void handleDelete(Path file) throws IOException {
        FileInfo previous = fileStates.remove(file);

        logEvent("DELETED", file, null, previous);

        System.out.println("\n=== FILE DELETED ===");
        System.out.println("File: " + file);
    }

    private void logEvent(String event, Path file,
                          FileInfo current, FileInfo previous) {
        try {
            jsonWriter.writeEvent(event, file, current, previous);
        } catch (IOException e) {
            System.err.println("Cannot write JSON event: "
                    + e.getMessage());
        }
    }

    private void printEvent(String label, Path file, FileInfo info) {
        System.out.println("\n=== " + label + " ===");
        System.out.println("File: " + file);
        System.out.println("Size: " + info.size + " bytes");
        System.out.println("Size KB: " + info.size / 1024.0);
        System.out.println("Modified: " + format(info.lastModified));
        System.out.println("Detected: " + format(Instant.now()));
    }

    private static String format(Instant instant) {
        return FORMAT.format(
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    // Writes valid JSON arrays and rotates output at approximately 1 MB.
    private static class JsonLogWriter implements AutoCloseable {

        private final Path outputPath;
        private BufferedWriter writer;
        private long bytesWritten;
        private boolean firstRecord;
        private Path currentFile;

        JsonLogWriter(Path outputPath) throws IOException {
            this.outputPath = outputPath;
            openNewFile();
        }

        synchronized void writeEvent(String event, Path file,
                                     FileInfo current, FileInfo previous)
                throws IOException {

            Instant detected = Instant.now();

            StringBuilder json = new StringBuilder();
            json.append("  {\n");
            field(json, "event", event, true);
            field(json, "filePath", file.toString(), true);
            field(json, "detectedTime", format(detected), true);
            field(json, "fileModifiedTime",
                    current != null ? format(current.lastModified)
                            : previous != null
                            ? format(previous.lastModified) : null, true);
            number(json, "sizeBytes",
                    current != null ? current.size : null, true);
            number(json, "previousSizeBytes",
                    previous != null ? previous.size : null, true);
            number(json, "sizeDifferenceBytes",
                    current != null && previous != null
                            ? current.size - previous.size : null, false);
            json.append("  }");

            String record = json.toString();
            long recordBytes = record.getBytes(StandardCharsets.UTF_8).length;
            long commaBytes = firstRecord ? 0 : 2;
            long closingBytes = 2; // newline and closing bracket

            // Rotate before the next record if it would exceed 1 MB.
            if (!firstRecord
                    && bytesWritten + commaBytes + recordBytes
                    + closingBytes > MAX_FILE_SIZE) {
                openNewFile();
            }

            if (!firstRecord) {
                writer.write(",\n");
                bytesWritten += 2;
            }

            writer.write(record);
            bytesWritten += recordBytes;
            firstRecord = false;
            writer.flush();
        }

        private void openNewFile() throws IOException {
            if (writer != null) {
                writer.write("\n]\n");
                writer.close();
            }

            String timestamp = format(Instant.now());
            String filename = "folderMonitor_" + timestamp + ".json";
            currentFile = outputPath.resolve(filename);

            // Avoid overwriting a file if two rotations happen in one ms.
            int suffix = 1;
            while (Files.exists(currentFile)) {
                currentFile = outputPath.resolve(
                        "folderMonitor_" + timestamp + "_"
                                + suffix++ + ".json");
            }

            writer = Files.newBufferedWriter(
                    currentFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE);

            writer.write("[\n");
            bytesWritten = 2;
            firstRecord = true;

            System.out.println("Writing JSON: " + currentFile);
        }

        private static void field(StringBuilder json, String name,
                                  String value, boolean comma) {
            json.append("    \"").append(escape(name)).append("\": ");
            if (value == null) {
                json.append("null");
            } else {
                json.append("\"").append(escape(value)).append("\"");
            }
            json.append(comma ? ",\n" : "\n");
        }

        private static void number(StringBuilder json, String name,
                                   Long value, boolean comma) {
            json.append("    \"").append(escape(name)).append("\": ");
            json.append(value == null ? "null" : value);
            json.append(comma ? ",\n" : "\n");
        }

        private static String escape(String value) {
            StringBuilder result = new StringBuilder();
            for (char c : value.toCharArray()) {
                switch (c) {
                    case '"':  result.append("\\\""); break;
                    case '\\': result.append("\\\\"); break;
                    case '\b': result.append("\\b"); break;
                    case '\f': result.append("\\f"); break;
                    case '\n': result.append("\\n"); break;
                    case '\r': result.append("\\r"); break;
                    case '\t': result.append("\\t"); break;
                    default:
                        if (c < 0x20) {
                            result.append(String.format("\\u%04x", (int) c));
                        } else {
                            result.append(c);
                        }
                }
            }
            return result.toString();
        }

        @Override
        public synchronized void close() throws IOException {
            if (writer != null) {
                writer.write("\n]\n");
                writer.close();
                writer = null;
                System.out.println("Closed JSON: " + currentFile);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path monitorFolder = Paths.get("C:\\json");
        Path logFolder = Paths.get("C:\\folderMonitorLogs");

        FM01 monitor = new FM01(monitorFolder, logFolder);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping monitor...");
            try {
                monitor.watchService.close();
            } catch (IOException ignored) {
            }
        }));

        monitor.run();
    }
}

```

## Example output

The program creates files such as:

```
C:\folderMonitorLogs\
    folderMonitor_20261022T190812345.json
    folderMonitor_20261022T191045678.json
```

Each JSON file contains records similar to:

```
[
  {
    "event": "MODIFIED",
    "filePath": "C:\\json\\test.txt",
    "detectedTime": "20261022T190812345",
    "fileModifiedTime": "20261022T190811900",
    "sizeBytes": 2048,
    "previousSizeBytes": 1024,
    "sizeDifferenceBytes": 1024
  }
]
```

### Important behavior

- `detectedTime` — when the monitor detected the event.
- `fileModifiedTime` — the file's last-modified timestamp, or the last known timestamp for a deleted file.
- `sizeBytes` — current file size; `null` for deletion events.
- `previousSizeBytes` — the previous size, when available.
- `sizeDifferenceBytes` — the size change in bytes.

Rotation: A new JSON file is opened when adding the next record would exceed 1 MB. An individual record larger than 1 MB is still written, so the size limit is approximate.

One limitation: Java's `WatchService` can report several events for one operation and may miss events if its event queue overflows. This implementation is suitable for general monitoring, but it is not a guaranteed audit trail.

Your last sentence ended with “inside the file try to do a t”. If you intended an additional feature inside each JSON record, send the rest of that requirement and I'll incorporate it.
