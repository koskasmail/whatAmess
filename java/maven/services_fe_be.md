Here’s an example of how to create a simple system where:

1. **Frontend**: HTML, CSS, and JavaScript are used to send a request and display JSON data fetched from a Java service.
2. **Backend**: A Java service serves a JSON response.

---

### 1. **Java Service (Backend)**
You can use a simple Java Spring Boot application to create the backend service.

#### Steps:
1. Add Spring Boot dependencies to your `pom.xml` for a Maven project:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

2. Create a `Controller` class to serve JSON data:
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    @GetMapping("/api/data")
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Hello from Java Service");
        data.put("status", "success");
        return data;
    }
}
```

3. Run the Spring Boot application. It will start on `http://localhost:8080`. The endpoint `http://localhost:8080/api/data` will return JSON data like:
```json
{
  "message": "Hello from Java Service",
  "status": "success"
}
```

---

### 2. **Frontend (HTML, CSS, JavaScript)**
Create a simple webpage that fetches and displays the JSON data.

#### HTML:
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Fetch JSON Example</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      text-align: center;
    }
    .data-container {
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <h1>JSON Data from Java Service</h1>
  <button id="fetchButton">Fetch Data</button>
  <div class="data-container" id="dataContainer"></div>

  <script>
    document.getElementById('fetchButton').addEventListener('click', () => {
      fetch('http://localhost:8080/api/data')
        .then(response => response.json())
        .then(data => {
          const container = document.getElementById('dataContainer');
          container.innerHTML = `<p>Message: ${data.message}</p><p>Status: ${data.status}</p>`;
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          const container = document.getElementById('dataContainer');
          container.innerHTML = `<p style="color: red;">Error fetching data.</p>`;
        });
    });
  </script>
</body>
</html>
```

---

### 3. **How it Works**
1. The Java service serves JSON at the `/api/data` endpoint.
2. The frontend has a "Fetch Data" button that triggers a JavaScript function using the `fetch()` API.
3. On clicking the button, the JavaScript fetches data from the backend and displays it dynamically in the web page.

---

### 4. **Running the System**
- **Backend**: Run your Java application and ensure the service is running at `http://localhost:8080/api/data`.
- **Frontend**: Open the HTML file in a browser. Click the "Fetch Data" button to see the JSON data displayed.

You can expand this further, for instance by adding forms, additional APIs, or integrating a database. Let me know how I can help! 🚀
