import curses
import subprocess
import time
import os

# -----------------------------
# Helper: run OS command
# -----------------------------
def run_command(stdscr, cmd):
    stdscr.clear()
    stdscr.addstr(1, 2, f"Running: {cmd}\n\n")
    stdscr.refresh()

    try:
        output = subprocess.check_output(cmd, shell=True, stderr=subprocess.STDOUT, text=True)
    except Exception as e:
        output = str(e)

    # Display output
    stdscr.addstr(3, 2, output)
    stdscr.addstr(curses.LINES - 2, 2, "Press any key to return...")
    stdscr.refresh()
    stdscr.getch()


# -----------------------------
# Submenu for OS commands
# -----------------------------
def submenu_os(stdscr):
    options = [
        ("Show TOP (1 second)", "top"),
        ("Show Disk Usage (df -h)", "df -h"),
        ("Show Free Memory", "free -m"),
        ("Show Disk Space (du -sh ./)", "du -sh ./"),
        ("Back", None)
    ]

    index = 0

    while True:
        stdscr.clear()
        stdscr.addstr(1, 2, "OS Commands Submenu (Use ↑ ↓, ENTER)")

        for i, (label, _) in enumerate(options):
            if i == index:
                stdscr.addstr(3 + i, 4, f"> {label}", curses.A_REVERSE)
            else:
                stdscr.addstr(3 + i, 4, f"  {label}")

        stdscr.refresh()
        key = stdscr.getch()

        if key == curses.KEY_UP:
            index = (index - 1) % len(options)
        elif key == curses.KEY_DOWN:
            index = (index + 1) % len(options)
        elif key in [10, 13]:  # ENTER
            label, cmd = options[index]
            if cmd is None:
                return
            run_command(stdscr, cmd)


# -----------------------------
# Main Menu
# -----------------------------
def main_menu(stdscr):
    curses.curs_set(0)

    menu = [
        "OS Commands",
        "Placeholder Menu 2",
        "Placeholder Menu 3",
        "Exit"
    ]

    index = 0

    while True:
        stdscr.clear()
        stdscr.addstr(1, 2, "Main Menu (Use ← → ↑ ↓, ENTER)")

        # Draw menu items horizontally
        x = 4
        for i, item in enumerate(menu):
            if i == index:
                stdscr.addstr(3, x, f"[ {item} ]", curses.A_REVERSE)
            else:
                stdscr.addstr(3, x, f"[ {item} ]")
            x += len(item) + 8

        stdscr.refresh()
        key = stdscr.getch()

        if key == curses.KEY_RIGHT:
            index = (index + 1) % len(menu)
        elif key == curses.KEY_LEFT:
            index = (index - 1) % len(menu)
        elif key in [10, 13]:  # ENTER
            if menu[index] == "OS Commands":
                submenu_os(stdscr)
            elif menu[index] == "Exit":
                break


# -----------------------------
# Entry point
# -----------------------------
def main(stdscr):
    stdscr.clear()
    stdscr.addstr(curses.LINES // 2, (curses.COLS // 2) - 4, "WELCOME")
    stdscr.refresh()
    time.sleep(1)

    main_menu(stdscr)

    stdscr.clear()
    stdscr.addstr(curses.LINES // 2, (curses.COLS // 2) - 3, "GOODBYE")
    stdscr.refresh()
    time.sleep(1)


curses.wrapper(main)
