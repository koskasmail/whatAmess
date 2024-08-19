<a name="topage"></a>

# folder_monitor_02

To accomplish this task in Java, you'll need to:

1. **Read the JSON file** (`path.json`) to extract the folder paths.
2. **Check each folder** for its status and retrieve its metadata (e.g., created date, update date, etc.).
3. **Insert the results** into a new JSON file (`output.json`).

### Dependencies
If you're using Maven, include Jackson dependency to work with JSON:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.3</version>
</dependency>
```

### Sample Input JSON (`path.json`)
Assume your input JSON looks like this:

```json
{
    "paths": [
        "/path/to/folder1",
        "/path/to/folder2"
    ]
}
```

### Java Code

Here's a Java program that reads the `path.json`, processes each folder, and writes the results to `output.json`:

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FolderInfoExtractor {

    public static void main(String[] args) {
        try {
            // Load and parse the JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File("path.json"));

            // Get the array of paths from JSON
            JsonNode pathsNode = rootNode.get("paths");

            // Prepare a list to store folder information
            List<JsonNode> folderInfoList = new ArrayList<>();

            for (JsonNode pathNode : pathsNode) {
                String pathStr = pathNode.asText();
                Path folderPath = Path.of(pathStr);

                if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                    // Get folder attributes
                    BasicFileAttributes attrs = Files.readAttributes(folderPath, BasicFileAttributes.class);

                    // Get created and last modified times
                    String createdDate = attrs.creationTime().toString().split("T")[0];
                    String createdTime = attrs.creationTime().toString().split("T")[1];
                    String updateDate = attrs.lastModifiedTime().toString().split("T")[0];
                    String updateTime = attrs.lastModifiedTime().toString().split("T")[1];

                    // Count subfolders at level 1
                    long subfolderCount = Files.list(folderPath)
                            .filter(Files::isDirectory)
                            .count();

                    // Create a JSON structure for this path
                    ObjectNode folderInfoNode = mapper.createObjectNode();
                    folderInfoNode.put("path", pathStr);
                    folderInfoNode.put("status", "exists");
                    folderInfoNode.put("total_subfolders", subfolderCount);
                    folderInfoNode.put("created_date", createdDate);
                    folderInfoNode.put("created_time", createdTime);
                    folderInfoNode.put("update_date", updateDate);
                    folderInfoNode.put("update_time", updateTime);

                    folderInfoList.add(folderInfoNode);
                } else {
                    // Path doesn't exist or isn't a directory
                    ObjectNode folderInfoNode = mapper.createObjectNode();
                    folderInfoNode.put("path", pathStr);
                    folderInfoNode.put("status", "missing");
                    folderInfoList.add(folderInfoNode);
                }
            }

            // Create a root node for the output JSON
            ObjectNode outputRootNode = mapper.createObjectNode();
            outputRootNode.putArray("folders").addAll(folderInfoList);

            // Save the output JSON to a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), outputRootNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Explanation

- **Parsing JSON**: We use Jackson's `ObjectMapper` to read the `path.json` file and retrieve the list of paths.
- **Folder Metadata**: For each folder path, the program checks if the folder exists and retrieves its metadata using `BasicFileAttributes`.
- **Subfolder Counting**: The code counts the number of subfolders at level 1 using `Files.list()` and filters only directories.
- **Building Output JSON**: For each folder, a JSON node is created with the required information and added to the `folders` array in the `output.json`.
- **Handling Missing Folders**: If a path doesn't exist or isn't a directory, it's marked as "missing" in the output JSON.

### Sample Output JSON (`output.json`)

After running the program, the `output.json` might look like this:

```json
{
    "folders": [
        {
            "path": "/path/to/folder1",
            "status": "exists",
            "total_subfolders": 5,
            "created_date": "2022-01-01",
            "created_time": "10:00:00",
            "update_date": "2023-01-01",
            "update_time": "12:00:00"
        },
        {
            "path": "/path/to/folder2",
            "status": "missing"
        }
    ]
}
```

This code will correctly process the folder paths specified in `path.json`, gather the necessary metadata, and output the results into `output.json`.

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
