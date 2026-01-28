Got it — classic Swing CRUD app with a popup dialog and JSON persistence 👍
Below is a **complete, runnable Swing example** that does exactly what you described.

### What this program does

* **Main window**

  * `JList` showing error records
  * **Add**, **Delete**, **Exit** buttons
* **Add window (dialog)**

  * Text field for **Error Message**
  * ComboBox for **Severity**
  * **Add**, **Clear** buttons
* Saves **all changes to a JSON file**
* Loads existing data from JSON on startup

---

## 1️⃣ Data Model (ErrorRecord.java)

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

## 2️⃣ JSON Utility (JsonUtil.java)

👉 Uses **Gson** (simple + clean)

**Dependency (Maven):**

```xml
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.10.1</version>
</dependency>
```

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

## 3️⃣ Add Error Dialog (AddErrorDialog.java)

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

        addBtn.addActionListener(e -> {
            if (!messageField.getText().trim().isEmpty()) {
                confirmed = true;
                setVisible(false);
            }
        });

        clearBtn.addActionListener(e -> {
            messageField.setText("");
            severityBox.setSelectedIndex(0);
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

## 4️⃣ Main Window (MainApp.java)

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
        records.forEach(listModel::addElement);

        errorList = new JList<>(listModel);
        add(new JScrollPane(errorList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton exitBtn = new JButton("Exit");

        addBtn.addActionListener(e -> addRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        exitBtn.addActionListener(e -> System.exit(0));

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
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
```

---

## ✅ Result

* Clean Swing GUI
* Popup window for adding errors
* Severity list
* Delete functionality
* **Persistent JSON storage**
* Auto-load on startup

If you want:

* ❌ validation messages
* ✏️ edit record feature
* 🎨 better layout (GridBagLayout)
* 📁 custom JSON location

Just say the word 😄
