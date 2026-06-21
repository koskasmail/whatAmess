package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;

public class StaticFileHandler implements HttpHandler {

    private final String filePath;

    public StaticFileHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            String msg = "404 Not Found";
            exchange.sendResponseHeaders(404, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
            return;
        }

        String contentType = guessType(filePath);
        byte[] bytes = Files.readAllBytes(file.toPath());

        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private String guessType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "text/plain";
    }
}
