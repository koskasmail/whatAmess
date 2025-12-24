import tkinter as tk

def show_result():
    name = name_entry.get()
    phone = phone_entry.get()
    address = address_entry.get()

    result_label_1.config(text=f"Name: {name}")
    result_label_2.config(text=f"Phone: {phone}")
    result_label_3.config(text=f"Address: {address}")

def clear_fields():
    name_entry.delete(0, tk.END)
    phone_entry.delete(0, tk.END)
    address_entry.delete(0, tk.END)

    result_label_1.config(text="")
    result_label_2.config(text="")
    result_label_3.config(text="")

def exit_app():
    root.destroy()

root = tk.Tk()
root.title("User Info Form")
root.geometry("300x300")

# Labels + Entries
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

# 3-line display area
result_label_1 = tk.Label(root, text="", fg="blue")
result_label_1.pack()

result_label_2 = tk.Label(root, text="", fg="blue")
result_label_2.pack()

result_label_3 = tk.Label(root, text="", fg="blue")
result_label_3.pack()

root.mainloop()
