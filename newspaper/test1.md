📰 That sounds like an awesome little dynamic newspaper project! Let’s build a lightweight example using **HTML**, **CSS**, and **JavaScript** that loads the correct news page based on the current date and time from a JSON file.

---

## 🧠 How it works
- JSON files are named using a specific format (e.g., `2025-07-08.json`)
- Each JSON file contains all the data to render the page (like headline, body, images, etc.)
- JavaScript detects the current date and loads the corresponding JSON file
- HTML provides the structure; CSS styles it like a newspaper

---

## 📁 Example JSON (`2025-07-08.json`)

```json
{
  "title": "New Discoveries on Mars",
  "date": "Tuesday, July 8, 2025",
  "articles": [
    {
      "headline": "NASA Rover Finds Frozen Lake",
      "content": "In a surprising twist, the latest Mars rover uncovered evidence of a frozen lake just beneath the surface...",
      "author": "Jane Orbiter"
    },
    {
      "headline": "Private Companies Race to Build Mars Base",
      "content": "Several startups are now competing to create the first human settlement on Mars within the decade...",
      "author": "Max Stellar"
    }
  ]
}
```

---

## 🧱 HTML (index.html)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Daily Space Times</title>
  <link rel="stylesheet" href="styles.css" />
</head>
<body>
  <div class="newspaper">
    <h1 id="title"></h1>
    <p id="date"></p>
    <div id="articles"></div>
  </div>
  <script src="script.js"></script>
</body>
</html>
```

---

## 🎨 CSS (styles.css)

```css
body {
  font-family: 'Times New Roman', serif;
  padding: 2rem;
  background: #fffdf5;
  color: #222;
}

.newspaper {
  max-width: 800px;
  margin: auto;
}

h1 {
  font-size: 3rem;
  border-bottom: 2px solid #222;
}

article {
  margin-bottom: 2rem;
}

article h2 {
  font-size: 1.5rem;
  color: #111;
}

article p {
  line-height: 1.6;
}
```

---

## ⚙️ JavaScript (script.js)

```javascript
const today = new Date();
const filename = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}.json`;

fetch(`data/${filename}`)
  .then(res => res.json())
  .then(data => {
    document.getElementById('title').textContent = data.title;
    document.getElementById('date').textContent = data.date;

    const articlesContainer = document.getElementById('articles');
    data.articles.forEach(article => {
      const section = document.createElement('article');
      section.innerHTML = `<h2>${article.headline}</h2><p>${article.content}</p><em>- ${article.author}</em>`;
      articlesContainer.appendChild(section);
    });
  })
  .catch(err => {
    document.getElementById('title').textContent = 'No News Today';
    document.getElementById('date').textContent = '';
    document.getElementById('articles').innerHTML = '<p>Looks like the press took the day off!</p>';
    console.error(err);
  });
```

---

Want to take this further with categories, images, or even search functionality? Let’s level up your newspaper together 📈🗞️
