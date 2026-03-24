Great — now we’re building this step‑by‑step, clean and structured.  
Let’s focus ONLY on **Step 1**, exactly as you requested:

### **✔ When the program loads:**
1. Check if `setup.json` exists  
2. If it **does not exist** → open a **GUI window**  
   - Show labels + text fields for each key  
   - Include a **Browse…** button for path fields  
   - Save the result as `setup.json`  
3. If it **exists** → load it using **Google Gson**  
4. The JSON contains these fields:  
   - `name`  
   - `purpose`  
   - `version`  
   - `target path`  
   - `filter path`  
   - `output path`  
   - `backup path`

Below is the **Java 1.8 code** for Step 1 only.  
No monitoring, no searching — just the setup loader GUI + JSON handling.

---

# ✅ **Step 1 — Java 1.8 Setup Loader (with GUI + Gson)**

### **📌 Requirements:**
- Java 1.8  
- Google Gson (gson‑2.x.jar) on classpath  
- Pure Swing GUI  

---

# 📄 **SetupData.java**
A simple POJO for Gson.

```java
public class SetupData {
    public String name;
    public String purpose;
    public String version;
    public String targetPath;
    public String filterPath;
    public String outputPath;
    public String backupPath;
}
```

---

# 📄 **SetupLoader.java**
Loads or creates the JSON file.

```java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SetupLoader {

    private static final String SETUP_FILE = "setup.json";

    public static SetupData loadSetup() {
        File file = new File(SETUP_FILE);

        if (!file.exists()) {
            return showSetupGuiAndSave();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            SetupData data = gson.fromJson(reader, SetupData.class);
            reader.close();
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return showSetupGuiAndSave();
        }
    }

    private static SetupData showSetupGuiAndSave() {
        SetupData data = new SetupData();

        JTextField nameField = new JTextField(20);
        JTextField purposeField = new JTextField(20);
        JTextField versionField = new JTextField(20);

        JTextField targetField = new JTextField(20);
        JButton targetBrowse = new JButton("Browse");

        JTextField filterField = new JTextField(20);
        JButton filterBrowse = new JButton("Browse");

        JTextField outputField = new JTextField(20);
        JButton outputBrowse = new JButton("Browse");

        JTextField backupField = new JTextField(20);
        JButton backupBrowse = new JButton("Browse");

        // File chooser logic
        targetBrowse.addActionListener(e -> choosePath(targetField));
        filterBrowse.addActionListener(e -> choosePath(filterField));
        outputBrowse.addActionListener(e -> choosePath(outputField));
        backupBrowse.addActionListener(e -> choosePath(backupField));

        JPanel panel = new JPanel(new GridLayout(0, 3, 5, 5));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Purpose:"));
        panel.add(purposeField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Version:"));
        panel.add(versionField);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Target Path:"));
        panel.add(targetField);
        panel.add(targetBrowse);

        panel.add(new JLabel("Filter Path:"));
        panel.add(filterField);
        panel.add(filterBrowse);

        panel.add(new JLabel("Output Path:"));
        panel.add(outputField);
        panel.add(outputBrowse);

        panel.add(new JLabel("Backup Path:"));
        panel.add(backupField);
        panel.add(backupBrowse);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Setup Configuration",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            data.name = nameField.getText();
            data.purpose = purposeField.getText();
            data.version = versionField.getText();
            data.targetPath = targetField.getText();
            data.filterPath = filterField.getText();
            data.outputPath = outputField.getText();
            data.backupPath = backupField.getText();

            saveSetup(data);
        }

        return data;
    }

    private static void choosePath(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private static void saveSetup(SetupData data) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(SETUP_FILE);
            gson.toJson(data, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

# 📄 **Main.java**
Loads the setup on startup.

```java
public class Main {
    public static void main(String[] args) {
        SetupData setup = SetupLoader.loadSetup();

        System.out.println("Loaded setup:");
        System.out.println("Name: " + setup.name);
        System.out.println("Purpose: " + setup.purpose);
        System.out.println("Version: " + setup.version);
        System.out.println("Target Path: " + setup.targetPath);
        System.out.println("Filter Path: " + setup.filterPath);
        System.out.println("Output Path: " + setup.outputPath);
        System.out.println("Backup Path: " + setup.backupPath);
    }
}
```

---

# 🎉 **Step 1 is complete**
You now have:

### ✔ GUI setup window  
### ✔ Browse buttons for path fields  
### ✔ JSON saved as `setup.json`  
### ✔ JSON loaded using Gson  
### ✔ Clean Java 1.8‑compatible code  

---

# 👉 Ready for Step 2?
Next steps could be:

- Folder monitoring  
- Finding newest file  
- Fast keyword search  
- Markdown output  

Tell me when you want to continue.
