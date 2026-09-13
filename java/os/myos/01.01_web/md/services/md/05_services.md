
#### 05_services 

#### (04_services + 03_services)


I would add:

* `GET /person` → returns all records from H2
* `GET /person/search?name=John` → searches **first name OR last name**
* H2 database stored locally in `./data/people`
* Automatic table creation
* Proper CORS handling
* UTF-8 JSON
* Your existing `/time`, `/date`, `/dateTime` endpoints remain

Also, your current code has this accidental text:

```java
server.setExecutor(null);https://developer.chrome.com/docs/webstore/publish
```

That must be removed.

### Complete `SimpleServiceJson5.java`

```java
package services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleServiceJson5 {

    // ============================================================
    // SERVER CONFIGURATION
    // ============================================================

    private static final int PORT = 8080;

    // H2 database stored in:
    // ./data/people.mv.db
    private static final String DB_URL =
            "jdbc:h2:file:./data/people";

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) throws Exception {

        // Create / initialize H2 database
        initializeDatabase();

        // Create HTTP server
        HttpServer server =
                HttpServer.create(new InetSocketAddress(PORT), 0);

        // ========================================================
        // ENDPOINTS
        // ========================================================

        server.createContext("/", new RootHandler());

        server.createContext("/time", new TimeHandler());

        server.createContext("/date", new DateHandler());

        server.createContext("/dateTime", new DateTimeHandler());

        // Get all people
        server.createContext("/person", new PersonHandler());

        // Search people
        server.createContext("/person/search", new PersonSearchHandler());

        server.setExecutor(null);

        server.start();

        System.out.println("----------------------------------------");
        System.out.println("Server running");
        System.out.println("http://localhost:" + PORT + "/");
        System.out.println("----------------------------------------");

        System.out.println("Available services:");
        System.out.println("http://localhost:8080/time");
        System.out.println("http://localhost:8080/date");
        System.out.println("http://localhost:8080/dateTime");
        System.out.println("http://localhost:8080/person");
        System.out.println("http://localhost:8080/person/search?name=John");

        System.out.println("----------------------------------------");
        System.out.println("H2 database:");
        System.out.println(DB_URL);
        System.out.println("----------------------------------------");

        System.out.println("Version: 6");
    }

    // ============================================================
    // ROOT
    // ============================================================

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String html =
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>Simple Service</title>" +
                    "</head>" +
                    "<body>" +

                    "<h1>Simple Service</h1>" +

                    "<h2>Time / Date</h2>" +

                    "<p>" +
                    "<a href='/time'>Server Time (JSON)</a>" +
                    "</p>" +

                    "<p>" +
                    "<a href='/date'>Server Date (JSON)</a>" +
                    "</p>" +

                    "<p>" +
                    "<a href='/dateTime'>Server Date + Time (JSON)</a>" +
                    "</p>" +

                    "<h2>People</h2>" +

                    "<p>" +
                    "<a href='/person'>Get All People</a>" +
                    "</p>" +

                    "<p>" +
                    "<a href='/person/search?name=John'>" +
                    "Search Person: John" +
                    "</a>" +
                    "</p>" +

                    "</body>" +
                    "</html>";

            send(exchange, html, "text/html");
        }
    }

    // ============================================================
    // TIME
    // ============================================================

    static class TimeHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (!isGetOrOptions(exchange)) {
                sendMethodNotAllowed(exchange);
                return;
            }

            String json =
                    "{\"time\":\"" +
                    LocalTime.now() +
                    "\"}";

            send(exchange, json, "application/json");
        }
    }

    // ============================================================
    // DATE
    // ============================================================

    static class DateHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (!isGetOrOptions(exchange)) {
                sendMethodNotAllowed(exchange);
                return;
            }

            String json =
                    "{\"date\":\"" +
                    LocalDate.now() +
                    "\"}";

            send(exchange, json, "application/json");
        }
    }

    // ============================================================
    // DATE + TIME
    // ============================================================

    static class DateTimeHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if (!isGetOrOptions(exchange)) {
                sendMethodNotAllowed(exchange);
                return;
            }

            String json =
                    "{ \"date\": \"" +
                    LocalDate.now() +
                    "\", \"time\": \"" +
                    LocalTime.now() +
                    "\" }";

            send(exchange, json, "application/json");
        }
    }

    // ============================================================
    // GET ALL PEOPLE
    //
    // GET /person
    // ============================================================

    static class PersonHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, "", "application/json");
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendMethodNotAllowed(exchange);
                return;
            }

            try {

                List<Person> people = getAllPeople();

                String json = peopleToJson(people);

                send(
                        exchange,
                        json,
                        "application/json"
                );

            } catch (Exception e) {

                e.printStackTrace();

                String json =
                        "{ \"error\": \"" +
                        escapeJson(e.getMessage()) +
                        "\" }";

                send(
                        exchange,
                        json,
                        "application/json",
                        500
                );
            }
        }
    }

    // ============================================================
    // SEARCH PEOPLE
    //
    // GET /person/search?name=John
    //
    // Searches:
    // firstName OR lastName
    // ============================================================

    static class PersonSearchHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, "", "application/json");
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendMethodNotAllowed(exchange);
                return;
            }

            try {

                String query =
                        exchange.getRequestURI().getQuery();

                String name = getQueryParameter(query, "name");

                if (name == null || name.trim().isEmpty()) {

                    String json =
                            "{ \"error\": \"Parameter 'name' is required\" }";

                    send(
                            exchange,
                            json,
                            "application/json",
                            400
                    );

                    return;
                }

                List<Person> people =
                        searchPeople(name.trim());

                String json = peopleToJson(people);

                send(
                        exchange,
                        json,
                        "application/json"
                );

            } catch (Exception e) {

                e.printStackTrace();

                String json =
                        "{ \"error\": \"" +
                        escapeJson(e.getMessage()) +
                        "\" }";

                send(
                        exchange,
                        json,
                        "application/json",
                        500
                );
            }
        }
    }

    // ============================================================
    // PERSON CLASS
    // ============================================================

    static class Person {

        private String firstName;
        private String lastName;
        private String phone;
        private String cellphone;

        public Person(
                String firstName,
                String lastName,
                String phone,
                String cellphone) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.cellphone = cellphone;
        }
    }

    // ============================================================
    // INITIALIZE H2 DATABASE
    // ============================================================

    private static void initializeDatabase()
            throws SQLException {

        System.out.println("Initializing H2 database...");

        try (Connection connection =
                     DriverManager.getConnection(
                             DB_URL,
                             DB_USER,
                             DB_PASSWORD)) {

            String createTable =
                    "CREATE TABLE IF NOT EXISTS person (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(100), " +
                    "last_name VARCHAR(100), " +
                    "phone VARCHAR(50), " +
                    "cellphone VARCHAR(50)" +
                    ")";

            try (Statement statement =
                         connection.createStatement()) {

                statement.execute(createTable);
            }

            // Check whether the table is empty
            String countSql =
                    "SELECT COUNT(*) FROM person";

            int count = 0;

            try (Statement statement =
                         connection.createStatement();

                 ResultSet rs =
                         statement.executeQuery(countSql)) {

                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

            // Add test data only when table is empty
            if (count == 0) {

                insertPerson(
                        connection,
                        "John",
                        "Smith",
                        "03-1234567",
                        "050-1234567"
                );

                insertPerson(
                        connection,
                        "David",
                        "Cohen",
                        "03-7654321",
                        "052-7654321"
                );

                insertPerson(
                        connection,
                        "Michael",
                        "Levi",
                        "03-5555555",
                        "053-5555555"
                );

                insertPerson(
                        connection,
                        "John",
                        "Cohen",
                        "03-8888888",
                        "054-8888888"
                );

                System.out.println(
                        "Test people inserted into database."
                );
            }

            System.out.println(
                    "H2 database initialized successfully."
            );
        }
    }

    // ============================================================
    // INSERT PERSON
    // ============================================================

    private static void insertPerson(
            Connection connection,
            String firstName,
            String lastName,
            String phone,
            String cellphone)
            throws SQLException {

        String sql =
                "INSERT INTO person " +
                "(first_name, last_name, phone, cellphone) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, cellphone);

            ps.executeUpdate();
        }
    }

    // ============================================================
    // GET ALL PEOPLE
    // ============================================================

    private static List<Person> getAllPeople()
            throws SQLException {

        List<Person> people =
                new ArrayList<>();

        String sql =
                "SELECT first_name, last_name, phone, cellphone " +
                "FROM person " +
                "ORDER BY id";

        try (Connection connection =
                     DriverManager.getConnection(
                             DB_URL,
                             DB_USER,
                             DB_PASSWORD);

             PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Person person =
                        new Person(
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("phone"),
                                rs.getString("cellphone")
                        );

                people.add(person);
            }
        }

        return people;
    }

    // ============================================================
    // SEARCH PEOPLE
    //
    // Searches first_name OR last_name
    // ============================================================

    private static List<Person> searchPeople(
            String name)
            throws SQLException {

        List<Person> people =
                new ArrayList<>();

        String sql =
                "SELECT first_name, last_name, phone, cellphone " +
                "FROM person " +
                "WHERE LOWER(first_name) LIKE LOWER(?) " +
                "OR LOWER(last_name) LIKE LOWER(?) " +
                "ORDER BY id";

        try (Connection connection =
                     DriverManager.getConnection(
                             DB_URL,
                             DB_USER,
                             DB_PASSWORD);

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            String searchValue =
                    "%" + name + "%";

            ps.setString(1, searchValue);
            ps.setString(2, searchValue);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    Person person =
                            new Person(
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("phone"),
                                    rs.getString("cellphone")
                            );

                    people.add(person);
                }
            }
        }

        return people;
    }

    // ============================================================
    // CONVERT PERSON LIST TO JSON
    // ============================================================

    private static String peopleToJson(
            List<Person> people) {

        StringBuilder json =
                new StringBuilder();

        json.append("[");

        for (int i = 0; i < people.size(); i++) {

            Person person = people.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append("{");

            json.append("\"firstName\":\"")
                    .append(escapeJson(person.firstName))
                    .append("\",");

            json.append("\"lastName\":\"")
                    .append(escapeJson(person.lastName))
                    .append("\",");

            json.append("\"phone\":\"")
                    .append(escapeJson(person.phone))
                    .append("\",");

            json.append("\"cellphone\":\"")
                    .append(escapeJson(person.cellphone))
                    .append("\"");

            json.append("}");
        }

        json.append("]");

        return json.toString();
    }

    // ============================================================
    // GET QUERY PARAMETER
    //
    // Example:
    //
    // ?name=John
    //
    // returns:
    //
    // John
    // ============================================================

    private static String getQueryParameter(
            String query,
            String parameterName) {

        if (query == null) {
            return null;
        }

        String[] parameters =
                query.split("&");

        for (String parameter : parameters) {

            String[] pair =
                    parameter.split("=", 2);

            if (pair.length == 2) {

                String key =
                        URLDecoder.decode(
                                pair[0],
                                StandardCharsets.UTF_8
                        );

                if (key.equals(parameterName)) {

                    return URLDecoder.decode(
                            pair[1],
                            StandardCharsets.UTF_8
                    );
                }
            }
        }

        return null;
    }

    // ============================================================
    // JSON ESCAPE
    // ============================================================

    private static String escapeJson(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    // ============================================================
    // CORS
    // ============================================================

    private static void handleCors(
            HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Methods",
                        "GET, OPTIONS"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Headers",
                        "Content-Type"
                );
    }

    // ============================================================
    // SEND RESPONSE
    // ============================================================

    private static void send(
            HttpExchange exchange,
            String response,
            String contentType)
            throws IOException {

        send(
                exchange,
                response,
                contentType,
                200
        );
    }

    // ============================================================
    // SEND RESPONSE WITH HTTP STATUS
    // ============================================================

    private static void send(
            HttpExchange exchange,
            String response,
            String contentType,
            int statusCode)
            throws IOException {

        handleCors(exchange);

        // Handle OPTIONS
        if ("OPTIONS".equalsIgnoreCase(
                exchange.getRequestMethod())) {

            exchange.sendResponseHeaders(
                    204,
                    -1
            );

            exchange.close();

            return;
        }

        byte[] responseBytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        contentType +
                        "; charset=utf-8"
                );

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        try (OutputStream os =
                     exchange.getResponseBody()) {

            os.write(responseBytes);
        }
    }

    // ============================================================
    // METHOD CHECK
    // ============================================================

    private static boolean isGetOrOptions(
            HttpExchange exchange) {

        String method =
                exchange.getRequestMethod();

        return "GET".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method);
    }

    // ============================================================
    // 405 METHOD NOT ALLOWED
    // ============================================================

    private static void sendMethodNotAllowed(
            HttpExchange exchange)
            throws IOException {

        String json =
                "{ \"error\": \"Method Not Allowed\" }";

        send(
                exchange,
                json,
                "application/json",
                405
        );
    }
}
```

### H2 dependency

Because this version uses JDBC, you need the H2 driver in your project.

If you're using Maven, add:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
</dependency>
```

### Database structure

The program automatically creates:

```text
data/
   people.mv.db
```

with this table:

```text
PERSON
--------------------------------
ID
FIRST_NAME
LAST_NAME
PHONE
CELLPHONE
```

The first time you run it, it inserts four test records.

### Service 1 — all records

Open:

```text
http://localhost:8080/person
```

Result:

```json
[
  {
    "firstName": "John",
    "lastName": "Smith",
    "phone": "03-1234567",
    "cellphone": "050-1234567"
  },
  {
    "firstName": "David",
    "lastName": "Cohen",
    "phone": "03-7654321",
    "cellphone": "052-7654321"
  },
  {
    "firstName": "Michael",
    "lastName": "Levi",
    "phone": "03-5555555",
    "cellphone": "053-5555555"
  },
  {
    "firstName": "John",
    "lastName": "Cohen",
    "phone": "03-8888888",
    "cellphone": "054-8888888"
  }
]
```

### Service 2 — search

Search by first name:

```text
http://localhost:8080/person/search?name=John
```

Returns both John records:

```json
[
  {
    "firstName": "John",
    "lastName": "Smith",
    "phone": "03-1234567",
    "cellphone": "050-1234567"
  },
  {
    "firstName": "John",
    "lastName": "Cohen",
    "phone": "03-8888888",
    "cellphone": "054-8888888"
  }
]
```

Search by last name:

```text
http://localhost:8080/person/search?name=Cohen
```

Returns:

```json
[
  {
    "firstName": "David",
    "lastName": "Cohen",
    "phone": "03-7654321",
    "cellphone": "052-7654321"
  },
  {
    "firstName": "John",
    "lastName": "Cohen",
    "phone": "03-8888888",
    "cellphone": "054-8888888"
  }
]
```

It also supports partial searches:

```text
/person/search?name=coh
```

will find `Cohen`, and the search is **case-insensitive**.

One important design point: I kept this as your original **plain Java `HttpServer`**, rather than changing it to Spring. So you only need the H2 JAR/dependency and your existing Java server architecture stays intact.
