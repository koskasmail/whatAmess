
# 01_file_json.md

* AddErrorDialog.java
```
package file4;

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

* ErrorRecord.java
```
package file4;

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

* ErrorTableModel.java
```
package file4;

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

* GoodbyeScreen.java
```
package file4;

import javax.swing.*;
import java.awt.*;

public class GoodbyeScreen extends JFrame {

    public GoodbyeScreen() {
        setTitle("Goodbye");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        Font bigFont = new Font("Arial", Font.BOLD, 32);
        Font normalFont = new Font("Arial", Font.PLAIN, 16);

        panel.add(Box.createVerticalStrut(40));

        panel.add(centerLabel("Exit ...", bigFont));

        panel.add(Box.createVerticalStrut(30));

        panel.add(centerLabel("have a nice day", normalFont));

        add(panel);
    }

    private JLabel centerLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static void showAndExit() {
        final GoodbyeScreen goodbye = new GoodbyeScreen();
        goodbye.setVisible(true);

        // Close application after 3 seconds
        Timer timer = new Timer(3000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(0);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
```

* JsonUtil.java
```
package file4;

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

* MainApp.java
```
package file4;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

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
                ///System.exit(0);
                dispose();                // close main window
                GoodbyeScreen.showAndExit();
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

* WelcomeScreen.java
```
package file4;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Welcome");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("WELCOME", titleFont));
        panel.add(centerLabel("TO", titleFont));
        panel.add(centerLabel("MYOS", titleFont));

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("written by : Radmidocs Team", textFont));

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("Have a nice day.", textFont));

        add(panel);
    }

    private JLabel centerLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final WelcomeScreen welcome = new WelcomeScreen();
                welcome.setVisible(true);

                // Auto-close after 3 seconds and open main window
                Timer timer = new Timer(5000, new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        welcome.dispose();       // close welcome screen
                        new MainApp().setVisible(true); // open main window
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }
}
```

