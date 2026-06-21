package services;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;

public class SimpleService {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // "/" → Welcome page
        server.createContext("/", new RootHandler());

        // "/time" → Server time
        server.createContext("/time", new TimeHandler());

        // "/date" → Server date
        server.createContext("/date", new DateHandler());

        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Server running on http://localhost:8080/");
    }

    // ---------------- HANDLERS ----------------

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response =
                    "<h1>Welcome</h1>" +
                    "<p><a href='/time'>Show Server Time</a></p>" +
                    "<p><a href='/date'>Show Server Date</a></p>";

            sendResponse(exchange, response);
        }
    }

    static class TimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Server time: " + LocalTime.now().toString();
            sendResponse(exchange, response);
        }
    }

    static class DateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Server date: " + LocalDate.now().toString();
            sendResponse(exchange, response);
        }
    }

    // Utility method
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
