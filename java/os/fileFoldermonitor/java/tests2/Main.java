package tests2;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Path[] paths = {
//        		Paths.get("/"),
        		Paths.get("/home/yaron/"),
                Paths.get("/home/yaron/Downloads/"),
                Paths.get("/home/yaron/Documents/"),
                Paths.get("/home/yaron/eclipse-workspace/sample/src/main/java/tests/")
        };

        for (Path p : paths) {
            DirectoryWatcher watcher = new DirectoryWatcher(p);
            Thread t = new Thread(watcher);
            t.start();
        }

        System.out.println("Directory watchers started.");
    }
}
