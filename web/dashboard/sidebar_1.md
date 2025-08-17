
<a name="topage"></a>

# dashboard sidebar with autohide/autoshow

-----

Absolutely! Here's a clean and functional example of a dashboard sidebar that auto-hides and auto-shows with a button click. It uses **HTML**, **CSS**, and **JavaScript**, and includes smooth transitions for a polished feel.

---

### 🧱 HTML

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Dashboard Sidebar</title>
  <link rel="stylesheet" href="style.css" />
</head>
<body>
  <div class="wrapper">
    <div id="sidebar" class="sidebar">
      <h2>Menu</h2>
      <ul>
        <li>🏠 Home</li>
        <li>👤 Profile</li>
        <li>⚙️ Settings</li>
        <li>🚪 Logout</li>
      </ul>
    </div>
    <div class="content">
      <button id="toggleBtn">☰ Toggle Sidebar</button>
      <h1>Welcome to Your Dashboard</h1>
      <p>This is your main content area.</p>
    </div>
  </div>

  <script src="script.js"></script>
</body>
</html>
```

---

### 🎨 CSS (style.css)

```css
body {
  margin: 0;
  font-family: 'Segoe UI', sans-serif;
}

.wrapper {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 250px;
  background-color: #34495e;
  color: white;
  padding: 20px;
  box-sizing: border-box;
  transition: transform 0.3s ease;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  margin: 15px 0;
  cursor: pointer;
}

.sidebar.hidden {
  transform: translateX(-100%);
}

.content {
  flex: 1;
  padding: 30px;
  background-color: #ecf0f1;
}

#toggleBtn {
  padding: 10px 15px;
  font-size: 16px;
  cursor: pointer;
  background-color: #2ecc71;
  border: none;
  color: white;
  border-radius: 5px;
}
```

---

### ⚙️ JavaScript (script.js)

```javascript
const toggleBtn = document.getElementById('toggleBtn');
const sidebar = document.getElementById('sidebar');

toggleBtn.addEventListener('click', () => {
  sidebar.classList.toggle('hidden');
});
```

---

### 💡 Bonus Ideas

Want to take it further?
- Add icons with [Font Awesome](https://fontawesome.com/)
- Make it responsive with media queries
- Add animation effects with `opacity` or `box-shadow`
- Store sidebar state in `localStorage` so it remembers your preference

Let me know if you want a version with collapsible menu items or a mobile-friendly layout. I’ve got plenty of tricks up my sleeve.

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
