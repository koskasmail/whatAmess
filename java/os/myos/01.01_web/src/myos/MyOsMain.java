package myos;

import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.net.InetSocketAddress;



public class MyOsMain {
	
    public static String getProjectRoot() {
        return System.getProperty("user.dir");
    }

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        String root = getProjectRoot() + "/src/web/";

        // Static files
        server.createContext("/", new StaticFileHandler(root + "index.html"));
        server.createContext("/style.css", new StaticFileHandler(root + "style.css"));
        server.createContext("/app.js", new StaticFileHandler(root + "app.js"));

        // API
        server.createContext("/time", new TimeHandler());
        server.createContext("/date", new DateHandler());
        server.createContext("/dateTime", new DateTimeHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running: ");
        System.out.println("http://localhost:8080/");
        System.out.println("http://kessler101:8080/");
        System.out.println("code, version: 1.1");
    }
}

