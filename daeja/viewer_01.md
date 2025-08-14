
# Deaja 


#### viewer_01

Creating something like **IBM Daeja ViewONE Virtual** using a **Content Navigator plugin project** involves building a custom plugin for IBM Content Navigator (ICN) that integrates a document viewer with advanced capabilities—such as annotations, redactions, and multi-format support—similar to what Daeja ViewONE offers.

Let’s walk through a **step-by-step guide** to build a simplified version of such a viewer plugin.

---

## 🧩 Overview

**Goal**: Create a Content Navigator plugin that embeds a custom document viewer (e.g., PDF.js or a third-party viewer) into ICN, allowing users to view documents stored in IBM FileNet or other repositories.

**Tools & Technologies**:
- IBM Content Navigator (ICN)
- ICN Plugin SDK
- Java (for backend logic)
- JavaScript/Dojo (for frontend UI)
- PDF.js or other viewer libraries
- Eclipse or IntelliJ (IDE)
- Apache Maven (build tool)

---

## 🛠️ Step-by-Step Guide

### 1. **Set Up Development Environment**
- Install **IBM Content Navigator Plugin SDK**
- Set up **Eclipse IDE** with the ICN plugin development environment
- Create a new **ICN Plugin Project**

```bash
mvn archetype:generate -DarchetypeGroupId=com.ibm.ecm -DarchetypeArtifactId=icn-plugin-archetype
```

### 2. **Create the Plugin Skeleton**
- Define your plugin class: `CustomViewerPlugin.java`
- Register the plugin in ICN using `plugin.xml`

```xml
<plugin>
  <id>custom.viewer.plugin</id>
  <name>Custom Viewer Plugin</name>
  <description>Embeds a custom document viewer</description>
</plugin>
```

### 3. **Add a Custom Feature or Action**
- Create a new **feature** or **menu action** that opens the viewer
- Use Dojo to define the UI component

```javascript
define([
  "dojo/_base/declare",
  "ecm/widget/layout/CommonActionsHandler"
], function(declare, CommonActionsHandler) {
  return declare("custom.viewer.ViewerAction", [CommonActionsHandler], {
    execute: function(repository, items, callback) {
      // Open viewer dialog
    }
  });
});
```

### 4. **Integrate a Viewer (e.g., PDF.js)**
- Add PDF.js or another viewer to your plugin’s web resources
- Create a Dojo widget that loads the viewer in an iframe or canvas

```html
<iframe src="pdfjs/web/viewer.html?file=yourDocument.pdf" width="100%" height="600px"></iframe>
```

### 5. **Fetch Document from Repository**
- Use ICN services to retrieve the document content
- Convert it to a format compatible with your viewer (e.g., PDF)

```java
Document doc = getDocumentFromRepository(docId);
InputStream contentStream = doc.getContentStream();
```

- Serve the content via a servlet or REST endpoint in your plugin

### 6. **Add Viewer Controls (Optional)**
- Implement annotation, zoom, rotate, and other controls using viewer APIs
- Store annotations in ICN metadata or a separate repository

### 7. **Deploy the Plugin**
- Package the plugin as a JAR
- Deploy it to ICN via the admin desktop
- Assign it to desktops or roles

---

## 🧪 Testing & Validation
- Test with various document types (PDF, TIFF, DOCX)
- Validate performance and compatibility
- Ensure security (e.g., access control, redaction)

---

## 🚀 Advanced Enhancements
- Add support for annotations and redactions
- Integrate with Daeja APIs if licensed
- Enable multi-page navigation and search
- Add audit logging for viewer actions

---

Would you like a sample plugin structure or code snippets for specific parts like the viewer integration or document retrieval?

------


#### viewer_02

Creating a **document viewer that supports multiple formats** (like `.txt`, `.html`, `.mht`, `.docx`, `.xlsx`, `.pptx`, `.pdf`, etc.) and displays content **directly in the browser without using an `<iframe>`** is a challenging but achievable task. It involves converting various file types into **HTML or canvas-rendered content** using JavaScript libraries and backend services.

Here’s a **step-by-step guide** to build such a viewer:

---

## 🧩 Architecture Overview

| Component        | Role                                                                 |
|------------------|----------------------------------------------------------------------|
| Frontend (JavaScript) | Renders documents using libraries like PDF.js, Mammoth.js, SheetJS |
| Backend (Java/Node.js) | Converts unsupported formats to HTML or PDF                     |
| ICN Plugin        | Embeds the viewer into IBM Content Navigator                        |

---

## 🛠️ Step-by-Step Implementation

### 1. **Frontend Setup**

Use a combination of JavaScript libraries to handle different formats:

| Format         | Library Used                  |
|----------------|-------------------------------|
| `.pdf`         | [PDF.js](https://mozilla.github.io/pdf.js/)           |
| `.docx`        | [Mammoth.js](https://github.com/mwilliamson/mammoth.js) |
| `.xlsx`, `.xls`| [SheetJS](https://sheetjs.com/)                        |
| `.pptx`        | [Pptx2Html](https://github.com/meshesha/pptx2html) or custom |
| `.txt`, `.html`| Native rendering              |
| `.mht`         | Convert to HTML server-side    |

#### Example: Load `.docx` with Mammoth.js

```html
<input type="file" id="upload" />
<div id="viewer"></div>

<script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
<script>
  document.getElementById("upload").addEventListener("change", function(event) {
    const reader = new FileReader();
    reader.onload = function(event) {
      mammoth.convertToHtml({ arrayBuffer: event.target.result })
        .then(result => {
          document.getElementById("viewer").innerHTML = result.value;
        });
    };
    reader.readAsArrayBuffer(event.target.files[0]);
  });
</script>
```

---

### 2. **Backend Conversion Service**

For formats not natively supported in the browser (e.g., `.doc`, `.ppt`, `.mht`, `.pss`), create a backend service that:

- Accepts file uploads or repository document IDs
- Converts files to HTML or PDF using tools like:
  - LibreOffice (headless mode)
  - Apache POI (for Office formats)
  - Pandoc (for text-based formats)
  - UnMHT (for `.mht` to `.html`)

#### Example: LibreOffice Conversion (Java)

```bash
soffice --headless --convert-to pdf yourfile.docx --outdir /converted
```

Serve the converted file via REST API.

---

### 3. **Integrate with ICN Plugin**

In your ICN plugin:

- Add a custom action or feature
- Use Dojo to load the viewer widget
- Fetch the document from the repository
- Pass it to the frontend viewer

#### Example: Dojo Widget Skeleton

```javascript
define([
  "dojo/_base/declare",
  "dijit/_WidgetBase",
  "dijit/_TemplatedMixin",
  "dojo/text!./templates/Viewer.html"
], function(declare, _WidgetBase, _TemplatedMixin, template) {
  return declare("custom.viewer.DocumentViewer", [_WidgetBase, _TemplatedMixin], {
    templateString: template,
    postCreate: function() {
      // Load and render document here
    }
  });
});
```

---

### 4. **Security & Performance**

- Sanitize HTML to prevent XSS
- Cache converted files
- Use lazy loading for large documents
- Apply access control based on ICN permissions

---

## 🧪 Testing

Test with:
- Large Excel files
- Complex Word documents with images
- PowerPoint with animations
- MHT archives with embedded resources

---

## 🚀 Optional Enhancements

- Add annotation tools (e.g., fabric.js for canvas)
- Enable search and navigation
- Support drag-and-drop
- Integrate with ICN metadata and audit logs

---

Would you like a working prototype using specific formats like `.docx`, `.pdf`, and `.xlsx`? I can help you scaffold the code for that.

------


#### viewer_03

Creating a **universal document viewer** that supports a wide range of formats—**without using `<iframe>`**—requires a smart combination of **frontend rendering libraries** and **backend conversion services**. Here's a complete blueprint to help you build it from scratch.

---

## 🧩 Supported Formats & Strategy

| Format Type       | Rendering Strategy                          |
|-------------------|---------------------------------------------|
| `.txt`, `.html`   | Direct rendering in browser (`<pre>`, `<div>`) |
| `.pdf`            | Render with [PDF.js](https://mozilla.github.io/pdf.js/) |
| `.docx`           | Render with [Mammoth.js](https://github.com/mwilliamson/mammoth.js) |
| `.xlsx`, `.xls`   | Render with [SheetJS](https://sheetjs.com/) |
| `.pptx`, `.ppsx`  | Convert to HTML server-side or use [PptxGenJS](https://gitbrent.github.io/PptxGenJS/) |
| `.doc`, `.ppt`, `.mht`, `.pss` | Convert to HTML/PDF server-side (LibreOffice, Pandoc) |

---

## 🛠️ Step-by-Step Implementation

### 1. 📦 Frontend HTML Shell

```html
<input type="file" id="fileInput" />
<div id="viewer"></div>

<!-- Libraries -->
<script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
```

---

### 2. 🧠 JavaScript Logic

```javascript
document.getElementById("fileInput").addEventListener("change", async function (event) {
  const file = event.target.files[0];
  const ext = file.name.split('.').pop().toLowerCase();
  const viewer = document.getElementById("viewer");
  viewer.innerHTML = "";

  if (ext === "txt" || ext === "html") {
    const text = await file.text();
    viewer.innerHTML = ext === "txt" ? `<pre>${text}</pre>` : text;
  }

  else if (ext === "docx") {
    const arrayBuffer = await file.arrayBuffer();
    const result = await mammoth.convertToHtml({ arrayBuffer });
    viewer.innerHTML = result.value;
  }

  else if (ext === "xlsx" || ext === "xls") {
    const data = await file.arrayBuffer();
    const workbook = XLSX.read(data, { type: "array" });
    const sheet = workbook.Sheets[workbook.SheetNames[0]];
    viewer.innerHTML = XLSX.utils.sheet_to_html(sheet);
  }

  else if (ext === "pdf") {
    const pdfData = await file.arrayBuffer();
    const pdf = await pdfjsLib.getDocument({ data: pdfData }).promise;
    for (let i = 1; i <= pdf.numPages; i++) {
      const page = await pdf.getPage(i);
      const viewport = page.getViewport({ scale: 1.5 });
      const canvas = document.createElement("canvas");
      canvas.height = viewport.height;
      canvas.width = viewport.width;
      viewer.appendChild(canvas);
      await page.render({ canvasContext: canvas.getContext("2d"), viewport }).promise;
    }
  }

  else {
    viewer.innerHTML = `<p>Unsupported format: ${ext}. Try converting it to PDF or HTML first.</p>`;
  }
});
```

---

### 3. 🔄 Backend Conversion (for `.doc`, `.ppt`, `.mht`, etc.)

Use **LibreOffice** in headless mode to convert files to HTML or PDF:

```bash
soffice --headless --convert-to html yourfile.doc --outdir /converted
```

Expose this via a REST API:

```http
GET /convert?fileId=abc123&type=html
```

Then fetch the converted HTML and inject it into the viewer.

---

### 4. 🧪 Testing Tips

- Try `.docx` with images and tables
- Use `.xlsx` with multiple sheets
- Test `.pdf` with multiple pages
- Validate `.pptx` conversion fidelity

---

## 🚀 Optional Enhancements

- 🔍 Add search and zoom
- 🖊️ Add annotation tools (e.g., fabric.js)
- 📄 Add export to PDF/HTML
- 🔐 Integrate with access control and audit logs

---

If you'd like, I can help you scaffold a full working prototype with backend conversion support and a polished UI. Want to go that route next?

------



#### viewer_03

------
