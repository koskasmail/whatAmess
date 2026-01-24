#!/usr/bin/env python3
import os
import stat
import curses
import time
import pwd
import grp

import subprocess



# ============================================================
# Helpers
# ============================================================

def format_mode(mode):
    """Create a permission string similar to ls -l."""
    is_dir = "d" if stat.S_ISDIR(mode) else "-"
    perms = ""
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

def is_text_file(path, blocksize=512):
    """Return True if file looks like text, False if binary."""
    try:
        with open(path, "rb") as f:
            block = f.read(blocksize)
        if b"\0" in block:
            return False
        try:
            block.decode("utf-8")
            return True
        except UnicodeDecodeError:
            return False
    except:
        return False


# ============================================================
# File viewer
# ============================================================

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
    max_lines = h - 2
    small_file = len(lines) <= max_lines

    while True:
        stdscr.clear()
        title = f"Viewing: {path}  (↑/↓ PgUp/PgDn Home/End q=back)"
        stdscr.addstr(0, 0, title[:w-1])

        for i in range(max_lines):
            if top + i < len(lines):
                stdscr.addstr(i + 1, 0, lines[top + i][:w-1])

        key = stdscr.getch()

        if key == ord("q"):
            break

        if small_file:
            continue

        if key == curses.KEY_DOWN and top < len(lines) - max_lines:
            top += 1
        elif key == curses.KEY_UP and top > 0:
            top -= 1
        elif key == curses.KEY_NPAGE:
            top = min(top + max_lines, len(lines) - max_lines)
        elif key == curses.KEY_PPAGE:
            top = max(top - max_lines, 0)
        elif key in (curses.KEY_HOME, 262, 1):
            top = 0
        elif key in (curses.KEY_END, 360, 5):
            top = max(0, len(lines) - max_lines)

# ============================================================
# File browser
# ============================================================

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

    entries.sort(key=lambda e: (not e["is_dir"], e["name"].lower()))
    return entries

def file_browser(stdscr, start_path="."):
    curses.curs_set(0)
    current_path = os.path.abspath(start_path)

    while True:
        entries = get_dir_entries(current_path)
        entries = [{
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
            stdscr.addstr(0, 0, f"Browsing: {current_path} (↑/↓ ENTER q=back)")
            stdscr.addstr(1, 0, "inode      perms       links user   group  size      date           name")

            max_rows = h - 3
            start = max(0, min(index - max_rows + 1, len(entries) - max_rows))

            for i in range(max_rows):
                pos = start + i
                if pos >= len(entries):
                    break
                e = entries[pos]
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
            elif key == curses.KEY_DOWN and index < len(entries) - 1:
                index += 1
            elif key == ord("q"):
                return
            elif key == ord("\n"):
                selected = entries[index]
                if selected["name"] == "..":
                    current_path = selected["full"]
                    break
                elif selected["is_dir"]:
                    current_path = selected["full"]
                    break
                else:
                    if is_text_file(selected["full"]):
                        view_file(stdscr, selected["full"])
                    break


# ============================================================
# Time screen (3 progress bars)
# ============================================================

def show_time_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    bar_width = 50

    while True:
        stdscr.clear()
        now = time.localtime()
        time_str = time.strftime("%Y-%m-%d %H:%M:%S", now)

        hour = now.tm_hour
        minute = now.tm_min
        second = now.tm_sec

        hour_pct = (hour / 23) * 100
        min_pct = (minute / 59) * 100
        sec_pct = (second / 59) * 100

        def make_bar(pct):
            filled = int((pct / 100) * bar_width)
            return "[" + "#" * filled + "-" * (bar_width - filled) + "]"

        stdscr.addstr(0, 0, "Time Status (press q to return)")
        stdscr.addstr(2, 0, time_str)

        stdscr.addstr(4, 0, f"Hours   ({hour:02d}/23)   {hour_pct:6.2f}%")
        stdscr.addstr(5, 0, make_bar(hour_pct))

        stdscr.addstr(7, 0, f"Minutes ({minute:02d}/59) {min_pct:6.2f}%")
        stdscr.addstr(8, 0, make_bar(min_pct))

        stdscr.addstr(10, 0, f"Seconds ({second:02d}/59) {sec_pct:6.2f}%")
        stdscr.addstr(11, 0, make_bar(sec_pct))

        stdscr.refresh()

        if stdscr.getch() == ord("q"):
            stdscr.nodelay(False)
            break

        time.sleep(1)

# ============================================================
# CPU screen (percentage + animated bar)
# ============================================================

def show_cpu_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    idle_prev, total_prev = read_cpu_times()
    anim = ["|", "/", "-", "\\"]
    anim_index = 0
    bar_width = 50

    while True:
        time.sleep(1)

        idle_curr, total_curr = read_cpu_times()
        idle_delta = idle_curr - idle_prev
        total_delta = total_curr - total_prev
        idle_prev, total_prev = idle_curr, total_curr

        usage = 0.0
        if total_delta > 0:
            usage = 100.0 * (1.0 - idle_delta / total_delta)

        filled = int((usage / 100) * bar_width)
        bar = "[" + "#" * filled + "-" * (bar_width - filled) + "]"

        spinner = anim[anim_index]
        anim_index = (anim_index + 1) % len(anim)

        stdscr.clear()
        stdscr.addstr(0, 0, "CPU Status (press q to return)")
        stdscr.addstr(2, 0, f"CPU Usage: {usage:.2f}%   {spinner}")
        stdscr.addstr(4, 0, bar)
        stdscr.refresh()

        if stdscr.getch() == ord("q"):
            stdscr.nodelay(False)
            break

# ============================================================
# Memory screen (percentage + bar)
# ============================================================

def show_memory_screen(stdscr):
    curses.curs_set(0)
    stdscr.nodelay(True)

    bar_width = 50

    while True:
        try:
            with open("/proc/meminfo") as f:
                lines = f.readlines()
        except:
            stdscr.addstr(0, 0, "Cannot read /proc/meminfo")
            stdscr.getch()
            return

        meminfo = {}
        for line in lines:
            parts = line.split()
            meminfo[parts[0].rstrip(":")] = int(parts[1])

        total = meminfo["MemTotal"] // 1024
        available = meminfo["MemAvailable"] // 1024
        used = total - available
        pct = (used / total) * 100

        filled = int((pct / 100) * bar_width)
        bar = "[" + "#" * filled + "-" * (bar_width - filled) + "]"

        stdscr.clear()
        stdscr.addstr(0, 0, "Memory Status (press q to return)")
        stdscr.addstr(2, 0, f"Total: {total} MB")
        stdscr.addstr(3, 0, f"Used : {used} MB")
        stdscr.addstr(4, 0, f"Free : {available} MB")
        stdscr.addstr(6, 0, f"Memory Usage: {pct:.2f}%")
        stdscr.addstr(8, 0, bar)
        stdscr.refresh()

        if stdscr.getch() == ord("q"):
            stdscr.nodelay(MENU_ITEMSFalse)
            break

        time.sleep(1)



# -----------------------------
# Welcome & Logout Screens
# -----------------------------

def welcome_screen(stdscr):
    stdscr.clear()
    h, w = stdscr.getmaxyx()
    text = "WELCOME TO SYSTEM DASHBOARD"
    stdscr.addstr(h//2, (w - len(text))//2, text)
    stdscr.refresh()
    curses.napms(2000)  # 2 seconds

def logout_screen(stdscr):
    stdscr.clear()
    h, w = stdscr.getmaxyx()
    text = "LOGGING OUT... GOODBYE!"
    stdscr.addstr(h//2, (w - len(text))//2, text)
    stdscr.refresh()
    curses.napms(2000)  # 2 seconds

###----###---###---###---###---###---###---###---###---##


MENU_ITEMS = [
    "Logs",
    "Docker Container List",
    "Show Files",
    "Memory Status",
    "CPU Use",
    "Exit"
]

# -----------------------------
# Helper functions
# -----------------------------

def run_cmd(cmd):
    return subprocess.getoutput(cmd).split("\n")

def draw_menu(stdscr, selected):
    stdscr.clear()
    h, w = stdscr.getmaxyx()

    for idx, item in enumerate(MENU_ITEMS):
        x = idx * (w // len(MENU_ITEMS))
        if idx == selected:
            stdscr.attron(curses.color_pair(1))
            stdscr.addstr(0, x, item.center(w // len(MENU_ITEMS)))
            stdscr.attroff(curses.color_pair(1))
        else:
            stdscr.addstr(0, x, item.center(w // len(MENU_ITEMS)))

    stdscr.refresh()

def show_output(stdscr, title, lines):
    stdscr.clear()
    stdscr.addstr(0, 0, f"--- {title} ---\n\n")

    for i, line in enumerate(lines, start=2):
        if i >= curses.LINES - 1:
            break
        stdscr.addstr(i, 0, line)

    stdscr.addstr(curses.LINES - 1, 0, "Press any key to return...")
    stdscr.refresh()
    stdscr.getch()

# -----------------------------
# Live update screens
# -----------------------------

def live_memory_status(stdscr):
    stdscr.nodelay(True)

    while True:
        stdscr.clear()
        stdscr.addstr(0, 0, "--- Memory Status (updates every 2s, press 'q' to exit) ---\n\n")

        mem_lines = run_cmd("free -m")
        for i, line in enumerate(mem_lines, start=2):
            stdscr.addstr(i, 0, line)

        stdscr.addstr(8, 0, "--- Top Memory Processes ---")
        proc_lines = run_cmd("ps aux --sort=-%mem | head -n 15")
        for i, line in enumerate(proc_lines, start=9):
            stdscr.addstr(i, 0, line)

        stdscr.refresh()

        if stdscr.getch() == ord('q'):
            stdscr.nodelay(False)
            return

        curses.napms(2000)

def live_cpu_status(stdscr):
    stdscr.nodelay(True)

    while True:
        stdscr.clear()
        stdscr.addstr(0, 0, "--- CPU Status (updates every 2s, press 'q' to exit) ---\n\n")

        cpu_lines = run_cmd("top -bn1 | head -n 5")
        for i, line in enumerate(cpu_lines, start=2):
            stdscr.addstr(i, 0, line)

        stdscr.addstr(8, 0, "--- Top CPU Processes ---")
        proc_lines = run_cmd("ps aux --sort=-%cpu | head -n 15")
        for i, line in enumerate(proc_lines, start=9):
            stdscr.addstr(i, 0, line)

        stdscr.refresh()

        if stdscr.getch() == ord('q'):
            stdscr.nodelay(False)
            return

        curses.napms(2000)


# -----------------------------
# Main program
# -----------------------------

def main(stdscr):
    curses.curs_set(0)
    curses.init_pair(1, curses.COLOR_BLACK, curses.COLOR_CYAN)

    # Show welcome screen
    welcome_screen(stdscr)

    selected = 0

    while True:
        draw_menu(stdscr, selected)
        key = stdscr.getch()

        if key == curses.KEY_RIGHT:
            selected = (selected + 1) % len(MENU_ITEMS)
        elif key == curses.KEY_LEFT:
            selected = (selected - 1) % len(MENU_ITEMS)


        elif key in [curses.KEY_ENTER, 10, 13]:
            choice = MENU_ITEMS[selected]

            if choice == "Logs":
                show_output(stdscr, "Logs", run_cmd("dmesg | tail -n 30"))

            elif choice == "Docker Container List":
                show_output(stdscr, "Docker Containers", run_cmd("docker ps -a"))


            elif choice == "Show Files":
                show_output(stdscr, "Files", run_cmd("ls -lh"))

####
            elif choice == "Memory Status":
                live_memory_status(stdscr)

####
            elif choice == "CPU Use":
                live_cpu_status(stdscr)

####
            elif choice == "Exit":
                logout_screen(stdscr)
                break

        stdscr.refresh()

# curses.wrapper(main)


# ============================================================
# Entry point
# ============================================================

def main():
    curses.wrapper(main)

# ??
# if __name__ == "__main__":
#     main()
