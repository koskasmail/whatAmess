import os
import time
import shutil

# Clear screen function
def clear():
    os.system("cls" if os.name == "nt" else "clear")

# Center text helper
def center(text):
    width = shutil.get_terminal_size().columns
    return text.center(width)

# Submenu action
def submenu_action(text):
    clear()
    print(center(text))
    time.sleep(2)
    clear()

# Navigation bar
def navbar():
    while True:
        clear()
        print(center("=== MAIN MENU ==="))
        print()
        print("1) Menu A")
        print("2) Menu B")
        print("3) Menu C")
        print("4) Exit")
        print()

        choice = input("Choose an option: ")

        if choice == "1":
            submenu_action("You selected submenu A")
        elif choice == "2":
            submenu_action("You selected submenu B")
        elif choice == "3":
            submenu_action("You selected submenu C")
        elif choice == "4":
            break
        else:
            submenu_action("Invalid choice")

# Main program
def main():
    clear()
    print()
    print(center("WELCOME"))
    print()
    time.sleep(2)

    navbar()

    clear()
    print()
    print(center("GOODBYE"))
    print()
    time.sleep(2)
    clear()

if __name__ == "__main__":
    main()
