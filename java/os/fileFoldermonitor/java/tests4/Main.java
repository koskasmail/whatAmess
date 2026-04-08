package tests4;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Path path = Paths.get("/home/yaron/Downloads/");

        try {
            FileMonitorInSubFolder monitor = new FileMonitorInSubFolder(path);
            new Thread(monitor).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("File monitor is running...");
    }
}