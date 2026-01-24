import curses
import subprocess

MENU_ITEMS = [
    "Logs",
    "Docker Container List",
    "Show Files",
    "Memory Status",
    "CPU Use",
    "Exit"
]



# -----------------------------
# Login & Logout Screens
# -----------------------------

def login_screen(stdscr):
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

def msg(stdscr, text):
    stdscr.clear()
    h, w = stdscr.getmaxyx()
    # text = "WELCOME TO SYSTEM DASHBOARD"
    stdscr.addstr(h//2, (w - len(text))//2, text)
    stdscr.refresh()
    curses.napms(2000)  # 2 seconds



# -----------------------------
# utilities function
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
# Main program
# -----------------------------

def main(stdscr):
    curses.curs_set(0)
    curses.init_pair(1, curses.COLOR_BLACK, curses.COLOR_CYAN)

    # Show welcome screen
    login_screen(stdscr)
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
                # show_output(stdscr, "Logs", run_cmd("dmesg | tail -n 30"))
                # print(f'Logs')
                msg(stdscr, 'Logs - please wait')

            elif choice == "Docker Container List":
                #show_output(stdscr, "Docker Containers", run_cmd("docker ps -a"))
                print(f'Docker Container List')

            elif choice == "Show Files":
                #show_output(stdscr, "Files", run_cmd("ls -lh"))
                print(f'Show Files')

            elif choice == "Memory Status":
                #live_memory_status(stdscr)
                print(f'Memory Status')

            elif choice == "CPU Use":
                #live_cpu_status(stdscr)
                print(f'CPU Use')

            elif choice == "Exit":
                logout_screen(stdscr)
                break


        stdscr.refresh()

curses.wrapper(main)
