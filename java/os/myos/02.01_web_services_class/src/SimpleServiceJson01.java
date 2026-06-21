package services;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;

public class SimpleServiceJson01 {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/time", new TimeHandler());
        server.createContext("/date", new DateHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running on http://localhost:8080/");
    }

    // ---------------- HANDLERS ----------------

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response =
                    "<h1>Welcome</h1>" +
                    "<p><a href='/time'>Show Server Time (JSON)</a></p>" +
                    "<p><a href='/date'>Show Server Date (JSON)</a></p>";

            send(exchange, response, "text/html");
        }
    }

    static class TimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"time\":\"" + LocalTime.now().toString() + "\"}";
            send(exchange, json, "application/json");
        }
    }

    static class DateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"date\":\"" + LocalDate.now().toString() + "\"}";
            send(exchange, json, "application/json");
        }
    }

    // Utility method
    private static void send(HttpExchange exchange, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
