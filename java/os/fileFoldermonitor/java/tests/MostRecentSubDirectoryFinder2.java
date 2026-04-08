package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Stream;

public class MostRecentSubDirectoryFinder2 {

    public MostRecentSubDirectoryFinder2()  {
        Path[] paths = {
                Paths.get("/home/yaron/Downloads/"),
                Paths.get("/home/yaron/Documents/"),
                Paths.get("/home/yaron/eclipse-workspace/sample/src/main/java/tests/")
        };

        try {
            String jsonResult = findMostRecentSubDirectory(paths);

            if (jsonResult != null) {
                System.out.println("Gson JSON Result: " + jsonResult);
            } else {
                System.out.println("No subdirectories found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findMostRecentSubDirectory(Path[] paths) throws IOException {
        Optional<Path> latestDir = Optional.empty();
        FileTime latestTime = FileTime.fromMillis(0);

        for (Path startPath : paths) {
            try (Stream<Path> subPaths = Files.walk(startPath, 1)) {
                Optional<Path> recentDir = subPaths
                        .filter(Files::isDirectory)
                        .filter(path -> !path.equals(startPath))
                        .max((path1, path2) -> {
                            try {
                                return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                if (recentDir.isPresent()) {
                    FileTime currentTime = Files.getLastModifiedTime(recentDir.get());
                    if (currentTime.compareTo(latestTime) > 0) {
                        latestTime = currentTime;
                        latestDir = recentDir;
                    }
                }
            }
        }

        if (latestDir.isPresent()) {
            Path mostRecentPath = latestDir.get();
            BasicFileAttributes attrs = Files.readAttributes(mostRecentPath, BasicFileAttributes.class);
            LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());

            long totalFiles;
            try (Stream<Path> files = Files.list(mostRecentPath)) {
                totalFiles = files.count();
            }

            // Create an object to hold the information
            DirectoryInfo directoryInfo = new DirectoryInfo(
                    mostRecentPath.toString(),
                    lastModifiedTime.toLocalDate().toString(),
                    lastModifiedTime.toLocalTime().toString(),
                    totalFiles
            );

            // Convert to JSON using Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(directoryInfo);
        }

        return null;
    }

    // DirectoryInfo class to hold directory information
    static class DirectoryInfo {
        String folder;
        String date;
        String time;
        long totalFiles;

        public DirectoryInfo(String folder, String date, String time, long totalFiles) {
            this.folder = folder;
            this.date = date;
            this.time = time;
            this.totalFiles = totalFiles;
        }
    }
}
