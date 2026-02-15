Below is a clean, **fully integrated version** of your code with a **dynamic JTable** added to the panel (`jpfrm1`).  
The table:

- Has **two columns**: *Severity* and *Error Message*  
- Starts **empty**  
- Automatically **resizes** when the frame is resized  
- Sits **above your buttons**, filling all remaining space  
- Uses a `JScrollPane` so it expands dynamically

I’ll show you only the **added/modified parts**, then the **full combined block** so you can paste it directly.

---

# ✅ **1. Add JTable fields**
You already have:

```java
private ErrorTableModel tableModel;
private JTable errorTable;
private ArrayList<ErrorRecord> records;
```

Good — we will use them.

---

# ✅ **2. Create the JTable inside the constructor**
Add this **right after** you create `jpfrm1` and set its layout:

```java
// ----[Create Empty JTable]----
records = new ArrayList<>();

// Create your table model with 2 columns
tableModel = new ErrorTableModel(records);
tableModel.addColumn("Severity");
tableModel.addColumn("Error Message");

// Create JTable
errorTable = new JTable(tableModel);
errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

// Put table inside scroll pane
JScrollPane scrollPane = new JScrollPane(errorTable);
scrollPane.setBounds(10, 10, 500, 200); // temporary, will be dynamic

jpfrm1.add(scrollPane);
```

---

# ✅ **3. Make the table resize dynamically**
Inside your existing `componentResized` listener, add:

```java
scrollPane.setBounds(10, 10, w - 20, h - btnHeight - padding - 40);
```

This makes the table fill all available space above the buttons.

---

# ✅ **4. Full integrated code block (paste directly)**

Replace your `///----[jpfrm1]----///` section with this:

```java
///----[jpfrm1]----///		

jpfrm1 = new JPanel();
frame.getRootPane().add(jpfrm1);
jpfrm1.setLayout(null);

// ----[Create Empty JTable]----
records = new ArrayList<>();

// Create your table model with 2 columns
tableModel = new ErrorTableModel(records);
tableModel.addColumn("Severity");
tableModel.addColumn("Error Message");

// Create JTable
errorTable = new JTable(tableModel);
errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

// Put table inside scroll pane
JScrollPane scrollPane = new JScrollPane(errorTable);
scrollPane.setBounds(10, 10, 500, 200); // initial size
jpfrm1.add(scrollPane);

int w = frame.getContentPane().getWidth();
int h = frame.getContentPane().getHeight();

int btnWidth = 117;
int btnHeight = 25;
int padding = 20;

// center the 3 buttons at bottom
int totalWidth = (btnWidth * 3) + (20 * 2);
int startX = (w - totalWidth) / 2;
int y = h - btnHeight - padding;

///---[buttons]---///	       
addBtn = new JButton("Add");
jpfrm1.add(addBtn);

deleteBtn = new JButton("Delete");
jpfrm1.add(deleteBtn);

exitBtn = new JButton("Exit");
jpfrm1.add(exitBtn);

// dynamic bottom buttons + dynamic table
frame.addComponentListener(new java.awt.event.ComponentAdapter() {
    public void componentResized(java.awt.event.ComponentEvent evt) {

        int w = frame.getContentPane().getWidth();
        int h = frame.getContentPane().getHeight();

        int btnWidth = 117;
        int btnHeight = 25;
        int padding = 20;

        // center the 3 buttons at bottom
        int totalWidth = (btnWidth * 3) + (20 * 2);
        int startX = (w - totalWidth) / 2;
        int y = h - btnHeight - padding;

        addBtn.setBounds(startX, y, btnWidth, btnHeight);
        deleteBtn.setBounds(startX + btnWidth + 20, y, btnWidth, btnHeight);
        exitBtn.setBounds(startX + (btnWidth + 20) * 2, y, btnWidth, btnHeight);

        // Resize table area dynamically
        scrollPane.setBounds(10, 10, w - 20, h - btnHeight - padding - 40);
    }
});
```

---

# 🎉 **Your JTable is now fully integrated**
You now have:

- A **dynamic**, resizable JTable  
- Two columns: **Severity**, **Error Message**  
- An empty table ready for data  
- Buttons that stay centered at the bottom  
- A table that fills the rest of the window  

---

# ➕ Want to add rows dynamically?

Example:

```java
tableModel.addRow(new Object[]{"High", "File not found"});
```

If you want, I can also:

- Add row selection + delete logic  
- Add color coding (e.g., red for High severity)  
- Add double‑click editing  
- Load/save JSON using your `JsonUtil`  

Just tell me what direction you want to go.
