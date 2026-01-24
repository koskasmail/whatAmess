import curses
import time

# Navbar items
menus = ["Menu A", "Menu B", "Menu C"]

# Submenus for each menu
submenus = {
    "Menu A": ["A1 Option", "A2 Option", "A3 Option"],
    "Menu B": ["B1 Option", "B2 Option"],
    "Menu C": ["C1 Option", "C2 Option", "C3 Option", "C4 Option"]
}

def draw_center(stdscr, text):
    h, w = stdscr.getmaxyx()
    x = w//2 - len(text)//2
    y = h//2
    stdscr.addstr(y, x, text)

def show_message(stdscr, text):
    stdscr.clear()
    draw_center(stdscr, text)
    stdscr.refresh()
    time.sleep(2)

def submenu_screen(stdscr, menu_name):
    items = submenus[menu_name]
    index = 0

    while True:
        stdscr.clear()
        stdscr.addstr(1, 2, f"{menu_name} Submenu (← Back)")

        for i, item in enumerate(items):
            if i == index:
                stdscr.addstr(3 + i, 4, f"> {item}", curses.A_REVERSE)
            else:
                stdscr.addstr(3 + i, 4, f"  {item}")

        stdscr.refresh()

        key = stdscr.getch()

        if key == curses.KEY_UP:
            index = (index - 1) % len(items)
        elif key == curses.KEY_DOWN:
            index = (index + 1) % len(items)
        elif key in [curses.KEY_RIGHT, curses.KEY_ENTER, 10, 13]:
            show_message(stdscr, f"You selected: {items[index]}")
            return
        elif key == curses.KEY_LEFT:
            return

def main(stdscr):
    curses.curs_set(0)
    stdscr.clear()

    # Welcome screen
    draw_center(stdscr, "WELCOME")
    stdscr.refresh()
    time.sleep(2)

    index = 0

    while True:
        stdscr.clear()

        # Draw navbar
        stdscr.addstr(1, 2, "Use ← → to choose menu, ENTER to open, Q to quit")

        for i, menu in enumerate(menus):
            if i == index:
                stdscr.addstr(3, 4 + i*12, f"[ {menu} ]", curses.A_REVERSE)
            else:
                stdscr.addstr(3, 4 + i*12, f"[ {menu} ]")

        stdscr.refresh()

        key = stdscr.getch()

        if key == curses.KEY_RIGHT:
            index = (index + 1) % len(menus)
        elif key == curses.KEY_LEFT:
            index = (index - 1) % len(menus)
        elif key in [curses.KEY_ENTER, 10, 13]:
            submenu_screen(stdscr, menus[index])
        elif key in [ord('q'), ord('Q')]:
            break

    # Goodbye screen
    stdscr.clear()
    draw_center(stdscr, "GOODBYE")
    stdscr.refresh()
    time.sleep(2)

curses.wrapper(main)
