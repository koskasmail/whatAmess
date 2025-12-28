Absolutely — adding **Page Up**, **Page Down**, **Home**, and **End** support will make your file viewer feel much more like a real terminal pager.

Below is the **updated `view_file()` function only**, ready to drop into your full script.  
Everything else stays the same.

---

# ✅ **Updated File Viewer With Full Navigation**
Supports:

- **↑** scroll up 1 line  
- **↓** scroll down 1 line  
- **Page Up** scroll up 1 screen  
- **Page Down** scroll down 1 screen  
- **Home** jump to top  
- **End** jump to bottom  
- **q** quit viewer  

```python
def view_file(stdscr, path):
    curses.curs_set(0)
    stdscr.clear()

    try:
        with open(path, "r", encoding="utf-8", errors="ignore") as f:
            lines = f.readlines()
    except Exception as e:
        stdscr.addstr(0, 0, f"Cannot open file: {e}")
        stdscr.getch()
        return

    top = 0
    h, w = stdscr.getmaxyx()
    max_lines = h - 2  # usable lines

    while True:
        stdscr.clear()
        title = f"Viewing: {path}  (↑↓ scroll, PgUp/PgDn, Home/End, q=back)"
        stdscr.addstr(0, 0, title[:w-1])

        # Draw visible lines
        for i in range(max_lines):
            if top + i < len(lines):
                stdscr.addstr(i + 1, 0, lines[top + i][:w-1])

        key = stdscr.getch()

        # Quit
        if key == ord("q"):
            break

        # Scroll 1 line
        elif key == curses.KEY_DOWN:
            if top < len(lines) - max_lines:
                top += 1

        elif key == curses.KEY_UP:
            if top > 0:
                top -= 1

        # Page Down
        elif key == curses.KEY_NPAGE:  # Page Down
            top = min(top + max_lines, len(lines) - max_lines)

        # Page Up
        elif key == curses.KEY_PPAGE:  # Page Up
            top = max(top - max_lines, 0)

        # Home → jump to top
        elif key == curses.KEY_HOME:
            top = 0

        # End → jump to bottom
        elif key == curses.KEY_END:
            top = max(0, len(lines) - max_lines)
```

---

# ⭐ What this adds

| Key | Action |
|-----|--------|
| ↑ | Scroll up 1 line |
| ↓ | Scroll down 1 line |
| Page Up | Scroll up 1 full screen |
| Page Down | Scroll down 1 full screen |
| Home | Jump to top of file |
| End | Jump to bottom of file |
| q | Quit viewer |

This makes your viewer behave like **less**, but inside your own TUI.

---

If you want, I can also add:

- search inside file (`/` like in `less`)  
- highlight search results  
- horizontal scrolling  
- line numbers  
- color themes  

Just tell me what direction you want to take it.
