Yes. You can make the application look approximately like **browser zoom 150%**, while still keeping the responsive behavior.

The important thing is **not** to simply increase every font and width manually. We can scale the whole application on larger screens and remove that scaling on smaller screens.

## 1. Change `src/styles.css`

Replace your current `styles.css` with:

```css
html,
body {
  margin: 0;
  min-height: 100%;

  font-family:
    Arial,
    "Segoe UI",
    sans-serif;

  background: #eaf2fc;
}

body {
  direction: rtl;
}

/* -----------------------------------
   DESKTOP - 150% visual scale
   ----------------------------------- */

@media (min-width: 1000px) {

  body {
    zoom: 1.5;
  }

}


/* -----------------------------------
   NORMAL SIZE BELOW DESKTOP
   ----------------------------------- */

@media (max-width: 999px) {

  body {
    zoom: 1;
  }

}


button,
input,
select,
textarea {
  font-family: inherit;
}

* {
  box-sizing: border-box;
}
```

This gives you:

```text
Browser normal:

┌───────────────────────────────┐
│                               │
│       Angular application     │
│                               │
└───────────────────────────────┘


Browser / application at 150%:

┌──────────────────────────────────────────┐
│                                          │
│          Angular application             │
│          appears larger                  │
│                                          │
└──────────────────────────────────────────┘
```

But there's an important difference: we're scaling the **application**, not changing the user's actual browser zoom.

---

# 2. Make better use of the available screen

Your current dashboard has this:

```css
.stats {
  width: min(738px, 100%);
}
```

and:

```css
.search-panel {
  width: min(738px, 100%);
}
```

and:

```css
.bottom-grid {
  width: min(738px, 100%);
}
```

That's quite narrow for a desktop monitor.

If you want the application to use more of the available browser window, change all three to:

```css
.stats {
  width: min(1100px, 100%);
  margin: 0 auto 14px;

  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}
```

```css
.search-panel {
  width: min(1100px, 100%);
  margin: 0 auto 14px;

  background: #fff;
  border-radius: 10px;
  padding: 13px 15px 14px;
}
```

```css
.bottom-grid {
  width: min(1100px, 100%);
  margin: 0 auto;

  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 14px;

  direction: ltr;
}
```

Now your application can expand much more on a large monitor.

---

# 3. Make the sidebar larger too

Your current sidebar is:

```css
.sidebar {
  width: 145px;
}
```

At 150% this becomes visually about:

```text
145 × 1.5 = 217.5px
```

That is probably too wide.

Instead, because we're using `zoom`, keep the CSS width around:

```css
.sidebar {
  width: 150px;
}
```

The visual result will be approximately:

```text
150 × 1.5 = 225px
```

If you want something closer to the screenshot, use:

```css
.sidebar {
  width: 145px;
}
```

So **don't increase the sidebar width** if you're using `zoom: 1.5`.

---

# 4. Responsive behavior

Keep your existing media queries in:

```text
dashboard.component.css
```

For example:

```css
@media (max-width: 850px) {

  .sidebar {
    width: 110px;
  }

  .content {
    width: calc(100% - 110px);
    margin-right: 110px;
    padding: 12px;
  }

  .service-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .bottom-grid {
    grid-template-columns: 1fr;
  }
}


@media (max-width: 600px) {

  .sidebar {
    width: 0;
    overflow: hidden;
    border: 0;
  }

  .content {
    width: 100%;
    margin-right: 0;
    padding: 10px;
  }

  .stats {
    grid-template-columns: 1fr;
  }

  .service-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
```

---

# 5. One change I recommend

Because we're using:

```css
body {
    zoom: 1.5;
}
```

I would change the desktop breakpoint from:

```css
@media (min-width: 1000px)
```

to:

```css
@media (min-width: 1200px)
```

So:

```css
@media (min-width: 1200px) {

  body {
    zoom: 1.5;
  }

}
```

This gives you:

| Browser width | Application |
| ------------: | ----------- |
|        1920px | 150%        |
|        1600px | 150%        |
|        1440px | 150%        |
|        1200px | 150%        |
|        1000px | Normal      |
|         900px | Normal      |
|         768px | Responsive  |
|         600px | Mobile      |
|         400px | Mobile      |

---

## 6. Recommended final `styles.css`

I would use this:

```css
html,
body {
  margin: 0;
  min-height: 100%;

  font-family:
    Arial,
    "Segoe UI",
    sans-serif;

  background: #eaf2fc;
}

body {
  direction: rtl;
}


/* =========================================
   LARGE DESKTOP
   Similar visual effect to 150% browser zoom
   ========================================= */

@media (min-width: 1200px) {

  body {
    zoom: 1.5;
  }

}


/* =========================================
   TABLET / SMALL DESKTOP
   ========================================= */

@media (max-width: 1199px) {

  body {
    zoom: 1;
  }

}


button,
input,
select,
textarea {
  font-family: inherit;
}


* {
  box-sizing: border-box;
}
```

And change the three dashboard widths to:

```css
.stats,
.search-panel,
.bottom-grid {
  width: min(1100px, 100%);
}
```

### Result

On a large monitor you'll get a UI that is **much larger and easier to read**, roughly like a 150% zoomed application, while on smaller screens the zoom disappears and your existing responsive rules take over.

One additional improvement would be to make the **dashboard automatically calculate its maximum width from the browser window**, so on a 1920px monitor it uses more space, on 1366px it fits nicely, and on mobile it switches to the compact layout.
