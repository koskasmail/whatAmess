
# doc_viewer_1

#### #1

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

#### #2

To build a **unified document viewer** that supports a wide range of formats—like `.txt`, `.html`, `.mht`, `.doc`, `.docx`, `.xls`, `.xlsx`, `.ppt`, `.pptx`, `.pps`, `.ppsx`, `.pdf`, and more—**without using `<iframe>`**, you’ll need a hybrid approach that combines:

- **Frontend JavaScript libraries** for rendering supported formats directly in the browser
- **Backend conversion services** for transforming unsupported formats into HTML or canvas-renderable content

Let’s walk through a complete **architecture and implementation plan**.

---

## 🧩 Architecture Summary

| Layer        | Role                                                                 |
|--------------|----------------------------------------------------------------------|
| Frontend     | Renders documents using JS libraries (PDF.js, Mammoth.js, SheetJS)  |
| Backend      | Converts complex formats to HTML or PDF using LibreOffice, Pandoc   |
| Viewer Shell | Unified UI to load and display content in `<div>` or `<canvas>`     |

---

## 🛠️ Step-by-Step Implementation

### 1. 📦 Frontend Libraries by Format

| Format         | Library Used                  | Rendered In |
|----------------|-------------------------------|-------------|
| `.pdf`         | [PDF.js](https://mozilla.github.io/pdf.js/)           | `<canvas>`   |
| `.docx`        | [Mammoth.js](https://github.com/mwilliamson/mammoth.js) | `<div>`      |
| `.xlsx`, `.xls`| [SheetJS](https://sheetjs.com/)                        | `<table>`    |
| `.pptx`, `.ppsx`| [Pptx2Html](https://github.com/meshesha/pptx2html) or backend | `<div>` |
| `.txt`, `.html`| Native rendering              | `<pre>` / `<div>` |
| `.mht`, `.doc`, `.ppt`, `.pss` | Convert to HTML/PDF server-side | `<div>` / `<canvas>` |

---

### 2. 🧠 Backend Conversion Service

Use a backend (Node.js, Python, or Java) to convert unsupported formats to HTML or PDF.

#### Example: LibreOffice Headless Conversion

```bash
soffice --headless --convert-to html yourfile.doc --outdir /converted
```

Expose this via REST API:

```http
GET /convert?fileId=123&type=html
```

---

### 3. 🖼️ Unified Viewer Shell (HTML + JS)

```html
<input type="file" id="upload" />
<div id="viewer"></div>

<script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>

<script>
  document.getElementById("upload").addEventListener("change", async function(event) {
    const file = event.target.files[0];
    const ext = file.name.split('.').pop().toLowerCase();
    const viewer = document.getElementById("viewer");
    viewer.innerHTML = "";

    if (ext === "txt") {
      const text = await file.text();
      viewer.innerHTML = `<pre>${text}</pre>`;
    } else if (ext === "html") {
      const html = await file.text();
      viewer.innerHTML = html;
    } else if (ext === "docx") {
      const arrayBuffer = await file.arrayBuffer();
      const result = await mammoth.convertToHtml({ arrayBuffer });
      viewer.innerHTML = result.value;
    } else if (ext === "xlsx" || ext === "xls") {
      const data = await file.arrayBuffer();
      const workbook = XLSX.read(data, { type: "array" });
      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const html = XLSX.utils.sheet_to_html(sheet);
      viewer.innerHTML = html;
    } else if (ext === "pdf") {
      const pdfData = await file.arrayBuffer();
      const pdf = await pdfjsLib.getDocument({ data: pdfData }).promise;
      for (let i = 1; i <= pdf.numPages; i++) {
        const page = await pdf.getPage(i);
        const viewport = page.getViewport({ scale: 1.5 });
        const canvas = document.createElement("canvas");
        const context = canvas.getContext("2d");
        canvas.height = viewport.height;
        canvas.width = viewport.width;
        viewer.appendChild(canvas);
        await page.render({ canvasContext: context, viewport }).promise;
      }
    } else {
      viewer.innerHTML = `<p>Unsupported format: ${ext}</p>`;
    }
  });
</script>
```

---

### 4. 🔄 Backend Fallback for Complex Formats

If the format is `.doc`, `.ppt`, `.mht`, `.pps`, `.pss`, etc.:

- Upload to backend
- Convert to HTML or PDF
- Return HTML content or PDF buffer
- Render using same frontend logic

---

### 5. 🔐 Security & UX Considerations

- Sanitize HTML to prevent XSS
- Show loading indicators for large files
- Add pagination for long documents
- Respect access control if integrated with a repository

---

## 🚀 Optional Features

- Annotation tools (fabric.js or PDF.js annotations)
- Search within documents
- Zoom and rotate
- Save as PDF or HTML
- Integration with IBM Content Navigator

---

Would you like me to generate a working HTML+JS prototype for a few formats (e.g., `.pdf`, `.docx`, `.xlsx`) to get started?

------


####

------



####

------



####

------


