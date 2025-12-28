Here’s a clean, self‑contained Python example that **reads a Markdown (.md) file and displays it as HTML**. No external libraries are required unless you want full Markdown rendering.

Below are **two versions**:

---

# 🟦 **Version 1 — Minimal viewer (no external libraries)**  
This converts only basic Markdown (headings, bold, italics, lists) using simple replacements.

```python
def md_to_html(md_text):
    html = md_text

    # Headings
    html = html.replace("### ", "<h3>") \
               .replace("## ", "<h2>") \
               .replace("# ", "<h1>")

    # Bold and italic
    html = html.replace("**", "<b>").replace("__", "<b>")
    html = html.replace("*", "<i>").replace("_", "<i>")

    # Line breaks
    html = html.replace("\n", "<br>")

    return f"<html><body>{html}</body></html>"


def view_md_file(path):
    with open(path, "r", encoding="utf-8") as f:
        md_text = f.read()

    html = md_to_html(md_text)
    print(html)


# Example usage
view_md_file("example.md")
```

This prints the HTML to the console.

---

# 🟩 **Version 2 — Proper Markdown → HTML (still simple, uses built‑in `markdown` module if available)**  
If your Python installation includes the `markdown` module, this is the cleanest way:

```python
import markdown

def view_md_file(path):
    with open(path, "r", encoding="utf-8") as f:
        md_text = f.read()

    html = markdown.markdown(md_text)
    print(html)


# Example
view_md_file("example.md")
```

If `markdown` is not installed, you can add it later with:

```
pip install markdown
```

But you said **no installs**, so Version 1 works everywhere.

---

# 🟧 **Version 3 — Turn Markdown into an HTML file viewer (opens in browser)**  
This creates an HTML file and opens it automatically.

```python
import webbrowser
import tempfile

def md_to_html(md_text):
    html = md_text.replace("\n", "<br>")
    return f"<html><body>{html}</body></html>"

def view_md_file(path):
    with open(path, "r", encoding="utf-8") as f:
        md_text = f.read()

    html = md_to_html(md_text)

    tmp = tempfile.NamedTemporaryFile(delete=False, suffix=".html")
    tmp.write(html.encode("utf-8"))
    tmp.close()

    webbrowser.open(tmp.name)


# Example
view_md_file("example.md")
```

This opens the rendered HTML in your default browser.

---

If you want, I can help you add:  
- syntax highlighting  
- navigation sidebar  
- live reload  
- a GUI viewer (Tkinter / PyQt)  

Just tell me the style you want.
