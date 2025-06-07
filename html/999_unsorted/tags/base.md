<a name="topage"></a>

# base.md


* `<base>` tag set base URL/target relative paths for all linked resources:
* The <base> tag specifies the base URL and/or target for all relative URLs in a document.
    * href (URL) 
    * target (_blank / _parent / _self / _top)

### 1. **HTML (index.html)**
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <base href="https://koskasmail.github.io/whatAmess/html/999_unsorted/tags/">  <!-- Base path for all relative links -->
    <title>Calculator</title>
    <link rel="stylesheet" href="styles/style.css">  <!-- path: ./tags/styles/style.css -->
</head>
<body>
    <h1>Simple Calculator</h1>
    <input type="number" id="num1" placeholder="Enter first number">
    <input type="number" id="num2" placeholder="Enter second number">
    <button onclick="calculate()">Calculate</button>
    <p id="result"></p>

    <script src="scripts/script.js"></script>  <!-- path: ./tags/scripts/script.js -->
</body>
</html>
```

### 2. **CSS (styles/style.css)**
```css
body {
    font-family: Arial, sans-serif;
    text-align: center;
    margin: 50px;
}

input, button {
    margin: 10px;
    padding: 10px;
}
```

### 3. **JavaScript (scripts/script.js)**
```js
function calculate() {
    let num1 = parseFloat(document.getElementById("num1").value);
    let num2 = parseFloat(document.getElementById("num2").value);
    let result = num1 + num2;
    document.getElementById("result").textContent = "Result: " + result;
}
```

### Folder Structure:
```
project-folder/
│── tags/
│   ├── html/       (Store HTML files here)
│   │   ├── index.html
│   ├── scripts/    (JavaScript files)
│   │   ├── script.js
│   ├── styles/     (CSS files)
│   │   ├── style.css
```

```html
...
<head>
    <base href="https://koskasmail.github.io/whatAmess/html/999_unsorted/tags/">
    ...
</head>
...
```

#### TIP
* This setup ensures that all files reference the correct paths relative to `./tags/`.
* [tag-example](https://koskasmail.github.io/whatAmess/html/999_unsorted/tags/base/html/)


---- 

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
