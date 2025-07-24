To create an Excel document in Java, the most widely used and powerful library is **Apache POI**. It supports both `.xls` (HSSF) and `.xlsx` (XSSF) formats. Here's a quick guide to get you started:

-----

### 🛠 Step-by-Step: Create Excel File Using Apache POI

#### 1. **Add Apache POI to Your Project**
If you're using Maven, include these dependencies in your `pom.xml`:

```xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi</artifactId>
  <version>5.2.2</version>
</dependency>
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>5.2.2</version>
</dependency>
```

---

#### 2. **Basic Java Code to Create an Excel File**

```java
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCreator {
    public static void main(String[] args) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employees");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Department");

            // Data row
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue(101);
            row1.createCell(1).setCellValue("Alice");
            row1.createCell(2).setCellValue("HR");

            // Save to file
            try (FileOutputStream fileOut = new FileOutputStream("employees.xlsx")) {
                workbook.write(fileOut);
            }

            System.out.println("Excel file created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 🎨 Optional Styling Example

You can style cells using `CellStyle` and `Font`:

```java
CellStyle style = workbook.createCellStyle();
Font font = workbook.createFont();
font.setBold(true);
font.setFontHeightInPoints((short) 14);
style.setFont(font);
header.getCell(0).setCellStyle(style);
```

---

### 📚 More Advanced Features

- Add multiple sheets
- Format cells (currency, date, etc.)
- Insert formulas
- Add charts and images

For a deeper dive, check out this [Apache POI tutorial](https://genuinecoder.com/how-to-create-excel-xlsx-spreadsheet-files-in-java/).

Would you like help adding formulas, charts, or styling to your Excel file next?

-----

Sure! Here's a complete Java program that creates an Excel document named `employees.xlsx` with a simple table of employee data:

---

### ✅ Java Program to Create Excel Document

```java
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDocumentCreator {
    public static void main(String[] args) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet
        Sheet sheet = workbook.createSheet("Employees");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Department");

        // Create sample data rows
        Object[][] employeeData = {
            {101, "Alice", "HR"},
            {102, "Bob", "Finance"},
            {103, "Charlie", "IT"},
            {104, "Diana", "Marketing"}
        };

        int rowNum = 1;
        for (Object[] emp : employeeData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((Integer) emp[0]);
            row.createCell(1).setCellValue((String) emp[1]);
            row.createCell(2).setCellValue((String) emp[2]);
        }

        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("employees.xlsx")) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Excel file 'employees.xlsx' created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 📦 Before Running the Code

Make sure you have Apache POI in your project. If you're not using Maven, download the required JARs from [Apache POI Downloads](https://poi.apache.org/download.html) and add them to your classpath:

- `poi-5.x.x.jar`
- `poi-ooxml-5.x.x.jar`
- `commons-collections4-4.x.jar`
- `xmlbeans-5.x.jar`
- `ooxml-schemas-1.x.jar`

---

Would you like to add styling, formulas, or export more complex data next?

-----

To create a Word document in Java, the most popular and free option is using **Apache POI**, specifically the `XWPF` classes for `.docx` files. Here's a simple example to get you started:

---

### 📝 Java Program to Create a Word Document (`.docx`)

#### 1. **Add Apache POI Dependency**

If you're using Maven, add this to your `pom.xml`:

```xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>5.2.3</version>
</dependency>
```

---

#### 2. **Java Code Example**

```java
import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.*;

public class WordDocumentCreator {
    public static void main(String[] args) {
        try {
            // Create a new document
            XWPFDocument document = new XWPFDocument();

            // Create a paragraph
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.LEFT);

            // Create a run to hold the text
            XWPFRun run = paragraph.createRun();
            run.setText("Hello, this is a Word document created using Apache POI!");
            run.setFontSize(14);
            run.setBold(true);
            run.setFontFamily("Arial");

            // Save the document
            try (FileOutputStream out = new FileOutputStream("example.docx")) {
                document.write(out);
                System.out.println("Word document created successfully.");
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 🧰 What You Can Add Next

- **Multiple paragraphs**
- **Tables**
- **Images**
- **Headers/footers**
- **Styles and formatting**

For more advanced features like inserting images or tables, check out [Baeldung’s Apache POI Word tutorial](https://www.baeldung.com/java-microsoft-word-with-apache-poi).

Would you like to add a table or image to your Word document next?

---- 
