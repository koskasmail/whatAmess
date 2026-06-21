You’re getting:

```
java.net.BindException: Address already in use
```

because **another instance of your server is already running on that port**.  
If you embed your UI inside the Java server (as you’re doing now), then **the server must check the port before starting** and exit cleanly if it’s already in use.

Below is the **correct, safe, Java‑1.8‑compatible way** to check whether the port is free **before** creating the `HttpServer`.

---

## ✅ **Add a port‑check before starting the server**

This method tries to bind the port using a temporary `ServerSocket`.  
If it fails → the port is in use → exit gracefully.

```java
private static boolean isPortAvailable(int port) {
    try {
        java.net.ServerSocket socket = new java.net.ServerSocket(port);
        socket.close();
        return true;
    } catch (Exception e) {
        return false;
    }
}
```

---

## ✅ **Use it in your Main.java**

```java
int port = 8080;

if (!isPortAvailable(port)) {
    System.out.println("Server already running on port " + port + ". Exiting.");
    System.exit(0);
}

HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
```

This prevents the crash and avoids the stack trace.

---

## 🟦 **Full example inside your existing Main.java**

```java
package myapp;

import com.sun.net.httpserver.HttpServer;
import myapp.handlers.*;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = 8080;

        if (!isPortAvailable(port)) {
            System.out.println("Server already running on port " + port + ". Exiting.");
            System.exit(0);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new StaticFileHandler("web/index.html"));
        server.createContext("/style.css", new StaticFileHandler("web/style.css"));
        server.createContext("/app.js", new StaticFileHandler("web/app.js"));

        server.createContext("/time", new TimeHandler());
        server.createContext("/date", new DateHandler());
        server.createContext("/dateTime", new DateTimeHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running at http://localhost:" + port + "/");
    }

    private static boolean isPortAvailable(int port) {
        try {
            java.net.ServerSocket socket = new java.net.ServerSocket(port);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

---

## 🟩 **Why this works**

- It checks the port **before** Java’s `HttpServer.create()` tries to bind.
- If the port is taken, the program exits cleanly.
- No exceptions, no stack trace, no crash.
- Works on **Java 1.8** and all OSes.

---

## 🟧 Next steps you can take

You can now safely extend your embedded UI server.  
Choose what you want next:

- Add Bootstrap support  
- Add image/static folder support  
- Add POST endpoints  
- Add SQLite or MySQL database  

Tell me which direction you want to grow this mini‑OS server.