Absolutely — let’s upgrade your **CPU screen** so it shows:

### ✔ Live auto‑refresh (every 1 second)  
### ✔ CPU percentage  
### ✔ A running “activity bar” (text graph)  
### ✔ A moving animation so the user sees it’s alive  

Below is a **drop‑in replacement** for your existing `show_cpu_screen()` function.  
You don’t need to change anything else in your program.

---

# ✅ **Improved CPU Screen (with text graph + running animation)**

```python
def show_cpu_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    idle_prev, total_prev = read_cpu_times()
    anim = ["|", "/", "-", "\\"]   # running animation
    anim_index = 0

    while True:
        time.sleep(1)

        # Read CPU times again
        idle_curr, total_curr = read_cpu_times()
        idle_delta = idle_curr - idle_prev
        total_delta = total_curr - total_prev
        idle_prev, total_prev = idle_curr, total_curr

        # Calculate CPU usage
        usage = 0.0
        if total_delta > 0:
            usage = 100.0 * (1.0 - idle_delta / total_delta)

        # Build text graph (50 chars wide)
        bar_width = 50
        filled = int((usage / 100.0) * bar_width)
        bar = "[" + "#" * filled + "-" * (bar_width - filled) + "]"

        # Running animation
        spinner = anim[anim_index]
        anim_index = (anim_index + 1) % len(anim)

        # Draw screen
        stdscr.clear()
        stdscr.addstr(0, 0, "CPU Status (press q to return)")
        stdscr.addstr(2, 0, f"CPU Usage: {usage:.2f}%   {spinner}")
        stdscr.addstr(4, 0, bar)
        stdscr.refresh()

        # Quit
        key = stdscr.getch()
        if key == ord("q"):
            stdscr.nodelay(False)
            break
```

---

# ⭐ What this adds

### ✔ **Text graph bar**
Example:

```
[###############-------------------------------]
```

### ✔ **Live animation**
A spinner (`| / - \`) rotates every second so the user sees the screen is updating.

### ✔ **Smooth auto‑refresh**
Updates every 1 second without blocking.

### ✔ **No external modules**
Still uses only `/proc/stat`.

---

# Want to go further?

I can add:

- Color bars (green/yellow/red depending on load)
- Multi‑core CPU graphs
- A scrolling history graph (like htop)
- CPU temperature (if available)
- A combined system dashboard screen

Just tell me what direction you want to take it.
