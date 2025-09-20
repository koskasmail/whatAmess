<a name="topage"></a>

# 01_md_Viewer.md

Here's a complete example of an HTML, CSS, and JavaScript site that includes:

- A **navbar** with a "Load" button and a text box showing the selected `.md` filename.
- The ability to **load and display Markdown files** as rendered HTML below the navbar.

---

### 🧩 File Structure
You only need one HTML file for this demo:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Markdown Viewer</title>
  <style>
    body {
      margin: 0;
      font-family: Arial, sans-serif;
    }
    .navbar {
      background-color: #333;
      color: white;
      padding: 10px;
      display: flex;
      align-items: center;
    }
    .navbar input[type="file"] {
      display: none;
    }
    .navbar label {
      background-color: #555;
      padding: 8px 12px;
      cursor: pointer;
      border-radius: 4px;
      margin-right: 10px;
    }
    .filename-box {
      background-color: white;
      color: black;
      padding: 6px 10px;
      border-radius: 4px;
      border: 1px solid #ccc;
      min-width: 200px;
    }
    #content {
      padding: 20px;
    }
  </style>
</head>
<body>

  <div class="navbar">
    <label for="fileInput">Load</label>
    <input type="file" id="fileInput" accept=".md" />
    <div class="filename-box" id="filenameBox">No file selected</div>
  </div>

  <div id="content">Markdown content will appear here...</div>

  <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
  <script>
    const fileInput = document.getElementById('fileInput');
    const filenameBox = document.getElementById('filenameBox');
    const contentDiv = document.getElementById('content');

    document.querySelector('label[for="fileInput"]').addEventListener('click', () => {
      fileInput.click();
    });

    fileInput.addEventListener('change', (event) => {
      const file = event.target.files[0];
      if (file && file.name.endsWith('.md')) {
        filenameBox.textContent = file.name;

        const reader = new FileReader();
        reader.onload = function(e) {
          const markdown = e.target.result;
          const html = marked.parse(markdown);
          contentDiv.innerHTML = html;
        };
        reader.readAsText(file);
      } else {
        filenameBox.textContent = 'Invalid file type';
        contentDiv.innerHTML = '';
      }
    });
  </script>

</body>
</html>
```

---

### 🛠 How It Works
- The `marked.js` library converts Markdown to HTML.
- The file input is hidden and triggered by a styled label.
- When a `.md` file is selected, its name appears in the box and its content is rendered below the navbar.

Would you like to add support for drag-and-drop or syntax highlighting too?

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
