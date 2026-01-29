
# file_02.md

Got it! You want to replace the `JList` with a `JTable` to display the error records in a tabular format. Below is the updated code that uses `JTable` instead of a `JList`. We’ll use a custom `TableModel` to manage the data.

Here’s the step-by-step modification for using a `JTable`:

### 1️⃣ Data Model (`ErrorRecord.java`)

No changes needed here.

```java
public class ErrorRecord {
    private String message;
    private String severity;

    public ErrorRecord(String message, String severity) {
        this.message = message;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return severity + " - " + message;
    }
}
```

---

### 2️⃣ JSON Utility (`JsonUtil.java`)

No changes needed here either — it’s fine as is.

```java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtil {
    private static final String FILE_NAME = "errors.json";
    private static final Gson gson = new Gson();

    public static void save(ArrayList<ErrorRecord> list) {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ErrorRecord> load() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type type = new TypeToken<ArrayList<ErrorRecord>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
```

---

### 3️⃣ Custom Table Model (`ErrorTableModel.java`)

We will create a custom `TableModel` to work with `JTable`.

```java
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ErrorTableModel extends AbstractTableModel {

    private ArrayList<ErrorRecord> records;
    private final String[] columnNames = {"Severity", "Error Message"};

    public ErrorTableModel(ArrayList<ErrorRecord> records) {
        this.records = records;
    }

    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ErrorRecord record = records.get(rowIndex);
        if (columnIndex == 0) {
            return record.getSeverity();
        } else if (columnIndex == 1) {
            return record.getMessage();
        }
        return null;
    }

    public void addRecord(ErrorRecord record) {
        records.add(record);
        fireTableRowsInserted(records.size() - 1, records.size() - 1);
    }

    public void removeRecord(int rowIndex) {
        records.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public ArrayList<ErrorRecord> getRecords() {
        return records;
    }
}
```

---

### 4️⃣ Add Error Dialog (`AddErrorDialog.java`)

No changes needed for the dialog.

```java
import javax.swing.*;
import java.awt.*;

public class AddErrorDialog extends JDialog {

    private JTextField messageField;
    private JComboBox<String> severityBox;
    private boolean confirmed = false;

    public AddErrorDialog(JFrame parent) {
        super(parent, "Add Error", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Error Message:"));
        messageField = new JTextField();
        add(messageField);

        add(new JLabel("Severity:"));
        severityBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        add(severityBox);

        JButton addBtn = new JButton("Add");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!messageField.getText().trim().isEmpty()) {
                    confirmed = true;
                    setVisible(false);
                }
            }
        });

        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                messageField.setText("");
                severityBox.setSelectedIndex(0);
            }
        });

        add(addBtn);
        add(clearBtn);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public ErrorRecord getRecord() {
        return new ErrorRecord(
                messageField.getText(),
                severityBox.getSelectedItem().toString()
        );
    }
}
```

---

### 5️⃣ Main Window (`MainApp.java`)

This is where we make the biggest change, replacing the `JList` with a `JTable` and using the custom `TableModel`.

```java
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainApp extends JFrame {

    private ErrorTableModel tableModel;
    private JTable errorTable;
    private ArrayList<ErrorRecord> records;

    public MainApp() {
        setTitle("Error Manager");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        records = JsonUtil.load();

        tableModel = new ErrorTableModel(records);
        errorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(errorTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton exitBtn = new JButton("Exit");

        // Anonymous inner class for Add button action
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addRecord();
            }
        });

        // Anonymous inner class for Delete button action
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteRecord();
            }
        });

        // Anonymous inner class for Exit button action
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRecord() {
        AddErrorDialog dialog = new AddErrorDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            ErrorRecord record = dialog.getRecord();
            tableModel.addRecord(record);
            JsonUtil.save(tableModel.getRecords());
        }
    }

    private void deleteRecord() {
        int selectedRow = errorTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRecord(selectedRow);
            JsonUtil.save(tableModel.getRecords());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }
}
```

### Key changes in this version:

* Replaced the `JList` with a `JTable` to display the error records in tabular format.
* Used a custom `ErrorTableModel` class that extends `AbstractTableModel` to handle the data and table rendering.
* The table model is updated every time you add or delete a record.

---

### ✅ Features:

* **Table-based display** of error records.
* Add, delete error records from the table.
* The table has two columns: **Severity** and **Error Message**.
* **Persistence**: Data saved in a JSON file.
* **Java 8 compatibility** (with anonymous inner classes for event handling).

---

### How to Run:

1. **Add Gson dependency**:

   * If using **Maven**, add this to your `pom.xml`:

   ```xml
   <dependency>
       <groupId>com.google.code.gson</groupId>
       <artifactId>gson</artifactId>
       <version>2.10.1</version>
   </dependency>
   ```

   * Or manually add **Gson JAR** if not using Maven.

2. **Compile and Run**:

   * Use **Java 8** for compatibility.
   * Compile and run the application.

Let me know if you'd like to add more features like **editing records** or **sorting** the table!
