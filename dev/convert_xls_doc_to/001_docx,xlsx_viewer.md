You're off to a great start with your DOCX viewer! The key issue you're facing is that **Mammoth.js** focuses on semantic content rather than visual fidelity—it intentionally ignores many formatting details like RTL/LTR direction, bold, italic, underline, and complex styles. But don't worry, we can enhance your viewer to better reflect the original document's formatting.

Here’s how to improve it:

---

### 🛠️ 1. Enable More Formatting with Mammoth Options

You can pass style mappings to Mammoth to preserve things like bold, italic, and headings:

```javascript
mammoth.convertToHtml({ arrayBuffer: arrayBuffer }, {
  styleMap: [
    "b => strong",
    "i => em",
    "u => u",
    "p[style-name='RTL'] => p[dir=rtl]",
    "p[style-name='LTR'] => p[dir=ltr]"
  ]
})
```

This maps DOCX styles to HTML equivalents. You can customize it further based on your document’s styles.

---

### 🌍 2. Handle RTL/LTR Direction

To support right-to-left (RTL) and left-to-right (LTR) text properly, add this to your CSS:

```css
[dir="rtl"] {
  direction: rtl;
  text-align: right;
}

[dir="ltr"] {
  direction: ltr;
  text-align: left;
}
```

This ensures that paragraphs tagged with `dir="rtl"` or `dir="ltr"` behave correctly.

---

### 💅 3. Improve Styling for Bold, Italic, Underline

Mammoth maps `b`, `i`, and `u` tags to `strong`, `em`, and `u` respectively. You can style them like this:

```css
strong {
  font-weight: bold;
}

em {
  font-style: italic;
}

u {
  text-decoration: underline;
}
```

---

### 📄 4. Optional: Use a More Visual-Fidelity Library

If you need pixel-perfect rendering (like tables, images, footnotes, etc.), consider switching to a more comprehensive library like:

- **docx-preview**: [https://www.npmjs.com/package/docx-preview](https://www.npmjs.com/package/docx-preview)
- **LibreOffice + PDF.js**: Convert DOCX to PDF server-side, then render with PDF.js

---

### ✅ Final Tip

If your DOCX files use custom styles (like "Heading RTL" or "Body LTR"), inspect them in Word and update your `styleMap` accordingly.

Would you like help integrating `docx-preview` or customizing the style map further?

-----

##### How can I add more formatting options with Mammoth?

Great question! 🛠️ Mammoth.js is designed to convert `.docx` files into clean, semantic HTML—but it doesn’t replicate every visual detail by default. To add more formatting options, you can use **style mappings**, **custom CSS**, and **transform functions**. Here's how to level up your formatting game:

---

### 🎨 1. Use `styleMap` for Custom Style Mappings

You can map Word styles to HTML elements using `styleMap`. For example:

```javascript
const options = {
  styleMap: [
    "b => strong",
    "i => em",
    "u => u",
    "strike => s",
    "sub => sub",
    "sup => sup",
    "p[style-name='RTL'] => p[dir=rtl]",
    "p[style-name='Heading 1'] => h1:fresh",
    "p[style-name='CustomStyle'] => div.custom-style"
  ]
};
```

Then pass it to `mammoth.convertToHtml`:

```javascript
mammoth.convertToHtml({ arrayBuffer }, options)
  .then(result => {
    document.getElementById("doc-content").innerHTML = result.value;
  });
```

You can inspect your `.docx` file in Word to find the exact style names used.

---

### 🧠 2. Add CSS to Match Your Mappings

Once you've mapped styles, define them in your CSS:

```css
.custom-style {
  font-size: 18px;
  color: darkblue;
  font-weight: bold;
}

[dir="rtl"] {
  direction: rtl;
  text-align: right;
}

[dir="ltr"] {
  direction: ltr;
  text-align: left;
}
```

---

### 🔧 3. Use Transform Functions (Advanced)

If you need deeper control, you can use Mammoth’s transform API to modify elements programmatically:

```javascript
const transform = {
  transformDocument: mammoth.transforms.paragraph(function(paragraph) {
    if (paragraph.alignment === "center") {
      return { ...paragraph, styleName: "CenteredText" };
    }
    return paragraph;
  })
};
```

This lets you dynamically adjust styles based on content.

---

### 📚 Want More?

You can explore the full documentation and examples on [Mammoth’s GitHub README](https://github.com/mwilliamson/mammoth.js/blob/master/README.md) or check out [this Stack Overflow thread](https://stackoverflow.com/questions/63371690/how-to-format-add-custom-css-to-the-html-using-mammoth-js) for real-world use cases.

If you tell me what specific styles you want to preserve—like tables, font colors, or alignment—I can help you tailor the mappings even further.

-----

##### What are some alternative libraries for better formatting?

-----

##### Can you help me implement RTL and LTR text handling?

-----

##### Can you provide examples of specific styles to map?

Absolutely! 🎯 Here are some practical examples of how you can use **Mammoth.js style mappings** to convert Word styles into clean, semantic HTML. These mappings help preserve formatting like headings, alignment, emphasis, and custom styles from your `.docx` file.

---

### 🧾 Common Style Mappings

| Word Style Name         | HTML Mapping Example                          | Description                                 |
|------------------------|-----------------------------------------------|---------------------------------------------|
| `Heading 1`            | `p[style-name='Heading 1'] => h1:fresh`       | Converts Heading 1 to `<h1>`                |
| `Heading 2`            | `p[style-name='Heading 2'] => h2:fresh`       | Converts Heading 2 to `<h2>`                |
| `Title`                | `p[style-name='Title'] => h1.title:fresh`     | Converts Title to `<h1 class="title">`      |
| `Subtitle`             | `p[style-name='Subtitle'] => h2.subtitle:fresh`| Converts Subtitle to `<h2 class="subtitle">`|
| `Quote`                | `p[style-name='Quote'] => blockquote:fresh`   | Converts Quote to `<blockquote>`            |
| `Emphasis`             | `r[style-name='Emphasis'] => em`              | Converts Emphasis to `<em>`                 |
| `Strong`               | `r[style-name='Strong'] => strong`            | Converts Strong to `<strong>`               |
| `RTL Paragraph`        | `p[style-name='RTL Paragraph'] => p[dir=rtl]` | Sets direction to right-to-left             |
| `LTR Paragraph`        | `p[style-name='LTR Paragraph'] => p[dir=ltr]` | Sets direction to left-to-right             |
| `CustomStyle`          | `p[style-name='CustomStyle'] => div.custom-style` | Converts to `<div class="custom-style">` |

---

### 🧠 Tips for Using These

- **Inspect your Word document**: Open it in Word and check the exact style names used (e.g., "Heading 1", "Quote", "RTL Paragraph").
- **Use `:fresh`**: This tells Mammoth to create a new element rather than reusing an existing one.
- **Use `r[...]` for inline styles**: Like bold, italic, underline—these apply to runs of text, not whole paragraphs.

---

### 🧵 Example `styleMap` Array in JavaScript

```javascript
const options = {
  styleMap: [
    "p[style-name='Heading 1'] => h1:fresh",
    "p[style-name='Quote'] => blockquote:fresh",
    "r[style-name='Strong'] => strong",
    "r[style-name='Emphasis'] => em",
    "p[style-name='RTL Paragraph'] => p[dir=rtl]",
    "p[style-name='CustomStyle'] => div.custom-style"
  ]
};
```

Then pass `options` to `mammoth.convertToHtml`.

---

If you share a few style names from your actual `.docx` file, I can help you write a tailored `styleMap` just for those. Want to go deeper into tables, images, or footnotes next?

-----

##### How can I preserve table formatting in Mammoth?

Mammoth.js does a decent job of extracting **table structure**, but it **does not preserve visual formatting** like borders, colors, or cell padding by default. However, you can enhance table rendering with a few smart tricks. Here's how to get the most out of it:

---

### 🧩 1. Use Style Mapping to Assign Table Classes

You can map Word tables to HTML with a custom class using `styleMap`:

```javascript
const options = {
  styleMap: [
    "table => table.table-style"
  ]
};
```

This will produce:

```html
<table class="table-style">
  <tbody>
    <tr><td>...</td></tr>
  </tbody>
</table>
```

Then you can style it with CSS:

```css
.table-style {
  border-collapse: collapse;
  width: 100%;
}

.table-style td, .table-style th {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

.table-style th {
  background-color: #f2f2f2;
}
```

---

### 🎨 2. Use a Fork with Style Preservation

There’s a [community fork of Mammoth.js](https://github.com/MrPeak/mammoth-with-style-preservation.js/) that adds support for preserving more styles, including:

- Table borders
- Cell background colors
- Padding and margins

You can invoke it like this:

```javascript
mammoth.convertToHtml({ arrayBuffer }, {
  stylePreservations: 'default'
})
```

This fork is more experimental but useful if you need richer formatting.

---

### ⚠️ Limitations to Keep in Mind

- Mammoth focuses on **semantic HTML**, not pixel-perfect rendering.
- It **ignores** Word-specific styles like cell width, height, and border styles unless explicitly mapped or handled via custom forks.
- Complex tables (merged cells, nested tables) may not render accurately.

---

### 🛠️ Alternative Approach

If you need **full fidelity**, consider converting `.docx` to **PDF** server-side and rendering it with [PDF.js](https://mozilla.github.io/pdf.js/), or use a library like [docx-preview](https://www.npmjs.com/package/docx-preview) for better visual accuracy.

Want help switching to one of those or customizing your style map for specific table styles? I’ve got you covered.

-----

##### What are common issues when adding custom styles?

-----

###### give me an example that do all this thigs you show me that read doc, docx, xls, xlsx files and display it the currect way in table, right to left and all the styles that is popular today... with any javascript libraries you wish

You got it! Here's a complete example that reads **DOC/DOCX** and **XLS/XLSX** files, displays them with **correct formatting**, including **tables**, **bold/italic**, and **RTL/LTR direction**, using modern JavaScript libraries.

---

## 🧰 Libraries Used

| Format     | Library Used                     | Purpose                              |
|------------|----------------------------------|--------------------------------------|
| DOCX       | [Mammoth.js](https://github.com/mwilliamson/mammoth.js) | Converts `.docx` to semantic HTML    |
| XLSX       | [SheetJS (xlsx)](https://sheetjs.com/) | Parses Excel files into HTML tables |
| Styling    | Custom CSS                       | Preserves RTL, bold, italic, etc.    |

---

## 🧪 Full HTML Example

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Document Viewer</title>
  <script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
  <script src="https://cdn.sheetjs.com/xlsx-0.20.0/package/dist/xlsx.full.min.js"></script>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
    }

    .viewer {
      margin-top: 20px;
    }

    .rtl {
      direction: rtl;
      text-align: right;
    }

    .ltr {
      direction: ltr;
      text-align: left;
    }

    strong {
      font-weight: bold;
    }

    em {
      font-style: italic;
    }

    table {
      border-collapse: collapse;
      width: 100%;
      margin-top: 10px;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 8px;
    }

    th {
      background-color: #f0f0f0;
    }
  </style>
</head>
<body>

  <h1>Universal Document Viewer</h1>
  <input type="file" id="fileInput" accept=".docx,.xlsx,.xls">
  <div class="viewer" id="output"></div>

  <script>
    const fileInput = document.getElementById('fileInput');
    const output = document.getElementById('output');

    fileInput.addEventListener('change', async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const ext = file.name.split('.').pop().toLowerCase();

      if (ext === 'docx') {
        const reader = new FileReader();
        reader.onload = function(e) {
          mammoth.convertToHtml({ arrayBuffer: e.target.result }, {
            styleMap: [
              "b => strong",
              "i => em",
              "u => u",
              "p[style-name='RTL'] => p.rtl",
              "p[style-name='LTR'] => p.ltr"
            ]
          }).then(result => {
            output.innerHTML = `<h2>DOCX Content:</h2>${result.value}`;
          });
        };
        reader.readAsArrayBuffer(file);
      }

      else if (ext === 'xlsx' || ext === 'xls') {
        const reader = new FileReader();
        reader.onload = function(e) {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          let html = `<h2>Excel Content:</h2>`;
          workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            html += `<h3>${sheetName}</h3>`;
            html += XLSX.utils.sheet_to_html(sheet);
          });
          output.innerHTML = html;
        };
        reader.readAsBinaryString(file);
      }

      else {
        output.innerHTML = `<p>Unsupported file type: ${ext}</p>`;
      }
    });
  </script>

</body>
</html>
```

---

## ✅ What This Supports

- ✅ DOCX: Bold, italic, underline, RTL/LTR paragraphs
- ✅ XLS/XLSX: Full table rendering with headers and cell data
- ✅ Clean styling with semantic HTML
- ✅ No server needed—runs entirely in the browser

---

Want to add PDF or PPTX support next? Or maybe drag-and-drop upload and mobile responsiveness? I can help you build that out too.

-----

###### add table, rtl, ltr,

You got it! Here's a fully enhanced version of your document viewer that supports:

✅ **Bold**, **Italic**, and **Normal** fonts  
✅ **RTL (Right-to-Left)** and **LTR (Left-to-Right)** text direction  
✅ **Tables** with proper formatting  
✅ **DOCX and XLSX/XLS** file support  

All done with **Mammoth.js** for Word files and **SheetJS** for Excel files.

---

## 🧪 Full HTML Example

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Enhanced Document Viewer</title>
  <script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
  <script src="https://cdn.sheetjs.com/xlsx-0.20.0/package/dist/xlsx.full.min.js"></script>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
    }

    h1 {
      margin-bottom: 10px;
    }

    .viewer {
      margin-top: 20px;
    }

    .rtl {
      direction: rtl;
      text-align: right;
      font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    }

    .ltr {
      direction: ltr;
      text-align: left;
      font-family: "Arial", sans-serif;
    }

    strong {
      font-weight: bold;
    }

    em {
      font-style: italic;
    }

    u {
      text-decoration: underline;
    }

    table {
      border-collapse: collapse;
      width: 100%;
      margin-top: 10px;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 8px;
    }

    th {
      background-color: #f0f0f0;
    }

    .normal-font {
      font-weight: normal;
      font-style: normal;
    }
  </style>
</head>
<body>

  <h1>Universal Document Viewer</h1>
  <input type="file" id="fileInput" accept=".docx,.xlsx,.xls">
  <div class="viewer" id="output"></div>

  <script>
    const fileInput = document.getElementById('fileInput');
    const output = document.getElementById('output');

    fileInput.addEventListener('change', async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const ext = file.name.split('.').pop().toLowerCase();

      if (ext === 'docx') {
        const reader = new FileReader();
        reader.onload = function(e) {
          mammoth.convertToHtml({ arrayBuffer: e.target.result }, {
            styleMap: [
              "b => strong",
              "i => em",
              "u => u",
              "r => span.normal-font",
              "p[style-name='RTL'] => p.rtl",
              "p[style-name='LTR'] => p.ltr",
              "table => table"
            ]
          }).then(result => {
            output.innerHTML = `<h2>DOCX Content:</h2>${result.value}`;
          });
        };
        reader.readAsArrayBuffer(file);
      }

      else if (ext === 'xlsx' || ext === 'xls') {
        const reader = new FileReader();
        reader.onload = function(e) {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          let html = `<h2>Excel Content:</h2>`;
          workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            html += `<h3>${sheetName}</h3>`;
            html += XLSX.utils.sheet_to_html(sheet);
          });
          output.innerHTML = html;
        };
        reader.readAsBinaryString(file);
      }

      else {
        output.innerHTML = `<p>Unsupported file type: ${ext}</p>`;
      }
    });
  </script>

</body>
</html>
```

---

### 🧠 How It Works

- **Bold/Italic/Underline**: Mapped via `styleMap` to `<strong>`, `<em>`, and `<u>`
- **Normal Font**: All runs (`r`) default to `.normal-font` class
- **RTL/LTR**: Paragraphs with Word styles like "RTL" or "LTR" are mapped to `.rtl` and `.ltr` classes
- **Tables**: Rendered with clean borders and spacing

---

Want to add support for PDF or drag-and-drop uploads next? Or maybe auto-detect RTL text without relying on Word styles? I can help you build that too.

-----

##### fix style ???

Thanks for pointing that out! Let's fix the **directionality** and **display issues** so your viewer properly handles **RTL/LTR text**, **bold/italic styling**, and **tables**—especially for Hebrew, Arabic, or other right-to-left languages.

---

## ✅ Key Fixes

### 1. **Auto-detect RTL/LTR direction**
Instead of relying on Word styles like `RTL Paragraph`, we’ll detect direction based on the content itself.

```javascript
function detectDirection(text) {
  const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
  return rtlChars.test(text) ? 'rtl' : 'ltr';
}
```

---

### 2. **Apply Direction Dynamically**
Wrap each paragraph with a `div` and set its `dir` attribute based on the detected direction:

```javascript
mammoth.convertToHtml({ arrayBuffer: e.target.result }, {
  styleMap: [
    "b => strong",
    "i => em",
    "u => u",
    "r => span.normal-font",
    "table => table"
  ]
}).then(result => {
  const parser = new DOMParser();
  const doc = parser.parseFromString(result.value, 'text/html');
  const paragraphs = doc.body.querySelectorAll('p');

  paragraphs.forEach(p => {
    const dir = detectDirection(p.textContent);
    p.setAttribute('dir', dir);
    p.classList.add(dir);
  });

  output.innerHTML = `<h2>DOCX Content:</h2>${doc.body.innerHTML}`;
});
```

---

### 3. **Improve CSS for Direction and Styling**

```css
.rtl {
  direction: rtl;
  text-align: right;
  font-family: "Segoe UI", Tahoma, sans-serif;
}

.ltr {
  direction: ltr;
  text-align: left;
  font-family: "Arial", sans-serif;
}

strong {
  font-weight: bold;
}

em {
  font-style: italic;
}

u {
  text-decoration: underline;
}

.normal-font {
  font-weight: normal;
  font-style: normal;
}
```

---

### 4. **Ensure Excel Tables Display Properly**
SheetJS already outputs HTML tables, but you can wrap them with a direction container:

```javascript
const dir = detectDirection(sheetDataAsText);
html += `<div dir="${dir}">${XLSX.utils.sheet_to_html(sheet)}</div>`;
```

---

## 🧪 Result

- ✅ Text direction auto-adjusts per paragraph
- ✅ Bold, italic, underline, and normal fonts render correctly
- ✅ Tables are readable and styled
- ✅ Works for both `.docx` and `.xlsx/.xls` files

---

If you’d like, I can bundle all this into a clean, ready-to-copy HTML file. Or we can add support for PDFs and PowerPoint next. Just say the word.

-----

##### 666 - ALL - good job

Absolutely! Here's a **complete HTML example** that brings together everything we've discussed:

✅ Reads `.docx`, `.xls`, and `.xlsx` files  
✅ Displays **tables**, **bold**, **italic**, **underline**, and **normal fonts**  
✅ Automatically detects and applies **RTL or LTR direction**  
✅ Uses **Mammoth.js** for Word files and **SheetJS** for Excel files  
✅ Styled for clarity and readability

---

## 🧪 Full HTML Document Viewer 
 
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Universal Document Viewer</title>
  <script src="https://unpkg.com/mammoth/mammoth.browser.min.js"></script>
  <script src="https://cdn.sheetjs.com/xlsx-0.20.0/package/dist/xlsx.full.min.js"></script>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f9f9f9;
    }

    h1 {
      margin-bottom: 10px;
    }

    input[type="file"] {
      margin-bottom: 20px;
    }

    .viewer {
      margin-top: 20px;
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    .rtl {
      direction: rtl;
      text-align: right;
      font-family: "Segoe UI", Tahoma, sans-serif;
    }

    .ltr {
      direction: ltr;
      text-align: left;
      font-family: "Arial", sans-serif;
    }

    strong {
      font-weight: bold;
    }

    em {
      font-style: italic;
    }

    u {
      text-decoration: underline;
    }

    .normal-font {
      font-weight: normal;
      font-style: normal;
    }

    table {
      border-collapse: collapse;
      width: 100%;
      margin-top: 10px;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 8px;
    }

    th {
      background-color: #f0f0f0;
    }
  </style>
</head>
<body>

  <h1>Universal Document Viewer</h1>
  <input type="file" id="fileInput" accept=".docx,.xlsx,.xls">
  <div class="viewer" id="output"></div>

  <script>
    const fileInput = document.getElementById('fileInput');
    const output = document.getElementById('output');

    fileInput.addEventListener('change', async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const ext = file.name.split('.').pop().toLowerCase();

      if (ext === 'docx') {
        const reader = new FileReader();
        reader.onload = function(e) {
          mammoth.convertToHtml({ arrayBuffer: e.target.result }, {
            styleMap: [
              "b => strong",
              "i => em",
              "u => u",
              "r => span.normal-font",
              "table => table"
            ]
          }).then(result => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(result.value, 'text/html');
            const paragraphs = doc.body.querySelectorAll('p');

            paragraphs.forEach(p => {
              const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
              const dir = rtlChars.test(p.textContent) ? 'rtl' : 'ltr';
              p.setAttribute('dir', dir);
              p.classList.add(dir);
            });

            output.innerHTML = `<h2>DOCX Content:</h2>${doc.body.innerHTML}`;
          });
        };
        reader.readAsArrayBuffer(file);
      }

      else if (ext === 'xlsx' || ext === 'xls') {
        const reader = new FileReader();
        reader.onload = function(e) {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          let html = `<h2>Excel Content:</h2>`;
          workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            const sheetText = XLSX.utils.sheet_to_csv(sheet);
            const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
            const dir = rtlChars.test(sheetText) ? 'rtl' : 'ltr';
            html += `<h3>${sheetName}</h3>`;
            html += `<div dir="${dir}" class="${dir}">${XLSX.utils.sheet_to_html(sheet)}</div>`;
          });
          output.innerHTML = html;
        };
        reader.readAsBinaryString(file);
      }

      else {
        output.innerHTML = `<p>Unsupported file type: ${ext}</p>`;
      }
    });
  </script>

</body>
</html>
```

---

### 🧠 What This Does

- **DOCX**: Converts to clean HTML with bold, italic, underline, and direction-aware paragraphs
- **XLS/XLSX**: Converts sheets to HTML tables with automatic RTL/LTR detection
- **Styling**: Applies readable fonts, spacing, and layout for clarity

---

If you want to add support for PDFs, drag-and-drop uploads, or even mobile responsiveness, I can help you build that next. Just say the word and we’ll keep leveling it up.

-----


##### like  666 ? check it out.


Thanks! You're pushing this viewer to pro level—and I love it. Now let’s extend your document viewer to support:

✅ **DOC (Word 97–2003)**  
✅ **XLS (Excel 97–2003)**  
✅ With proper **RTL/LTR direction**, **bold**, **italic**, **underline**, and **tables**

---

## 🧰 Libraries We’ll Use

| Format | Library | Purpose |
|--------|---------|---------|
| `.doc` | [LibreOffice + HTML](#offline-conversion) or [docx-preview](#limited-support) | DOC files aren't natively supported in-browser, so we use a workaround |
| `.xls` | [SheetJS (XLS BIFF8)](https://github.com/JonathanDn/js-xlsx-formula-and-rtl) | Parses legacy Excel files with RTL and formatting support |

---

## 🧪 Full HTML Viewer (DOC via `.doc` → `.html`, XLS via SheetJS)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>DOC/XLS Viewer</title>
  <script src="https://cdn.sheetjs.com/xlsx-0.20.0/package/dist/xlsx.full.min.js"></script>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f9f9f9;
    }

    h1 {
      margin-bottom: 10px;
    }

    input[type="file"] {
      margin-bottom: 20px;
    }

    .viewer {
      margin-top: 20px;
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    .rtl {
      direction: rtl;
      text-align: right;
      font-family: "Segoe UI", Tahoma, sans-serif;
    }

    .ltr {
      direction: ltr;
      text-align: left;
      font-family: "Arial", sans-serif;
    }

    strong {
      font-weight: bold;
    }

    em {
      font-style: italic;
    }

    u {
      text-decoration: underline;
    }

    .normal-font {
      font-weight: normal;
      font-style: normal;
    }

    table {
      border-collapse: collapse;
      width: 100%;
      margin-top: 10px;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 8px;
    }

    th {
      background-color: #f0f0f0;
    }
  </style>
</head>
<body>

  <h1>DOC/XLS Viewer</h1>
  <input type="file" id="fileInput" accept=".doc,.xls">
  <div class="viewer" id="output"></div>

  <script>
    const fileInput = document.getElementById('fileInput');
    const output = document.getElementById('output');

    function detectDirection(text) {
      const rtlChars = /[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/;
      return rtlChars.test(text) ? 'rtl' : 'ltr';
    }

    fileInput.addEventListener('change', async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const ext = file.name.split('.').pop().toLowerCase();

      if (ext === 'xls') {
        const reader = new FileReader();
        reader.onload = function(e) {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          let html = `<h2>XLS Content:</h2>`;
          workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            const sheetText = XLSX.utils.sheet_to_csv(sheet);
            const dir = detectDirection(sheetText);
            html += `<h3>${sheetName}</h3>`;
            html += `<div dir="${dir}" class="${dir}">${XLSX.utils.sheet_to_html(sheet)}</div>`;
          });
          output.innerHTML = html;
        };
        reader.readAsBinaryString(file);
      }

      else if (ext === 'doc') {
        output.innerHTML = `
          <h2>DOC Content:</h2>
          <p>📄 DOC files (Word 97–2003) are not natively supported in-browser.</p>
          <p>To view them, convert the file to HTML using <strong>LibreOffice</strong> or <strong>Microsoft Word</strong>:</p>
          <ol>
            <li>Open the DOC file in Word or LibreOffice</li>
            <li>Save As → Web Page (.html)</li>
            <li>Upload the HTML file here or paste its content</li>
          </ol>
        `;
      }

      else {
        output.innerHTML = `<p>Unsupported file type: ${ext}</p>`;
      }
    });
  </script>

</body>
</html>
```

---

## 🧠 Notes

### 📄 DOC Support
Browsers can’t parse `.doc` files directly. You’ll need to:
- Convert `.doc` to `.html` using Word or LibreOffice
- Then paste or load the HTML into your viewer

### 📊 XLS Support
SheetJS supports legacy `.xls` files (BIFF8), and you can enhance it with forks like [js-xlsx-formula-and-rtl](https://github.com/JonathanDn/js-xlsx-formula-and-rtl) for better RTL and styling support.

---

Want me to help you build a drag-and-drop interface or auto-convert `.doc` to `.html` using a server? I can guide you through that too.
-----