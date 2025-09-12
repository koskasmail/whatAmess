# 002_doc2docx_xls2xlsx.md


You're asking for a powerful cross-platform solution: converting legacy Microsoft Office formats (`.doc`, `.xls`) to modern ones (`.docx`, `.xlsx`) using both **JavaScript** and **Java with Maven**. Here's how to tackle it properly:

---

## ⚙️ Part 1: JavaScript-Based Conversion (Client or Node.js)

Unfortunately, **JavaScript cannot directly convert `.doc` to `.docx` or `.xls` to `.xlsx`** in the browser or Node.js due to format complexity and lack of native support. However, you can use **third-party APIs** or **server-side tools**:

### ✅ Option: Use [Spire.Office for JavaScript](https://www.e-iceblue.com/Introduce/free-office-for-javascript.html) (limited free version)

```js
// Pseudo-code: Requires server-side setup
const spire = require('spire-office-js');

const doc = spire.load('input.doc');
doc.saveAs('output.docx');

const xls = spire.load('input.xls');
xls.saveAs('output.xlsx');
```

> ⚠️ Limitations: Free version has restrictions (e.g., 3 pages, 200 rows). For full conversion, you'd need a paid license.

---

## ⚙️ Part 2: Java-Based Conversion with Maven

Java is much better suited for this task. You can use **Aspose** or **Docx4j + Apache POI**. Here's a full Maven-based solution using **Aspose.Words** and **Aspose.Cells**:

### 🧱 `pom.xml` Dependencies

```xml
<dependencies>
  <!-- Aspose.Words for DOC to DOCX -->
  <dependency>
    <groupId>com.aspose</groupId>
    <artifactId>aspose-words</artifactId>
    <version>24.8</version>
  </dependency>

  <!-- Aspose.Cells for XLS to XLSX -->
  <dependency>
    <groupId>com.aspose</groupId>
    <artifactId>aspose-cells</artifactId>
    <version>24.8</version>
  </dependency>
</dependencies>
```

> 💡 You’ll need a free trial or commercial license from [Aspose](https://www.aspose.com/).

---

### 🧪 Java Code Example

```java
import com.aspose.words.Document;
import com.aspose.cells.Workbook;

public class OfficeConverter {
    public static void main(String[] args) throws Exception {
        // Convert DOC to DOCX
        Document doc = new Document("input.doc");
        doc.save("output.docx");

        // Convert XLS to XLSX
        Workbook workbook = new Workbook("input.xls");
        workbook.save("output.xlsx");
    }
}
```

---

## 🧠 Alternative: LibreOffice Command-Line (Server-Side)

If you prefer open-source tools, you can use LibreOffice in headless mode:

```bash
soffice --headless --convert-to docx input.doc
soffice --headless --convert-to xlsx input.xls
```

You can invoke this from Java using `ProcessBuilder`.

---

## ✅ Summary

| Platform     | DOC to DOCX | XLS to XLSX | Notes |
|--------------|-------------|-------------|-------|
| JavaScript   | ❌ (not natively) | ❌ | Use APIs or server tools |
| Java + Maven | ✅ Aspose or LibreOffice | ✅ Aspose or LibreOffice | Best for automation |
| LibreOffice  | ✅ | ✅ | Free, CLI-based |

Would you like me to generate a full Maven project structure or help you set up the LibreOffice command integration in Java?

------