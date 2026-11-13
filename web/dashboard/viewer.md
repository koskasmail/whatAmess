Building a universal document viewer like **IBM Daeja ViewONE** requires combining a **Java backend** (for parsing and converting complex formats like Word, Excel, and PowerPoint) with a **JavaScript frontend** (for multi-tab management, zooming, fitting pages, printing, and UI toolbars).

Because native browsers cannot natively display `.docx` or `.xlsx` files without plugins, enterprise apps use a **conversion-and-rendering pipeline**.

---

### Step 1: Architectural Overview

1. **The Backend (Java):** Receives incoming documents (PDF, Word, Excel, PPT, images). It uses libraries to convert non-PDF formats (like Office docs) into standard viewable formats (like **PDF** or **HTML/SVG canvas chunks**).
2. **The Frontend (JavaScript/HTML/CSS):** Renders the converted stream in the browser. It implements a multi-tab workspace, custom toolbars (zoom, fit-to-page, print, download), and page navigation.

---

### Step 2: Implementing the Java Backend (Document Conversion)

To handle Word (`.docx`), Excel (`.xlsx`), PowerPoint (`.pptx`), and Images, your Java backend needs conversion libraries. **Apache POI** is the industry standard for reading Office files in Java, and libraries like **Apache PDFBox** or **LibreOffice headless mode** handle rendering them into PDFs.

#### Recommended Java Libraries:

* **Apache POI:** Reads `.docx`, `.xlsx`, and `.pptx` structures.
* **LibreOffice (Command-line / Process Builder):** For high-fidelity conversion, many enterprise Java applications invoke LibreOffice in headless mode (`libreoffice --headless --convert-to pdf document.docx`) via Java's `ProcessBuilder` to convert any Office file into a PDF on the fly.

#### Example Spring Boot Controller snippet to handle file intake and conversion:

```java
@RestController
@RequestMapping("/api/view")
public class DocumentController {

    @PostMapping("/convert")
    public ResponseEntity<Resource> convertToPdf(@RequestParam("file") MultipartFile file) {
        try {
            File tempInput = File.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempInput);

            // Using LibreOffice via ProcessBuilder to convert Office/Images to PDF
            File tempOutput = new File(tempInput.getParent(), "output.pdf");
            ProcessBuilder pb = new ProcessBuilder("libreoffice", "--headless", "--convert-to", "pdf", tempInput.getAbsolutePath(), "--outdir", tempInput.getParent());
            Process process = pb.start();
            process.waitFor();

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempOutput));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

```

---

### Step 3: Implementing the JavaScript Frontend (UI, Tabs & Zoom)

For the frontend viewer, you don't have to write the PDF/image rendering engine from scratch. You can build a multi-tab toolbar wrapper using **Mozilla's PDF.js** (for PDFs and converted Office files) and native HTML5 image tags (for PNG/GIF files).

#### 1. Multi-Tab Architecture

You can maintain an array of open document states in JavaScript and swap the active container DOM element when a tab is clicked.

#### 2. Toolbar Operations (Zoom, Fit Page, Print)

Using a wrapper around PDF.js gives you programmatic control over scaling and layout:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Daeja-Style Web Viewer</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; display: flex; flex-direction: column; height: 100vh; }
        #toolbar { background: #333; color: white; padding: 10px; display: flex; gap: 10px; align-items: center; }
        #tab-bar { background: #eee; display: flex; border-bottom: 1px solid #ccc; overflow-x: auto; }
        .tab { padding: 8px 16px; cursor: pointer; border-right: 1px solid #ccc; background: #e0e0e0; }
        .tab.active { background: white; font-weight: bold; border-bottom: 2px solid #007bff; }
        #viewer-container { flex: 1; overflow: auto; position: relative; background: #525659; display: flex; justify-content: center; align-items: flex-start; padding: 20px; }
        canvas { box-shadow: 0 4px 6px rgba(0,0,0,0.3); background: white; }
    </style>
</head>
<body>

    <!-- Tab Bar -->
    <div id="tab-bar">
        <div class="tab active" onclick="switchTab('doc1')">Contract.pdf</div>
        <div class="tab" onclick="switchTab('doc2')">Spreadsheet.xlsx (Converted)</div>
    </div>

    <!-- Toolbar -->
    <div id="toolbar">
        <button onclick="zoomIn()">Zoom In (+)</button>
        <button onclick="zoomOut()">Zoom Out (-)</button>
        <button onclick="fitToPage()">Fit Page</button>
        <button onclick="fitWidth()">Horizontal Fit</button>
        <button onclick="printDoc()">Print</button>
        <button onclick="downloadDoc()">Download</button>
    </div>

    <!-- Main Viewport -->
    <div id="viewer-container">
        <canvas id="pdf-canvas"></canvas>
    </div>

    <!-- Include PDF.js Library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.min.js"></script>
    <script>
        pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js';

        let pdfDoc = null,
            pageNum = 1,
            scale = 1.0,
            canvas = document.getElementById('pdf-canvas'),
            ctx = canvas.getContext('2d');

        // Load sample PDF (representing your converted Word/Excel/PDF file)
        function loadDocument(url) {
            pdfjsLib.getDocument(url).promise.then(function(pdfDoc_) {
                pdfDoc = pdfDoc_;
                renderPage(pageNum);
            });
        }

        function renderPage(num) {
            pdfDoc.getPage(num).then(function(page) {
                let viewport = page.getViewport({ scale: scale });
                canvas.height = viewport.height;
                canvas.width = viewport.width;

                let renderContext = {
                    canvasContext: ctx,
                    viewport: viewport
                };
                page.render(renderContext);
            });
        }

        // Toolbar Button Controls
        function zoomIn() { scale += 0.25; renderPage(pageNum); }
        function zoomOut() { scale = Math.max(0.25, scale - 0.25); renderPage(pageNum); }
        
        function fitWidth() {
            let containerWidth = document.getElementById('viewer-container').clientWidth - 40;
            pdfDoc.getPage(pageNum).then(function(page) {
                let unscaledViewport = page.getViewport({ scale: 1.0 });
                scale = containerWidth / unscaledViewport.width;
                renderPage(pageNum);
            });
        }

        function fitToPage() {
            let containerHeight = document.getElementById('viewer-container').clientHeight - 40;
            pdfDoc.getPage(pageNum).then(function(page) {
                let unscaledViewport = page.getViewport({ scale: 1.0 });
                scale = containerHeight / unscaledViewport.height;
                renderPage(pageNum);
            });
        }

        function printDoc() { window.print(); }
        function downloadDoc() { /* Trigger file blob download */ }

        function switchTab(docId) {
            // Logic to switch active tab data stream
            // e.g., loadDocument(docId === 'doc1' ? '/api/view/file1' : '/api/view/file2');
        }

        // Initialize viewer with a demo file
        loadDocument('https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/web/compressed.tracemonkey-pldi-09.pdf');
    </script>
</body>
</html>

```

---

### Step 4: Enterprise Commercial Alternatives (Shortcuts)

Building a complete viewer with annotation support, high-accuracy Excel cell rendering, and CAD support from scratch takes thousands of hours. If you want a drop-in component like Daeja ViewONE, look into commercial SDKs that provide the exact Java/JavaScript architecture out of the box:

* **Apryse WebViewer (formerly PDFTron):** Client-side and server-side SDK that views and edits native PDF, Word, Excel, PowerPoint, and images with full multi-tab UI components.
* **Nutrient (formerly PSPDFKit Web SDK):** Excellent JavaScript/Java enterprise framework for document viewing, Office-to-PDF conversion, and annotations.

Would you like to dive deeper into configuring the Java Spring Boot file converter backend or expanding the JavaScript frontend layout?
