<a name="topage"></a>

# template

Great! Here's a complete example of a **glass-style business card** that:

- Displays info from a **JSON file**
- Includes **forward/back arrows** to cycle through cards
- Uses **HTML, CSS, and JavaScript**

---

### ✅ Folder Structure:

```
/project-folder
│
├── index.html
├── style.css
├── script.js
└── data.json
```

---

### 📄 `data.json` (Example Data)

```json
[
  {
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1 (123) 456-7890",
    "department": "Marketing"
  },
  {
    "firstName": "Jane",
    "lastName": "Smith",
    "phone": "+1 (987) 654-3210",
    "department": "Engineering"
  },
  {
    "firstName": "Carlos",
    "lastName": "Rivera",
    "phone": "+44 20 7946 0958",
    "department": "Sales"
  }
]
```

---

### 📄 `index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Glass Business Card</title>
  <link rel="stylesheet" href="style.css" />
</head>
<body>
  <div class="container">
    <div class="glass-card" id="card">
      <h2 id="fullName">Loading...</h2>
      <p><span class="label">Phone:</span> <span id="phone"></span></p>
      <p><span class="label">Department:</span> <span id="department"></span></p>
    </div>
    <div class="nav-buttons">
      <button id="prev">&larr;</button>
      <button id="next">&rarr;</button>
    </div>
  </div>

  <script src="script.js"></script>
</body>
</html>
```

---

### 🎨 `style.css`

```css
body {
  margin: 0;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #4facfe, #00f2fe);
  font-family: 'Segoe UI', sans-serif;
}

.container {
  text-align: center;
}

.glass-card {
  width: 320px;
  padding: 20px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
  text-shadow: 0 1px 2px rgba(0,0,0,0.2);
  margin-bottom: 15px;
}

h2 {
  margin: 0;
}

.label {
  font-weight: bold;
}

.nav-buttons button {
  padding: 10px 20px;
  font-size: 20px;
  margin: 0 10px;
  border: none;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  cursor: pointer;
  transition: 0.3s;
}

.nav-buttons button:hover {
  background: rgba(255, 255, 255, 0.4);
}
```

---

### 🧠 `script.js`

```javascript
let data = [];
let currentIndex = 0;

const fullName = document.getElementById("fullName");
const phone = document.getElementById("phone");
const department = document.getElementById("department");
const prevBtn = document.getElementById("prev");
const nextBtn = document.getElementById("next");

async function loadData() {
  const response = await fetch("data.json");
  data = await response.json();
  displayCard(currentIndex);
}

function displayCard(index) {
  const person = data[index];
  fullName.textContent = `${person.firstName} ${person.lastName}`;
  phone.textContent = person.phone;
  department.textContent = person.department;
}

prevBtn.addEventListener("click", () => {
  currentIndex = (currentIndex - 1 + data.length) % data.length;
  displayCard(currentIndex);
});

nextBtn.addEventListener("click", () => {
  currentIndex = (currentIndex + 1) % data.length;
  displayCard(currentIndex);
});

loadData();
```

---

### ✅ To Run This:
- Place all files in the same folder.
- Serve with **Live Server** or any local server (fetch won't work with `file://` due to CORS in most browsers).

- 
-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>

Want a fancier version with animations or card flipping effect?
