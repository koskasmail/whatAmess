Here’s a clean upgrade to your menu program:  
**a file viewer with smart scrolling**, meaning you can read the file page‑by‑page (like `less` in Linux) without loading everything at once.

This version uses **no external libraries** — just Python’s built‑ins.

---

# 🟦 Updated Menu With Smart‑Scroll File Viewer

```python
import os
import stat

PAGE_LINES = 20   # how many lines to show per scroll

def show_files():
    print("\nFiles in:", os.getcwd())
    for f in os.listdir():
        print(" -", f)

def change_folder():
    new_path = input("Enter folder path: ")
    try:
        os.chdir(new_path)
        print("Changed directory to:", os.getcwd())
    except Exception as e:
        print("Error:", e)

def view_permissions():
    filename = input("Enter file name: ")
    if not os.path.exists(filename):
        print("File does not exist")
        return

    st = os.stat(filename)
    permissions = stat.filemode(st.st_mode)
    print(f"Permissions for {filename}: {permissions}")

def view_file_content():
    filename = input("Enter file name: ")

    if not os.path.exists(filename):
        print("File does not exist")
        return

    try:
        with open(filename, "r", encoding="utf-8", errors="ignore") as f:
            lines = f.readlines()

        index = 0
        total = len(lines)

        while index < total:
            os.system("clear")  # or "cls" on Windows
            print(f"--- Viewing: {filename} ({index}/{total}) ---\n")

            for i in range(index, min(index + PAGE_LINES, total)):
                print(lines[i], end="")

            index += PAGE_LINES

            if index < total:
                cmd = input("\nPress ENTER to scroll, 'q' to quit: ")
                if cmd.lower() == "q":
                    break
            else:
                print("\n--- End of file ---")
                input("Press ENTER to return to menu")
    except Exception as e:
        print("Error reading file:", e)

def menu():
    while True:
        print("\n=== MENU ===")
        print("1. Show files in folder")
        print("2. Change folder")
        print("3. View file permissions")
        print("4. View file content (smart scroll)")
        print("5. Exit program")

        choice = input("Choose an option: ")

        if choice == "1":
            show_files()
        elif choice == "2":
            change_folder()
        elif choice == "3":
            view_permissions()
        elif choice == "4":
            view_file_content()
        elif choice == "5":
            print("Exiting program...")
            break
        else:
            print("Invalid choice")

menu()
```

---

# 🟩 What “smart scroll” does

✔ Shows the file **20 lines at a time**  
✔ Clears the screen between pages  
✔ Lets you press **ENTER to continue**  
✔ Press **q** to quit early  
✔ Works on any text file (including `.md`, `.txt`, `.py`, etc.)

---

If you want, I can help you add:

- search inside file  
- jump to top/bottom  
- scroll up as well as down  
- color syntax highlighting  
- a full TUI (terminal UI) with arrow keys  

Just tell me how advanced you want this viewer to become.
