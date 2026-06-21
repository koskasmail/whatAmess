package handlers;

//public class TimeHandler {
//
//	public TimeHandler() {
//		// TODO Auto-generated constructor stub
//	}
//
//}
//
//
//package myapp.handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.time.LocalTime;

public class TimeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) throws IOException {
        String json = "{\"time\":\"" + LocalTime.now() + "\"}";
        send(ex, json);
    }

    private void send(HttpExchange ex, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, json.length());
        ex.getResponseBody().write(json.getBytes());
        ex.close();
    }
}
