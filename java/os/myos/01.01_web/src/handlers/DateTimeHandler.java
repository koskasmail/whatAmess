package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.time.*;

public class DateTimeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) throws IOException {
        String json = "{ \"date\": \"" + LocalDate.now() +
                      "\", \"time\": \"" + LocalTime.now() + "\" }";
        send(ex, json);
    }

    private void send(HttpExchange ex, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, json.length());
        ex.getResponseBody().write(json.getBytes());
        ex.close();
    }
}
