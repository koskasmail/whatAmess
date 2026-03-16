Here’s a compact but complete Swing example that does what you described.

```java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FolderBookmarkApp extends JFrame {

    private static final String PRIMARY_FILE = "folder.json";   // searched first
    private static final String NEW_FILE = "myfolder.json";     // created if none / broken

    private File dataFile;
    private DefaultTableModel tableModel;
    private JTable table;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showSplash("Welcome to Folder Bookmark", 1500);
            FolderBookmarkApp app = new FolderBookmarkApp();
            app.setVisible(true);
        });
    }

    public FolderBookmarkApp() {
        super("folder book mark");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        initDataFile();
        initUI();
        loadData();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });
    }

    private void initDataFile() {
        File primary = new File(PRIMARY_FILE);
        if (primary.exists()) {
            // Try to load; if broken, rename and create NEW_FILE
            if (isJsonLoadable(primary)) {
                dataFile = primary;
            } else {
                String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File backup = new File("folder_backup_" + ts + ".json");
                primary.renameTo(backup);
                dataFile = new File(NEW_FILE);
            }
        } else {
            dataFile = new File(NEW_FILE);
        }
    }

    private boolean isJsonLoadable(File f) {
        try {
            String content = Files.readString(f.toPath(), StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) return true; // treat empty as ok
            // Very simple validation: must start with [ and end with ]
            return content.startsWith("[") && content.endsWith("]");
        } catch (IOException e) {
            return false;
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Name", "Description", "Path", "Open"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only "Open" column is not editable directly
                return column != 3;
            }
        };
        table = new JTable(tableModel);
        table.getColumn("Open").setCellRenderer(new ButtonRenderer());
        table.getColumn("Open").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton exitBtn = new JButton("Exit");

        addBtn.addActionListener(e -> addRecord());
        editBtn.addActionListener(e -> editRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        saveBtn.addActionListener(e -> saveData());
        exitBtn.addActionListener(e -> exitApp());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRecord() {
        FolderRecord rec = showRecordDialog(null);
        if (rec != null) {
            tableModel.addRow(new Object[]{rec.name, rec.description, rec.path, "Open"});
        }
    }

    private void editRecord() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.");
            return;
        }
        FolderRecord current = new FolderRecord(
                (String) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2)
        );
        FolderRecord updated = showRecordDialog(current);
        if (updated != null) {
            tableModel.setValueAt(updated.name, row, 0);
            tableModel.setValueAt(updated.description, row, 1);
            tableModel.setValueAt(updated.path, row, 2);
        }
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }

    private FolderRecord showRecordDialog(FolderRecord existing) {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField pathField = new JTextField();

        if (existing != null) {
            nameField.setText(existing.name);
            descField.setText(existing.description);
            pathField.setText(existing.path);
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Path:"));
        panel.add(pathField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                existing == null ? "Add Folder" : "Edit Folder",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            String path = pathField.getText().trim();
            if (name.isEmpty() || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Path are required.");
                return null;
            }
            return new FolderRecord(name, desc, path);
        }
        return null;
    }

    void openFolderAtRow(int row) {
        String path = (String) tableModel.getValueAt(row, 2);
        File folder = new File(path);
        if (!folder.exists()) {
            JOptionPane.showMessageDialog(this, "Folder does not exist:\n" + path);
            return;
        }
        try {
            Desktop.getDesktop().open(folder);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot open folder:\n" + ex.getMessage());
        }
    }

    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }
        try {
            String content = Files.readString(dataFile.toPath(), StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) return;

            List<FolderRecord> records = parseJson(content);
            for (FolderRecord r : records) {
                tableModel.addRow(new Object[]{r.name, r.description, r.path, "Open"});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void saveData() {
        List<FolderRecord> records = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            records.add(new FolderRecord(
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1),
                    (String) tableModel.getValueAt(i, 2)
            ));
        }
        String json = toJson(records);
        try (Writer w = new OutputStreamWriter(new FileOutputStream(dataFile), StandardCharsets.UTF_8)) {
            w.write(json);
            JOptionPane.showMessageDialog(this, "Saved to " + dataFile.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    private void exitApp() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Save before exit?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (confirm == JOptionPane.CANCEL_OPTION) return;
        if (confirm == JOptionPane.YES_OPTION) {
            saveData();
        }
        dispose();
        showSplash("Goodbye from Folder Bookmark", 1200);
        System.exit(0);
    }

    // Very simple JSON (array of objects with name, description, path)

    private String toJson(List<FolderRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < records.size(); i++) {
            FolderRecord r = records.get(i);
            sb.append("{");
            sb.append("\"name\":\"").append(escape(r.name)).append("\",");
            sb.append("\"description\":\"").append(escape(r.description)).append("\",");
            sb.append("\"path\":\"").append(escape(r.path)).append("\"");
            sb.append("}");
            if (i < records.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private List<FolderRecord> parseJson(String json) {
        // Minimal, not general-purpose JSON parser; assumes our own format
        List<FolderRecord> list = new ArrayList<>();
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) return list;
        json = json.substring(1, json.length() - 1).trim();
        if (json.isEmpty()) return list;

        // Split objects by "},{" (simple but works for our controlled format)
        String[] objs = json.split("\\},\\s*\\{");
        for (String obj : objs) {
            String o = obj.trim();
            if (!o.startsWith("{")) o = "{" + o;
            if (!o.endsWith("}")) o = o + "}";
            String name = extractField(o, "name");
            String desc = extractField(o, "description");
            String path = extractField(o, "path");
            if (name != null && path != null) {
                list.add(new FolderRecord(unescape(name), unescape(desc == null ? "" : desc), unescape(path)));
            }
        }
        return list;
    }

    private String extractField(String obj, String field) {
        String key = "\"" + field + "\":";
        int idx = obj.indexOf(key);
        if (idx < 0) return null;
        int start = obj.indexOf('"', idx + key.length());
        if (start < 0) return null;
        int end = obj.indexOf('"', start + 1);
        if (end < 0) return null;
        return obj.substring(start + 1, end);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static void showSplash(String text, int millis) {
        JWindow splash = new JWindow();
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel(text));
        splash.getContentPane().add(panel);
        splash.pack();
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        new Timer(millis, e -> {
            splash.setVisible(false);
            splash.dispose();
        }) {{
            setRepeats(false);
        }}.start();
    }

    // Data holder
    private static class FolderRecord {
        String name;
        String description;
        String path;

        FolderRecord(String name, String description, String path) {
            this.name = name;
            this.description = description;
            this.path = path;
        }
    }

    // Button renderer/editor for "Open" column

    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Open");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Open");
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final FolderBookmarkApp app;
        private int row;

        public ButtonEditor(JCheckBox checkBox, FolderBookmarkApp app) {
            super(checkBox);
            this.app = app;
            this.button = new JButton("Open");
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            app.openFolderAtRow(row);
            return "Open";
        }
    }
}
```

You can drop this into a single `FolderBookmarkApp.java` file, compile, and run. If you want, I can next simplify the JSON handling by switching to a library like Gson or Jackson.
