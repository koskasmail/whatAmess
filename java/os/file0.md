To adapt the code for **Java 1.8**, we need to ensure that we're using features compatible with **Java 8**. Since Java 8 does not support some of the newer language features (like lambda expressions for method references in certain places), we will adjust the code accordingly.

Let's break down the changes for **Java 8** compatibility:

1. **DefaultListModel** and **JList** will still work.
2. **Gson** dependency is still fine to use.
3. We’ll remove **lambdas** and use **anonymous inner classes** instead, which were the norm in Java 8.
4. We'll handle file operations and GUI actions just as before, but we will use a bit more verbosity where lambdas were used.

Here’s the updated code that will work with **Java 8**:

---

### 1️⃣ Data Model (`ErrorRecord.java`)

This file doesn’t change. It’s fine for Java 8.

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

No changes here either. **Gson** library works with Java 8 just fine.

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

### 3️⃣ Add Error Dialog (`AddErrorDialog.java`)

We’ll use **anonymous inner classes** instead of lambdas for button actions:

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

        // Anonymous inner class for Add button action
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!messageField.getText().trim().isEmpty()) {
                    confirmed = true;
                    setVisible(false);
                }
            }
        });

        // Anonymous inner class for Clear button action
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

### 4️⃣ Main Window (`MainApp.java`)

This is the most involved change — we need to remove lambdas and use **anonymous inner classes** for action listeners. Additionally, `DefaultListModel` and `JList` will still work perfectly.

```java
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainApp extends JFrame {

    private DefaultListModel<ErrorRecord> listModel;
    private JList<ErrorRecord> errorList;
    private ArrayList<ErrorRecord> records;

    public MainApp() {
        setTitle("Error Manager");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        records = JsonUtil.load();

        listModel = new DefaultListModel<>();
        for (ErrorRecord record : records) {
            listModel.addElement(record);
        }

        errorList = new JList<>(listModel);
        add(new JScrollPane(errorList), BorderLayout.CENTER);

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
            records.add(record);
            listModel.addElement(record);
            JsonUtil.save(records);
        }
    }

    private void deleteRecord() {
        int index = errorList.getSelectedIndex();
        if (index != -1) {
            records.remove(index);
            listModel.remove(index);
            JsonUtil.save(records);
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

### Changes in this version:

* **Anonymous inner classes** replace **lambda expressions**.
* The `run` method in the `SwingUtilities.invokeLater` block uses an explicit `Runnable` instead of a lambda expression.

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

   * If not using Maven, download the **Gson JAR** from [here](https://mvnrepository.com/artifact/com.google.code.gson/gson) and add it to your `lib` folder.

2. **Compile and Run**:

   * Make sure you have **Java 8** set as your project JDK.
   * Compile the code and run it.

---

### ✅ Features:

* **Error List Management**

  * Add, Delete records.
  * Error records stored in a JSON file.
  * Each error has a severity and message.
* **Java 8 Compatible**
* **Persistent Data**
