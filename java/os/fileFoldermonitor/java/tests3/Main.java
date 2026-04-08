package tests3;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Path[] paths = {        		
//                Paths.get("/home/yaron/Downloads/"),
                Paths.get("/home/yaron/Documents/")
//                Paths.get("/home/yaron/eclipse-workspace/sample/src/main/java/tests/")
        };

        System.out.println("Starting watchers...");

        for (Path p : paths) {
            try {
                // Watch only top-level folder
                DirectoryWatcher watcher = new DirectoryWatcher(p);
                new Thread(watcher).start();

                // Watch all subfolders recursively
                SubDirectoryWatcher subWatcher = new SubDirectoryWatcher(p);
                new Thread(subWatcher).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
