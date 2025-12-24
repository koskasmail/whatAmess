import tkinter as tk
from tkinter import messagebox

def show_result():
    name = name_entry.get()
    phone = phone_entry.get()
    address = address_entry.get()

    result = f"Name: {name}\nPhone: {phone}\nAddress: {address}"
    messagebox.showinfo("Result", result)

def clear_fields():
    name_entry.delete(0, tk.END)
    phone_entry.delete(0, tk.END)
    address_entry.delete(0, tk.END)

def exit_app():
    root.destroy()

root = tk.Tk()
root.title("User Info Form")
root.geometry("300x250")

# Labels
tk.Label(root, text="Name:").pack()
name_entry = tk.Entry(root)
name_entry.pack()

tk.Label(root, text="Phone:").pack()
phone_entry = tk.Entry(root)
phone_entry.pack()

tk.Label(root, text="Address:").pack()
address_entry = tk.Entry(root)
address_entry.pack()

# Buttons
tk.Button(root, text="Show Result", command=show_result).pack(pady=5)
tk.Button(root, text="Clear", command=clear_fields).pack(pady=5)
tk.Button(root, text="Exit", command=exit_app).pack(pady=5)

root.mainloop()
