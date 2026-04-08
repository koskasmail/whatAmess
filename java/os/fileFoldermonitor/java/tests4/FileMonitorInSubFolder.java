package tests4;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class FileMonitorInSubFolder implements Runnable {

    private final Path rootPath;
    private final WatchService watchService;
    private final Map<WatchKey, Path> keyMap = new HashMap<>();

    public FileMonitorInSubFolder(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.watchService = FileSystems.getDefault().newWatchService();
        registerAllSubFolders(rootPath);
    }

    private void registerAllSubFolders(Path start) throws IOException {
        Files.walk(start)
                .filter(Files::isDirectory)
                .forEach(path -> {
                    try {
                        WatchKey key = path.register(
                                watchService,
                                StandardWatchEventKinds.ENTRY_CREATE
                        );
                        keyMap.put(key, path);
                        System.out.println("Watching folder: " + path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void run() {
        System.out.println("File monitor started for: " + rootPath);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                return;
            }

            Path dir = keyMap.get(key);
            if (dir == null) continue;

            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path created = dir.resolve((Path) event.context());

                    if (Files.isDirectory(created)) {
                        try {
                            registerAllSubFolders(created);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
							printFileInfo(created);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keyMap.remove(key);
                if (keyMap.isEmpty()) break;
            }
        }
    }

    private void printFileInfo(Path file) throws IOException {
        long sizeBytes = Files.size(file);

        double sizeKB = sizeBytes / 1024.0;
        double sizeMB = sizeKB / 1024.0;
        double sizeGB = sizeMB / 1024.0;

        System.out.println("\n=== New File Detected ===");
        System.out.println("File Name: " + file.getFileName());
        System.out.println("Path: " + file.getParent());
        System.out.println("Full Path: " + file.toAbsolutePath());
        System.out.println("Size (bytes): " + sizeBytes);
        System.out.printf("Size (KB): %.2f%n", sizeKB);
        System.out.printf("Size (MB): %.2f%n", sizeMB);
        System.out.printf("Size (GB): %.4f%n", sizeGB);
        System.out.println("=========================\n");
    }
}
