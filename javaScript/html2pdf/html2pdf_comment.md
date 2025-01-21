<a name="topage"></a>

#### html
```
<h1>MHT File Reader</h1>

<input type="file" id="mhtFileInput">

<div id="htmlDisplay"></div>
```

##### css
```
body {
  font-family: sans-serif;
}

#mhtFileInput {
  margin-bottom: 10px;
}

#htmlDisplay {
  border: 1px solid #ccc;
  padding: 10px;
  overflow: auto;
  height: 300px; /* Adjust height as needed */
}
```


#### js
```
function readMHTFile(file, callback) {
  const reader = new FileReader();

  reader.onload = function(event) {
    const mhtData = event.target.result;
    const lines = mhtData.split(/\r\n|\n/);

    let html = "";
    let currentPart = "";

    for (const line of lines) {
      if (line.startsWith("Content-Type: text/html")) {
        currentPart = "html";
      } else if (line.startsWith("Content-Type:")) {
        currentPart = "";
      }

      if (currentPart === "html") {
        html += line + "\n";
      }
    }

    callback(html);
  };

  reader.onerror = function(error) {
    console.error("Error reading MHT file:", error);
    callback(null);
  };

  reader.readAsText(file);
}

// Example usage:
const fileInput = document.getElementById("mhtFileInput");

fileInput.addEventListener("change", function() {
  const file = fileInput.files[0];
  if (file) {
    readMHTFile(file, function(html) {
      if (html) {
        // Display or process the extracted HTML
        const displayArea = document.getElementById("htmlDisplay");
        displayArea.innerHTML = html;
      }
    });
  }
});
```


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
