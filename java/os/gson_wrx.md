
# gson

Below is a clean, practical example in Java using **Google Gson** to **read** and **write** a JSON file that contains:

- server name  
- folders path (as a list)  
- filename  
- entry date  
- entry time  

The structure is flexible, and you can adapt it to your needs.

---

# 📁 JSON Structure (example)

```json
{
  "serverName": "MainServer01",
  "folders": [
    "C:/data/input",
    "C:/data/output"
  ],
  "fileName": "report.txt",
  "entryDate": "2026-01-28",
  "entryTime": "18:15"
}
```

---

# 🧱 Java Model Class

```java
public class ServerConfig {
    private String serverName;
    private List<String> folders;
    private String fileName;
    private String entryDate;
    private String entryTime;

    // Getters and setters
    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    public List<String> getFolders() { return folders; }
    public void setFolders(List<String> folders) { this.folders = folders; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }

    public String getEntryTime() { return entryTime; }
    public void setEntryTime(String entryTime) { this.entryTime = entryTime; }
}
```

---

# 📖 Method to Read JSON (Gson)

```java
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;

public static ServerConfig readConfig(String filePath) {
    Gson gson = new Gson();
    try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
        return gson.fromJson(reader, ServerConfig.class);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
```

---

# ✍️ Method to Write JSON (Gson)

```java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

public static void writeConfig(String filePath, ServerConfig config) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (FileWriter writer = new FileWriter(filePath)) {
        gson.toJson(config, writer);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

---

# 🚀 Example Usage

```java
public static void main(String[] args) {

    // Create config object
    ServerConfig config = new ServerConfig();
    config.setServerName("MainServer01");
    config.setFolders(List.of("C:/data/input", "C:/data/output"));
    config.setFileName("report.txt");
    config.setEntryDate("2026-01-28");
    config.setEntryTime("18:15");

    // Write JSON
    writeConfig("config.json", config);

    // Read JSON
    ServerConfig loaded = readConfig("config.json");
    System.out.println("Loaded server: " + loaded.getServerName());
}
```

---

If you want, I can also help you:

- validate the JSON  
- add error handling  
- support nested objects  
- generate the JSON schema  

Just tell me what direction you want to take next.
