Here’s the **cleanest, fully working plan** to add a new static page **userInfo.html** with its own **CSS**, **JS**, and **backend services** for:

- **Load users** (GET)
- **Create user** (POST)
- **Update user** (PUT)
- **Delete user** (DELETE)

All built **exactly like your existing Java HTTP server**, using the same structure and style.

I’ll give you:

1. **Frontend files** (HTML + CSS + JS)  
2. **Backend handlers** (Java)  
3. **Server wiring**  
4. **Temporary in‑memory user storage**

Everything is modular and matches your current project.

---

# ⭐ 1. userInfo.html (frontend UI)

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Info</title>
    <link rel="stylesheet" href="userInfo.css">
</head>
<body>

<h1>User Manager</h1>

<div class="form">
    <input id="name" placeholder="Name">
    <input id="email" placeholder="Email">
    <button onclick="createUser()">Add User</button>
</div>

<table>
    <thead>
        <tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>
    </thead>
    <tbody id="userTable"></tbody>
</table>

<script src="userInfo.js"></script>
</body>
</html>
```

---

# ⭐ 2. userInfo.css (simple clean UI)

```css
body {
    font-family: Arial;
    padding: 20px;
    background: #f4f4f4;
}

h1 {
    margin-bottom: 20px;
}

.form input {
    padding: 8px;
    margin-right: 5px;
}

button {
    padding: 8px 12px;
    cursor: pointer;
}

table {
    width: 100%;
    margin-top: 20px;
    background: white;
    border-collapse: collapse;
}

th, td {
    padding: 10px;
    border-bottom: 1px solid #ddd;
}
```

---

# ⭐ 3. userInfo.js (frontend logic)

```javascript
async function loadUsers() {
    const res = await fetch("/users");
    const users = await res.json();

    const table = document.getElementById("userTable");
    table.innerHTML = "";

    users.forEach(u => {
        table.innerHTML += `
            <tr>
                <td>${u.id}</td>
                <td><input value="${u.name}" id="name_${u.id}"></td>
                <td><input value="${u.email}" id="email_${u.id}"></td>
                <td>
                    <button onclick="updateUser(${u.id})">Update</button>
                    <button onclick="deleteUser(${u.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function createUser() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;

    await fetch("/users", {
        method: "POST",
        body: JSON.stringify({ name, email })
    });

    loadUsers();
}

async function updateUser(id) {
    const name = document.getElementById("name_" + id).value;
    const email = document.getElementById("email_" + id).value;

    await fetch("/users?id=" + id, {
        method: "PUT",
        body: JSON.stringify({ name, email })
    });

    loadUsers();
}

async function deleteUser(id) {
    await fetch("/users?id=" + id, { method: "DELETE" });
    loadUsers();
}

loadUsers();
```

---

# ⭐ 4. Java backend — temporary in‑memory storage

Create a new class:

## **UserDatabase.java**
```java
package data;

import java.util.*;

public class UserDatabase {

    private static final Map<Integer, UserRecord> users = new HashMap<>();
    private static int nextId = 1;

    public static List<UserRecord> getAll() {
        return new ArrayList<>(users.values());
    }

    public static void add(String name, String email) {
        users.put(nextId, new UserRecord(nextId, name, email));
        nextId++;
    }

    public static void update(int id, String name, String email) {
        if (users.containsKey(id)) {
            users.put(id, new UserRecord(id, name, email));
        }
    }

    public static void delete(int id) {
        users.remove(id);
    }
}
```

## **UserRecord.java**
```java
package data;

public class UserRecord {
    public int id;
    public String name;
    public String email;

    public UserRecord(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
```

---

# ⭐ 5. Java backend — REST handler

## **UserHandler.java**
```java
package handlers;

import com.sun.net.httpserver.*;
import data.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class UserHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();

        switch (method) {
            case "GET": handleGet(ex); break;
            case "POST": handlePost(ex); break;
            case "PUT": handlePut(ex); break;
            case "DELETE": handleDelete(ex); break;
        }
    }

    private void handleGet(HttpExchange ex) throws IOException {
        String json = new com.google.gson.Gson().toJson(UserDatabase.getAll());
        send(ex, 200, json);
    }

    private void handlePost(HttpExchange ex) throws IOException {
        String body = read(ex);
        UserRecord u = new com.google.gson.Gson().fromJson(body, UserRecord.class);
        UserDatabase.add(u.name, u.email);
        send(ex, 200, "OK");
    }

    private void handlePut(HttpExchange ex) throws IOException {
        int id = Integer.parseInt(getQuery(ex, "id"));
        String body = read(ex);
        UserRecord u = new com.google.gson.Gson().fromJson(body, UserRecord.class);
        UserDatabase.update(id, u.name, u.email);
        send(ex, 200, "OK");
    }

    private void handleDelete(HttpExchange ex) throws IOException {
        int id = Integer.parseInt(getQuery(ex, "id"));
        UserDatabase.delete(id);
        send(ex, 200, "OK");
    }

    private String read(HttpExchange ex) throws IOException {
        return new BufferedReader(new InputStreamReader(ex.getRequestBody()))
                .lines().collect(Collectors.joining());
    }

    private String getQuery(HttpExchange ex, String key) {
        String q = ex.getRequestURI().getQuery();
        for (String p : q.split("&")) {
            String[] kv = p.split("=");
            if (kv[0].equals(key)) return kv[1];
        }
        return null;
    }

    private void send(HttpExchange ex, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
}
```

---

# ⭐ 6. Add routes to your main server

Modify **MyOsMain.java**:

```java
server.createContext("/userInfo", new StaticFileHandler(root + "userInfo.html"));
server.createContext("/userInfo.css", new StaticFileHandler(root + "userInfo.css"));
server.createContext("/userInfo.js", new StaticFileHandler(root + "userInfo.js"));

server.createContext("/users", new UserHandler());
```

---

# 🎉 Done — You now have:

- A **new static page** `/userInfo`
- A **frontend UI** for CRUD operations
- A **backend REST API**
- A **temporary in‑memory database**
- Full **create / update / delete / list** functionality

---

If you want, I can also add:

- Search bar
- Pagination
- Better UI design
- Persistent storage using JSON file

Which upgrade do you want next?