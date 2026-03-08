הקוד שלך כבר כולל **Add** ו‑**Delete**, ועכשיו אתה רוצה להוסיף גם **Edit** (כפתור + מחלקה/חלון עריכה).  
החדשות הטובות: רוב התשתית כבר קיימת אצלך — יש לך אפילו פונקציה `editRow()` מוכנה, רק שלא מחוברת לשום כפתור.

בוא נעשה סדר ונוסיף את זה בצורה נקייה וברורה.

---

# ✨ מה צריך לעשות?
## 1) להוסיף כפתור Edit למסך הראשי  
בקטע שבו אתה יוצר את הכפתורים:

```java
editBtn = new JButton("Edit");
editBtn.setToolTipText("Edit selected record");
editBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("editBtn");
        editRecord();
    }
});
jpfrm1.add(editBtn);
```

## 2) למקם אותו יחד עם שאר הכפתורים  
אם יש לך 3 כפתורים, עכשיו יהיו 4.  
תעדכן את החישוב:

```java
int totalWidth = (btnWidth * 4) + (20 * 3);
```

ותוסיף:

```java
editBtn.setBounds(startX + (btnWidth + 20) * 1, y, btnWidth, btnHeight);
```

(ותזיז Delete ו‑Exit אחד ימינה)

---

# ✨ 3) להוסיף פונקציה editRecord()  
יש לך כבר פונקציה `editRow()`, אבל היא סטטית ועובדת עם `model`.  
כדאי ליישר קו עם שאר הקוד:

```java
private void editRecord() {
    int selectedRow = errorTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(frame, "Please select a row to edit.");
        return;
    }

    ErrorRecord record = tableModel.getRecords().get(selectedRow);

    EditErrorDialog dialog = new EditErrorDialog(frame, record);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        tableModel.updateRecord(selectedRow, dialog.getRecord());
        JsonUtil.save(tableModel.getRecords());
    }
}
```

---

# ✨ 4) ליצור מחלקת EditErrorDialog (כמו AddErrorDialog)

דוגמה פשוטה:

```java
public class EditErrorDialog extends JDialog {

    private boolean confirmed = false;
    private JTextField nameField;
    private JTextField pathField;
    private ErrorRecord record;

    public EditErrorDialog(JFrame parent, ErrorRecord record) {
        super(parent, "Edit Record", true);
        this.record = record;

        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Name:"));
        nameField = new JTextField(record.getName());
        add(nameField);

        add(new JLabel("Path:"));
        pathField = new JTextField(record.getPath());
        add(pathField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            record.setName(nameField.getText());
            record.setPath(pathField.getText());
            confirmed = true;
            dispose();
        });

        add(new JLabel());
        add(saveBtn);

        setSize(300, 150);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public ErrorRecord getRecord() {
        return record;
    }
}
```

---

# ✨ 5) לעדכן את ErrorTableModel  
תוסיף:

```java
public void updateRecord(int index, ErrorRecord record) {
    records.set(index, record);
    fireTableRowsUpdated(index, index);
}
```

---

# 🎉 וזהו — יש לך Add, Delete, Edit מלאים!

אם תרצה, אני יכול גם:
- לייצר לך UI מודרני יותר  
- להוסיף אייקונים לכפתורים  
- לעבור ל‑Layout Manager נורמלי במקום `null`  
- להפוך את כל המערכת ל‑MVC מסודר  

רוצה שאשדרג את זה?
