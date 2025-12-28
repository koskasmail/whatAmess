You already had an Exit option in the menu, but here is a **cleaner, improved version** with a proper exit path and clearer structure. This version ensures the program stops immediately when the user chooses Exit.

---

# 🟦 Updated Python Menu (with proper Exit)

```python
import os
import stat

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

def menu():
    while True:
        print("\n=== MENU ===")
        print("1. Show files in folder")
        print("2. Change folder")
        print("3. View file permissions")
        print("4. Exit program")

        choice = input("Choose an option: ")

        if choice == "1":
            show_files()
        elif choice == "2":
            change_folder()
        elif choice == "3":
            view_permissions()
        elif choice == "4":
            print("Exiting program...")
            break
        else:
            print("Invalid choice")

menu()
```

---

# 🟩 What changed?

- The Exit option now clearly says **Exit program**.
- When selected, it prints a message and **breaks the loop**, ending the script immediately.
- Everything is organized and easy to extend.

---

If you want, I can help you add:

- colors  
- a "go back" option  
- file preview  
- delete/rename options  
- arrow‑key navigation (curses UI)

Just tell me what direction you want to take it.
