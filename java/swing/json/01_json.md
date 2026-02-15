
# 01_json.md

#### version #1

* Menu.java
```
package menu4;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private static final String JSON_PATH = "C:/json/prod.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static DefaultTableModel model;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Prod JSON Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        String[] columns = {"Name", "Path", "Open"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col < 2; // Only Name and Path editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);

        loadJson();

        // Click "Open" column
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 2) { // Open column
                    String path = model.getValueAt(row, 1).toString();
                    openPath(path);
                }
            }
        });

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton exitBtn = new JButton("Exit");

        addBtn.addActionListener(e -> openAddWindow());
        delBtn.addActionListener(e -> deleteRow(table));
        exitBtn.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(exitBtn);



        editBtn.addActionListener(e -> editRow(table));

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(exitBtn);

        
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void loadJson() {
        try (FileReader reader = new FileReader(JSON_PATH)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray links = root.getAsJsonArray("links");

            for (JsonElement el : links) {
                JsonObject obj = el.getAsJsonObject();
                model.addRow(new Object[]{
                        obj.get("name").getAsString(),
                        obj.get("path").getAsString(),
                        "Open"
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveJson() {
        List<JsonObject> list = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", model.getValueAt(i, 0).toString());
            obj.addProperty("path", model.getValueAt(i, 1).toString());
            list.add(obj);
        }

        JsonObject root = new JsonObject();
        Type listType = new TypeToken<List<JsonObject>>() {}.getType();
        root.add("links", gson.toJsonTree(list, listType));

        try (FileWriter writer = new FileWriter(JSON_PATH)) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openAddWindow() {
        JFrame addFrame = new JFrame("Add New Entry");
        addFrame.setSize(350, 200);
        addFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel pathLabel = new JLabel("Path:");
        JTextField pathField = new JTextField();

        JButton saveBtn = new JButton("Save");

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String path = pathField.getText().trim();

            if (!name.isEmpty() && !path.isEmpty()) {
                model.addRow(new Object[]{name, path, "Open"});
                saveJson();
                addFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addFrame, "Both fields are required.");
            }
        });

        addFrame.add(nameLabel);
        addFrame.add(nameField);
        addFrame.add(pathLabel);
        addFrame.add(pathField);
        addFrame.add(new JLabel());
        addFrame.add(saveBtn);

        addFrame.setVisible(true);
    }

    private static void deleteRow(JTable table) {
        int row = table.getSelectedRow();
        if (row != -1) {
            model.removeRow(row);
            saveJson();
        }
    }

    private static void openPath(String path) {
        try {
            new ProcessBuilder("explorer.exe", path).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void editRow(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to edit.");
            return;
        }

        JFrame editFrame = new JFrame("Edit Entry");
        editFrame.setSize(350, 200);
        editFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(model.getValueAt(row, 0).toString());

        JLabel pathLabel = new JLabel("Path:");
        JTextField pathField = new JTextField(model.getValueAt(row, 1).toString());

        JButton saveBtn = new JButton("Save");

        saveBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPath = pathField.getText().trim();

            if (!newName.isEmpty() && !newPath.isEmpty()) {
                model.setValueAt(newName, row, 0);
                model.setValueAt(newPath, row, 1);
                saveJson();
                editFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(editFrame, "Both fields are required.");
            }
        });

        editFrame.add(nameLabel);
        editFrame.add(nameField);
        editFrame.add(pathLabel);
        editFrame.add(pathField);
        editFrame.add(new JLabel());
        editFrame.add(saveBtn);

        editFrame.setVisible(true);
    }

}
```

* menu.json
```
{
  "environments": ["prod", "test", "dev"]
}

```

* prod.json
```
{
  "links": [
    { "name": "Logs", "path": "C:/prod/logs" },
    { "name": "Config", "path": "C:/prod/config" }
  ]
}
```

-----


