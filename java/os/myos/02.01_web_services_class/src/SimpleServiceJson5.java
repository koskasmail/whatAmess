package services;


import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;

public class SimpleServiceJson5 {

    public static void main(String[] args) throws Exception {

        // Listen on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoints
        server.createContext("/", new RootHandler());
        server.createContext("/time", new TimeHandler());
        server.createContext("/date", new DateHandler());
        server.createContext("/dateTime", new DateTimeHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running at http://localhost:8080/");
        System.out.println("version: 5");
    }

    // ---------------- HANDLERS ----------------

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html =
                "<h1>Welcome</h1>" +
                "<p><a href='/time'>Server Time (JSON)</a></p>" +
                "<p><a href='/date'>Server Date (JSON)</a></p>" +
                "<p><a href='/dateTime'>Server Date + Time (JSON)</a></p>";

            send(exchange, html, "text/html");
        }
    }

    static class TimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"time\":\"" + LocalTime.now() + "\"}";
            send(exchange, json, "application/json");
        }
    }

    static class DateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"date\":\"" + LocalDate.now() + "\"}";
            send(exchange, json, "application/json");
        }
    }

    static class DateTimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json =
                "{ \"date\": \"" + LocalDate.now() +
                "\", \"time\": \"" + LocalTime.now() + "\" }";

            send(exchange, json, "application/json");
        }
    }

  
//    private static void send(HttpExchange exchange, String response, String contentType) throws IOException {
//
//        // Handle CORS preflight
//        if ("OPTIONS".equals(exchange.getRequestMethod())) {
//            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
//            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
//            exchange.sendResponseHeaders(204, -1);
//            return;
//        }
//
//        // Normal response
//        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
//        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
//
//        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
//        exchange.sendResponseHeaders(200, response.getBytes().length);
//
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }
    
    private static void handleCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // No content
        }
    }

    private static void send(HttpExchange exchange, String response, String contentType) throws IOException {

        // Always add CORS headers
        handleCors(exchange);

        // Stop here if it's a preflight request
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            return;
        }

        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    

}
