<a name="topage"></a>

# 02_md_viewer.md


You got it! Here's an enhanced version of your site that:

✅ Keeps the **navbar fixed** at the top of the page  
✅ Properly **renders Markdown** including links, bold, italic, underline, headings, lists, etc.  
✅ Uses the `marked.js` library to convert `.md` content into clean HTML

---

### ✅ Full HTML + CSS + JS Example

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
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      background-color: #333;
      color: white;
      padding: 10px;
      display: flex;
      align-items: center;
      z-index: 1000;
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
      padding: 80px 20px 20px; /* Top padding to avoid overlap with fixed navbar */
    }

    #content a {
      color: blue;
      text-decoration: underline;
    }

    #content strong {
      font-weight: bold;
    }

    #content em {
      font-style: italic;
    }

    #content u {
      text-decoration: underline;
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

### 💡 Notes
- The navbar is `position: fixed` so it stays visible while scrolling.
- The `marked.js` library handles all Markdown syntax including:
  - `**bold**`, `*italic*`, `__underline__`, `# headings`, `[links](url)`, lists, code blocks, etc.
- You can customize the CSS to style Markdown elements further if needed.

Want to add support for `.md` drag-and-drop or syntax highlighting for code blocks next?

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
