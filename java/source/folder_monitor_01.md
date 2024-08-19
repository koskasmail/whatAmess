<a name="topage"></a>

# folder_monitor_01

To achieve this in Java, you'll need to follow these steps:

1. **Parse the JSON File**: Load and parse the JSON file (`path.json`) to extract the paths.
2. **Get Folder Information**: For each path, retrieve the folder status, update date, total number of subfolders (level 1), created date, created time, update date, and update time.
3. **Update the JSON**: Update the JSON structure with the new information.

Here’s an outline of the code:

### Dependencies
Make sure you have the following dependencies in your `pom.xml` if you're using Maven:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.3</version>
</dependency>
```

### Sample JSON (`path.json`)
Assume the JSON file looks something like this:

```json
{
    "paths": [
        "/path/to/folder1",
        "/path/to/folder2"
    ]
}
```

### Java Code

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FolderInfoUpdater {

    public static void main(String[] args) {
        try {
            // Load and parse the JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File("path.json"));

            // Get the array of paths from JSON
            JsonNode pathsNode = rootNode.get("paths");

            // Prepare a list to store folder information
            List<JsonNode> updatedPaths = new ArrayList<>();

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

                    // Create updated JSON structure for this path
                    ObjectNode updatedPathNode = mapper.createObjectNode();
                    updatedPathNode.put("path", pathStr);
                    updatedPathNode.put("status", "exists");
                    updatedPathNode.put("total_subfolders", subfolderCount);
                    updatedPathNode.put("created_date", createdDate);
                    updatedPathNode.put("created_time", createdTime);
                    updatedPathNode.put("update_date", updateDate);
                    updatedPathNode.put("update_time", updateTime);

                    updatedPaths.add(updatedPathNode);
                } else {
                    // Path doesn't exist or isn't a directory
                    ObjectNode missingPathNode = mapper.createObjectNode();
                    missingPathNode.put("path", pathStr);
                    missingPathNode.put("status", "missing");
                    updatedPaths.add(missingPathNode);
                }
            }

            // Replace the original paths array with updated information
            ((ObjectNode) rootNode).putArray("paths").addAll(updatedPaths);

            // Save the updated JSON back to the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("updated_path.json"), rootNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Explanation

- **Parsing JSON**: We use Jackson's `ObjectMapper` to read the JSON file and retrieve the list of paths.
- **Fetching Folder Attributes**: For each folder path, the code checks if it exists and whether it's a directory. It then retrieves the folder's creation and last modified times.
- **Counting Subfolders**: The code counts the number of immediate subfolders (level 1) within each folder.
- **Updating JSON**: The folder information is added to a new JSON structure, and the original JSON is updated and saved as `updated_path.json`.

### Output

The updated JSON file (`updated_path.json`) might look something like this:

```json
{
    "paths": [
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

This code should work for most cases where you need to update folder information from a list of paths in a JSON file.


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
