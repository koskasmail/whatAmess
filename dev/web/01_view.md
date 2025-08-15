
# 01_view.md

Here’s a complete example that creates a simple web interface using **HTML**, **CSS**, and **JavaScript** to:

- Display a top navigation bar with two buttons: **Load File** and **Display File**
- Allow the user to upload a `.docx` file
- Convert the file to HTML using **Mammoth.js** (a browser-friendly library)
- Render the converted HTML below the buttons, preserving the original document’s structure as closely as possible

---

### 📁 Step 1: Include Mammoth.js

You’ll need to include [Mammoth.js](https://github.com/mwilliamson/mammoth.js) in your project. You can use a CDN:

```html
<script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
```

---

### 🧩 Full HTML + CSS + JS Example

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>DOCX Viewer</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
    }

    .navbar {
      display: flex;
      background-color: #333;
      padding: 10px;
      color: white;
    }

    .navbar button {
      background-color: #444;
      color: white;
      border: none;
      padding: 10px 20px;
      margin-right: 10px;
      cursor: pointer;
    }

    .navbar button:hover {
      background-color: #555;
    }

    #output {
      padding: 20px;
    }

    input[type="file"] {
      display: none;
    }
  </style>
</head>
<body>

  <div class="navbar">
    <label for="upload" class="button">
      <button>Load File</button>
    </label>
    <button onclick="displayFile()">Display File</button>
    <input type="file" id="upload" accept=".docx">
  </div>

  <div id="output">
    <h2>Converted HTML Output:</h2>
    <div id="doc-content"></div>
  </div>

  <script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
  <script>
    let uploadedFile = null;

    document.getElementById('upload').addEventListener('change', function(event) {
      uploadedFile = event.target.files[0];
    });

    function displayFile() {
      if (!uploadedFile) {
        alert("Please upload a .docx file first.");
        return;
      }

      const reader = new FileReader();
      reader.onload = function(event) {
        const arrayBuffer = event.target.result;

        mammoth.convertToHtml({ arrayBuffer: arrayBuffer })
          .then(function(result) {
            document.getElementById("doc-content").innerHTML = result.value;
          })
          .catch(function(err) {
            console.error("Error converting file:", err);
          });
      };

      reader.readAsArrayBuffer(uploadedFile);
    }
  </script>

</body>
</html>
```

---

### 🧠 Notes

- This works **only with `.docx` files**, not `.doc` (older format). You can convert `.doc` to `.docx` using Word or LibreOffice.
- Mammoth.js focuses on **semantic HTML**, so it won’t replicate exact fonts, colors, or layout—but it does a great job with headings, paragraphs, lists, and tables.
- For **pixel-perfect fidelity**, you’d need a server-side solution like LibreOffice or Word automation.

---

Want to take this further with drag-and-drop upload or export to PDF? I can help you build that too.
