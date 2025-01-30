<a name="topage"></a>

# png2svg

Creating a program that converts PNG files to SVG format using just HTML, CSS, and JavaScript is a bit complex, as it involves both image processing and file format conversion, which is not directly supported by HTML or JavaScript in a straightforward manner. However, I can guide you through a basic approach using JavaScript and a library like `png-to-svg` or `potrace`.

To convert a PNG image to SVG, we generally need to trace the raster image (PNG) into vector data. Here's a step-by-step guide to building a simple program that does this:

### 1. Use HTML for File Input
First, allow the user to upload a PNG file.

### 2. Use JavaScript to Handle the Conversion
You can utilize JavaScript libraries that help in tracing a PNG to SVG format. One popular choice is `potrace`, which can convert raster images into vector format.

### Full Example:

#### HTML (index.html)
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>PNG to SVG Converter</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      text-align: center;
      padding: 50px;
    }
    input[type="file"] {
      margin-bottom: 20px;
    }
    #svgOutput {
      margin-top: 20px;
      max-width: 100%;
    }
  </style>
</head>
<body>

  <h1>PNG to SVG Converter</h1>

  <input type="file" id="uploadImage" accept="image/png">
  <div id="svgOutput"></div>

  <script src="https://cdn.jsdelivr.net/npm/potrace@1.2.0/potrace.min.js"></script>
  <script>
    const fileInput = document.getElementById('uploadImage');
    const svgOutput = document.getElementById('svgOutput');

    fileInput.addEventListener('change', handleFileSelect);

    function handleFileSelect(event) {
      const file = event.target.files[0];
      if (file && file.type === "image/png") {
        const reader = new FileReader();
        
        reader.onload = function(e) {
          const img = new Image();
          img.src = e.target.result;
          
          img.onload = function() {
            convertToSVG(img);
          };
        };
        
        reader.readAsDataURL(file);
      } else {
        alert("Please upload a PNG file.");
      }
    }

    function convertToSVG(image) {
      // Create a canvas to draw the PNG image
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      canvas.width = image.width;
      canvas.height = image.height;
      ctx.drawImage(image, 0, 0);

      // Get the image data
      const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
      
      // Convert the image data to SVG using Potrace library
      const potrace = new Potrace();
      potrace.loadImageData(imageData);
      potrace.trace(function(svg) {
        // Display the SVG
        svgOutput.innerHTML = svg;
      });
    }
  </script>

</body>
</html>
```

### How This Works:
1. **File Upload**: The user can upload a PNG file.
2. **File Reader**: The `FileReader` API is used to load the image.
3. **Canvas**: The image is drawn on a hidden HTML `<canvas>`.
4. **Potrace**: Potrace is a library that can trace raster images and convert them to SVG. We use it to convert the canvas image data into an SVG format.
5. **Output**: The resulting SVG is displayed on the page.

### Libraries Used:
- **Potrace**: This library is used to trace raster images (PNG) into SVG paths.

#### Additional Notes:
- **Quality of Conversion**: The quality of the SVG generated depends on the original PNG image and the settings of the tracing library (like Potrace).
- **Advanced Features**: Potrace offers configuration options like controlling the detail level of the trace (e.g., color, curve smoothness).
  
This is a basic implementation that should get you started. You can further enhance it by allowing download of the SVG or optimizing the tracing algorithm.



----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
