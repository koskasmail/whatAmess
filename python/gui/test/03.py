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
root.geometry("350x250")

# --- Input rows ---
tk.Label(root, text="Name:").grid(row=0, column=0, padx=5, pady=5, sticky="e")
name_entry = tk.Entry(root, width=25)
name_entry.grid(row=0, column=1)

tk.Label(root, text="Phone:").grid(row=1, column=0, padx=5, pady=5, sticky="e")
phone_entry = tk.Entry(root, width=25)
phone_entry.grid(row=1, column=1)

tk.Label(root, text="Address:").grid(row=2, column=0, padx=5, pady=5, sticky="e")
address_entry = tk.Entry(root, width=25)
address_entry.grid(row=2, column=1)

# --- Buttons in one line ---
tk.Button(root, text="Show Result", command=show_result).grid(row=3, column=0, pady=10)
tk.Button(root, text="Clear", command=clear_fields).grid(row=3, column=1, sticky="w")
tk.Button(root, text="Exit", command=exit_app).grid(row=3, column=1, sticky="e")

# --- Result display (3 lines) ---
result_label_1 = tk.Label(root, text="", fg="blue")
result_label_1.grid(row=4, column=0, columnspan=2)

result_label_2 = tk.Label(root, text="", fg="blue")
result_label_2.grid(row=5, column=0, columnspan=2)

result_label_3 = tk.Label(root, text="", fg="blue")
result_label_3.grid(row=6, column=0, columnspan=2)

root.mainloop()
