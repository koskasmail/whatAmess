package tests3;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class SubDirectoryWatcher implements Runnable {

    private final Path rootPath;
    private final WatchService watchService;
    private final Map<WatchKey, Path> keyMap = new HashMap<>();

    public SubDirectoryWatcher(Path rootPath) throws IOException {
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
                        System.out.println("Watching: " + path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void run() {
        System.out.println("Recursive watcher started for: " + rootPath);

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
                            printDirectoryInfo(created);
                        } catch (IOException e) {
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

    private void printDirectoryInfo(Path folder) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(folder, BasicFileAttributes.class);

        LocalDateTime creationTime = LocalDateTime.ofInstant(
                attrs.creationTime().toInstant(),
                ZoneId.systemDefault()
        );

        long fileCount = Files.list(folder).count();

        System.out.println("\n=== New Subfolder Detected ===");
        System.out.println("Path: " + folder);
        System.out.println("Created: " + creationTime.toLocalDate() + " " + creationTime.toLocalTime());
        System.out.println("Total Files: " + fileCount);
        System.out.println("==============================\n");
    }
}
