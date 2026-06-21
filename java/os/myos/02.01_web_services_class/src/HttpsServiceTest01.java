//package services;
//
//import com.sun.net.httpserver.*;
//import javax.net.ssl.*;
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.security.KeyStore;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//public class HttpsServiceTest01 {
//
//    public static void main(String[] args) throws Exception {
//
//        // Load keystore
//        char[] pass = "password".toCharArray();
//        KeyStore ks = KeyStore.getInstance("JKS");
//        ks.load(new FileInputStream("server.jks"), pass);
//
//        // Create key manager
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//        kmf.init(ks, pass);
//
//        // Create SSL context
//        SSLContext ssl = SSLContext.getInstance("TLS");
//        ssl.init(kmf.getKeyManagers(), null, null);
//
//        // Create HTTPS server
//        HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
//        server.setHttpsConfigurator(new HttpsConfigurator(ssl));
//
//        // Routes
//        server.createContext("/", new RootHandler());
//        server.createContext("/time", new TimeHandler());
//        server.createContext("/date", new DateHandler());
//
//        server.setExecutor(null);
//        server.start();
//
//        System.out.println("HTTPS server running at https://localhost:8443/");
//    }
//
//    // Handlers
//    static class RootHandler implements HttpHandler {
//        public void handle(HttpExchange ex) throws IOException {
//            String html =
//                "<h1>Welcome</h1>" +
//                "<p><a href='/time'>Server Time</a></p>" +
//                "<p><a href='/date'>Server Date</a></p>";
//            send(ex, html);
//        }
//    }
//
//    static class TimeHandler implements HttpHandler {
//        public void handle(HttpExchange ex) throws IOException {
//            send(ex, "Server time: " + LocalTime.now());
//        }
//    }
//
//    static class DateHandler implements HttpHandler {
//        public void handle(HttpExchange ex) throws IOException {
//            send(ex, "Server date: " + LocalDate.now());
//        }
//    }
//
//    private static void send(HttpExchange ex, String msg) throws IOException {
//        ex.sendResponseHeaders(200, msg.getBytes().length);
//        OutputStream os = ex.getResponseBody();
//        os.write(msg.getBytes());
//        os.close();
//    }
//}
//
//
///*
// * Bug
// * Https service isn't working.
// */
////Exception in thread "main" java.io.FileNotFoundException: server.jks (No such file or directory)
////at java.io.FileInputStream.open0(Native Method)
////at java.io.FileInputStream.open(FileInputStream.java:195)
////at java.io.FileInputStream.<init>(FileInputStream.java:138)
////at java.io.FileInputStream.<init>(FileInputStream.java:93)
////at services.HttpsServiceTest01.main(HttpsServiceTest01.java:18)
//
