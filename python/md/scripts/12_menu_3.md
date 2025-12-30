Absolutely — let’s upgrade your **Memory screen** so it shows:

### ✔ Total memory  
### ✔ Used memory  
### ✔ Free memory  
### ✔ **Memory percentage used**  
### ✔ A text‑graph bar (like the CPU screen)  
### ✔ Auto‑refresh every 1 second  

Below is a **drop‑in replacement** for your existing `show_memory_screen()` function.  
You don’t need to change anything else in your program.

---

# ✅ **Improved Memory Screen (with percentage + text graph + auto‑refresh)**

```python
def show_memory_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    while True:
        try:
            with open("/proc/meminfo") as f:
                lines = f.readlines()
        except Exception as e:
            stdscr.clear()
            stdscr.addstr(0, 0, f"Cannot read /proc/meminfo: {e}")
            stdscr.getch()
            stdscr.nodelay(False)
            return

        meminfo = {}
        for line in lines:
            parts = line.split()
            key = parts[0].rstrip(":")
            value = int(parts[1])  # kB
            meminfo[key] = value

        total = meminfo.get("MemTotal", 0) // 1024
        available = meminfo.get("MemAvailable", 0) // 1024
        used = total - available

        # Memory percentage
        percent = (used / total) * 100 if total > 0 else 0

        # Build text graph (50 chars wide)
        bar_width = 50
        filled = int((percent / 100.0) * bar_width)
        bar = "[" + "#" * filled + "-" * (bar_width - filled) + "]"

        # Draw screen
        stdscr.clear()
        stdscr.addstr(0, 0, "Memory Status (press q to return)")
        stdscr.addstr(2, 0, f"Total: {total} MB")
        stdscr.addstr(3, 0, f"Used : {used} MB")
        stdscr.addstr(4, 0, f"Free : {available} MB")
        stdscr.addstr(6, 0, f"Memory Usage: {percent:.2f}%")
        stdscr.addstr(8, 0, bar)
        stdscr.refresh()

        # Quit
        key = stdscr.getch()
        if key == ord("q"):
            stdscr.nodelay(False)
            break

        time.sleep(1)
```

---

# ⭐ What this adds

### ✔ Memory percentage  
Example:

```
Memory Usage: 42.18%
```

### ✔ Text graph bar  
Example:

```
[######################------------------------]
```

### ✔ Auto‑refresh every second  
Just like the CPU screen.

---

If you want, I can also add:

- Color bars (green/yellow/red depending on usage)  
- A combined CPU+Memory dashboard  
- Disk usage screen  
- Network usage screen  
- System uptime  

Just tell me what you want next.
