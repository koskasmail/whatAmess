Here’s a **single complete Python program** with all the features we built so far:

- Full curses TUI
- Main menu (navigate with ↑ ↓ and Enter)
- File browser:
  - Shows entries like `ls -lih` (inode, perms, links, user, group, size, date, name)
  - Dirs and files, sorted (dirs first)
  - `..` to go up
  - Move with ↑ ↓, open with Enter
- File viewer:
  - Scroll with ↑ ↓
  - Page Up / Page Down
  - Home = jump to top
  - End = jump to bottom
  - If file is shorter than one page: scrolling keys are disabled (only `q` works)
- Time & Date screen with auto-refresh (1 second)
- CPU status screen using `/proc/stat`, auto-refresh (1 second)
- Memory status screen using `/proc/meminfo`, auto-refresh (1 second)
- All without external modules

Save as `tui_manager.py` and run with:

```bash
python3 tui_manager.py
```

---

### Full program

```python
import os
import stat
import curses
import time
import pwd
import grp

# ==============================
# Helpers
# ==============================

def format_mode(mode):
    """Create a permission string similar to ls -l."""
    is_dir = "d" if stat.S_ISDIR(mode) else "-"
    perms = ""
    # user, group, other (rwx)
    perms_bits = [
        stat.S_IRUSR, stat.S_IWUSR, stat.S_IXUSR,
        stat.S_IRGRP, stat.S_IWGRP, stat.S_IXGRP,
        stat.S_IROTH, stat.S_IWOTH, stat.S_IXOTH,
    ]
    perm_chars = "rwxrwxrwx"
    for bit, ch in zip(perms_bits, perm_chars):
        perms += ch if (mode & bit) else "-"
    return is_dir + perms

def read_cpu_times():
    """Read /proc/stat first line and return (idle, total)."""
    with open("/proc/stat", "r") as f:
        line = f.readline()
    parts = line.split()[1:]
    nums = list(map(int, parts))
    idle = nums[3]
    total = sum(nums)
    return idle, total

# ==============================
# File viewer
# ==============================

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
    max_lines = h - 2  # usable lines under title
    small_file = len(lines) <= max_lines

    while True:
        stdscr.clear()
        title = f"Viewing: {path}  (↑/↓, PgUp/PgDn, Home/End, q=back)"
        stdscr.addstr(0, 0, title[:w-1])

        for i in range(max_lines):
            if top + i < len(lines):
                stdscr.addstr(i + 1, 0, lines[top + i][:w-1])

        key = stdscr.getch()

        # Quit
        if key == ord("q"):
            break

        # If file fits on one screen, ignore scroll keys
        if small_file:
            continue

        # Scroll 1 line
        if key == curses.KEY_DOWN:
            if top < len(lines) - max_lines:
                top += 1

        elif key == curses.KEY_UP:
            if top > 0:
                top -= 1

        # Page Down
        elif key == curses.KEY_NPAGE:  # Page Down
            top = min(top + max_lines, max(0, len(lines) - max_lines))

        # Page Up
        elif key == curses.KEY_PPAGE:  # Page Up
            top = max(top - max_lines, 0)

        # Home (multiple possible key codes depending on terminal)
        elif key in (curses.KEY_HOME, 262, 1):
            top = 0

        # End (multiple possible key codes depending on terminal)
        elif key in (curses.KEY_END, 360, 5):
            top = max(0, len(lines) - max_lines)

# ==============================
# File browser
# ==============================

def get_dir_entries(path):
    entries = []
    try:
        names = os.listdir(path)
    except PermissionError:
        names = []

    for name in names:
        full = os.path.join(path, name)
        try:
            st = os.lstat(full)
        except OSError:
            continue

        mode = format_mode(st.st_mode)
        nlink = st.st_nlink
        try:
            user = pwd.getpwuid(st.st_uid).pw_name
        except KeyError:
            user = str(st.st_uid)
        try:
            group = grp.getgrgid(st.st_gid).gr_name
        except KeyError:
            group = str(st.st_gid)
        size = st.st_size
        mtime = time.strftime("%Y-%m-%d %H:%M", time.localtime(st.st_mtime))
        inode = st.st_ino

        entries.append({
            "name": name,
            "full": full,
            "is_dir": stat.S_ISDIR(st.st_mode),
            "inode": inode,
            "mode": mode,
            "nlink": nlink,
            "user": user,
            "group": group,
            "size": size,
            "mtime": mtime,
        })

    # Sort: dirs first, then files, alphabetically
    entries.sort(key=lambda e: (not e["is_dir"], e["name"].lower()))
    return entries

def file_browser(stdscr, start_path="."):
    curses.curs_set(0)
    current_path = os.path.abspath(start_path)

    while True:
        entries = get_dir_entries(current_path)
        # Add .. at top
        entries_with_parent = [{
            "name": "..",
            "full": os.path.dirname(current_path),
            "is_dir": True,
            "inode": 0,
            "mode": "drwxr-xr-x",
            "nlink": 1,
            "user": "",
            "group": "",
            "size": 0,
            "mtime": ""
        }] + entries

        index = 0

        while True:
            stdscr.clear()
            h, w = stdscr.getmaxyx()
            title = f"Browsing: {current_path}  (↑/↓ move, ENTER open, q=back)"
            stdscr.addstr(0, 0, title[:w-1])

            header = "inode      perms       links  user    group   size      date           name"
            stdscr.addstr(1, 0, header[:w-1])

            max_rows = h - 3
            start = max(0, min(index - max_rows + 1, len(entries_with_parent) - max_rows))

            for i in range(max_rows):
                pos = start + i
                if pos >= len(entries_with_parent):
                    break
                e = entries_with_parent[pos]
                line = (
                    f"{str(e['inode']).ljust(10)} "
                    f"{e['mode'].ljust(10)} "
                    f"{str(e['nlink']).ljust(5)} "
                    f"{e['user'][:8].ljust(8)} "
                    f"{e['group'][:8].ljust(8)} "
                    f"{str(e['size']).ljust(9)} "
                    f"{e['mtime'].ljust(16)} "
                    f"{e['name']}"
                )
                if pos == index:
                    stdscr.addstr(i + 2, 0, line[:w-1], curses.A_REVERSE)
                else:
                    stdscr.addstr(i + 2, 0, line[:w-1])

            key = stdscr.getch()

            if key == curses.KEY_UP and index > 0:
                index -= 1
            elif key == curses.KEY_DOWN and index < len(entries_with_parent) - 1:
                index += 1
            elif key == ord("q"):
                return
            elif key == ord("\n"):
                selected = entries_with_parent[index]
                if selected["name"] == "..":
                    current_path = selected["full"]
                    break
                elif selected["is_dir"]:
                    current_path = selected["full"]
                    break
                else:
                    view_file(stdscr, selected["full"])
                    # After viewing, stay in same dir and re-list
                    break

# ==============================
# Info screens (auto-refresh)
# ==============================

def show_time_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)  # non-blocking

    while True:
        stdscr.clear()
        h, w = stdscr.getmaxyx()

        stdscr.addstr(0, 0, "Current Time & Date (press q to return)")
        now = time.strftime("%Y-%m-%d %H:%M:%S")
        stdscr.addstr(2, 0, now[:w-1])
        stdscr.refresh()

        key = stdscr.getch()
        if key == ord("q"):
            stdscr.nodelay(False)
            break

        time.sleep(1)

def show_cpu_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    idle_prev, total_prev = read_cpu_times()

    while True:
        time.sleep(1)
        idle_curr, total_curr = read_cpu_times()
        idle_delta = idle_curr - idle_prev
        total_delta = total_curr - total_prev
        idle_prev, total_prev = idle_curr, total_curr

        usage = 0.0
        if total_delta > 0:
            usage = 100.0 * (1.0 - idle_delta / total_delta)

        stdscr.clear()
        stdscr.addstr(0, 0, "CPU Status (press q to return)")
        stdscr.addstr(2, 0, f"CPU Usage: {usage:.2f}%")
        stdscr.refresh()

        key = stdscr.getch()
        if key == ord("q"):
            stdscr.nodelay(False)
            break

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

        stdscr.clear()
        stdscr.addstr(0, 0, "Memory Status (press q to return)")
        stdscr.addstr(2, 0, f"Total: {total} MB")
        stdscr.addstr(3, 0, f"Used : {used} MB")
        stdscr.addstr(4, 0, f"Free : {available} MB")
        stdscr.refresh()

        key = stdscr.getch()
        if key == ord("q"):
            stdscr.nodelay(False)
            break

        time.sleep(1)

# ==============================
# Main menu
# ==============================

def main_menu(stdscr):
    curses.curs_set(0)
    curses.use_default_colors()
    stdscr.keypad(True)

    options = [
        "Browse files and folders",
        "Show time and date",
        "Show CPU status",
        "Show memory status",
        "Exit"
    ]

    index = 0

    while True:
        stdscr.clear()
        h, w = stdscr.getmaxyx()
        title = "MAIN MENU (Use ↑/↓ to move, ENTER to select)"
        stdscr.addstr(0, 0, title[:w-1])

        for i, opt in enumerate(options):
            line = opt
            if i == index:
                stdscr.addstr(i + 2, 2, line[:w-4], curses.A_REVERSE)
            else:
                stdscr.addstr(i + 2, 2, line[:w-4])

        key = stdscr.getch()
        if key == curses.KEY_UP and index > 0:
            index -= 1
        elif key == curses.KEY_DOWN and index < len(options) - 1:
            index += 1
        elif key == ord("\n"):
            choice = options[index]
            if choice == "Browse files and folders":
                file_browser(stdscr, ".")
            elif choice == "Show time and date":
                show_time_screen(stdscr)
            elif choice == "Show CPU status":
                show_cpu_screen(stdscr)
            elif choice == "Show memory status":
                show_memory_screen(stdscr)
            elif choice == "Exit":
                break

# ==============================
# Entry point
# ==============================

def main():
    curses.wrapper(main_menu)

if __name__ == "__main__":
    main()
```

---

If you want to keep evolving this (colors, delete/rename files, search in viewer, etc.), tell me what feels like the next step for you.
