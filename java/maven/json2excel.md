
# java json to excel

-----

Absolutely! Here's a step-by-step Java program that reads a JSON file and writes its contents to an Excel spreadsheet using the popular libraries **Jackson** (for JSON parsing) and **Apache POI** (for Excel file creation).

### 🛠 Required Dependencies
Make sure your `pom.xml` includes these:

```xml
<dependencies>
  <!-- JSON Parser -->
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
  </dependency>

  <!-- Apache POI for Excel -->
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.3</version>
  </dependency>
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
  </dependency>
</dependencies>
```

### 📄 Sample JSON Format

Assume the JSON looks like this:

```json
[
  {"name": "Alice", "age": 30, "city": "London"},
  {"name": "Bob", "age": 25, "city": "Paris"}
]
```

### 🧩 Java Program

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

public class JsonToExcel {
    public static void main(String[] args) throws Exception {
        // Load JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootArray = mapper.readTree(new File("data.json"));

        // Create Excel Workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Add Header Row
        Row header = sheet.createRow(0);
        Iterator<String> fieldNames = rootArray.get(0).fieldNames();
        int colIndex = 0;
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            header.createCell(colIndex++).setCellValue(field);
        }

        // Add Data Rows
        int rowIndex = 1;
        for (JsonNode objNode : rootArray) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for (Iterator<String> it = objNode.fieldNames(); it.hasNext(); ) {
                String field = it.next();
                row.createCell(cellIndex++).setCellValue(objNode.get(field).asText());
            }
        }

        // Save to file
        try (FileOutputStream fileOut = new FileOutputStream("output.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Excel file created: output.xlsx");
    }
}
```

Let me know if your JSON structure is more complex—nested arrays, custom fields, etc.—and I can tailor the code to suit it!

-----

Got it! Here's an updated version of the code tailored to Java 1.8. The core logic remains the same, but it's fully compatible without any newer language features like `var` or enhanced `try-with-resources`.

### ☕ Java 1.8-Compatible Program

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class JsonToExcel {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        try {
            JsonNode rootArray = mapper.readTree(new File("data.json"));

            // Header row
            Row header = sheet.createRow(0);
            Iterator<String> fieldNames = rootArray.get(0).fieldNames();
            int colIndex = 0;
            while (fieldNames.hasNext()) {
                header.createCell(colIndex++).setCellValue(fieldNames.next());
            }

            // Data rows
            int rowIndex = 1;
            for (JsonNode objNode : rootArray) {
                Row row = sheet.createRow(rowIndex++);
                Iterator<String> fields = objNode.fieldNames();
                int cellIndex = 0;
                while (fields.hasNext()) {
                    String field = fields.next();
                    row.createCell(cellIndex++).setCellValue(objNode.get(field).asText());
                }
            }

            FileOutputStream fileOut = new FileOutputStream("output.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Excel file created: output.xlsx");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### ✅ Fully Compatible with Java 1.8

Let me know if you want this to work with nested JSON objects, write multiple sheets, or include cell styles. We can definitely level it up!
