package tests3;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DirectoryWatcher implements Runnable {

    private final Path pathToWatch;

    public DirectoryWatcher(Path pathToWatch) {
        this.pathToWatch = pathToWatch;
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            pathToWatch.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );

            System.out.println("Watching for new folders in: " + pathToWatch);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path created = pathToWatch.resolve((Path) event.context());

                        if (Files.isDirectory(created)) {
                            printDirectoryInfo(created);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printDirectoryInfo(Path folder) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(folder, BasicFileAttributes.class);

        LocalDateTime creationTime = LocalDateTime.ofInstant(
                attrs.creationTime().toInstant(),
                ZoneId.systemDefault()
        );

        long fileCount = Files.list(folder).count();

        System.out.println("\n=== New Folder Detected ===");
        System.out.println("Path: " + folder);
        System.out.println("Created: " + creationTime.toLocalDate() + " " + creationTime.toLocalTime());
        System.out.println("Total Files: " + fileCount);
        System.out.println("===========================\n");
    }
}
