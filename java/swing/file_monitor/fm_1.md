<a name="topage"></a>


# fm_1

#### Myos
```java
package myos;

import jaron.Jaron;

public class Myos {

	public Myos() {
//		MainApp ma = new MainApp();
//		MainScreen ms = new MainScreen();
		Jaron jr = new Jaron();
	}

	public static void main(String[] args) {
		Myos ms = new Myos();
	}
}

```

----

#### Jaron
```java
package jaron;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Jaron {
	
	public Jaron() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final WelcomeScreen welcome = new WelcomeScreen();
				welcome.setVisible(true);

				// Auto-close after 3 seconds and open main window
				Timer timer = new Timer(1000, new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						welcome.dispose(); // close welcome screen
                 // new MainScreen().setVisible(true); // open main window
						new MainScreen();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		});
	}

///---[main]---///
	public static void main(String[] args) {
		Jaron jaron = new Jaron();
	}
}
```

----

#### MainScreen
```java
package jaron;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class MainScreen {

	private JFrame frame = null;
	private JPanel panel = null;
	private JPanel jpfrm1 = null;

	private JMenuBar mbar = null;
	private JMenu fileMenu = null;
	private JMenuItem item = null;

	private ErrorTableModel tableModel;
	private JTable errorTable;
	private ArrayList<ErrorRecord> records;

	private JButton addBtn = null;
	private JButton deleteBtn = null;
	private JButton exitBtn = null;

	private String[] fileItems = new String[] { "Help", "About", "Exit" };

	private JButton Frm1SelectFile = null;
	
	
    private static final String JSON_PATH = "C://json//prod.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static DefaultTableModel model;

    
///----[MainScreen()]----///
	public MainScreen() {

		frame = new JFrame("RamiDocs Monitor...");
		// frame.setResizable(false);
		frame.setLocationRelativeTo(null); // --center--
		frame.setAlwaysOnTop(true);
		// frame.setIconImage(Toolkit.getDefaultToolkit().getImage(nof.class.getResource("/icons/star.jpg")));

		// [Frame.Menu-Bar]------------------------------------------
		// menu-bar + pop up result.
		mbar = new JMenuBar();
		fileMenu = new JMenu("Menu");

		// listers
		ActionListener printListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("Help")) {
					System.out.println("Help");
					String stTitle = "Help,";
					String stMessage = "H E L P \n\n Welcome to... \n\n have a nice day.";
					JOptionPane.showMessageDialog(frame, stMessage, stTitle, JOptionPane.INFORMATION_MESSAGE);
				}

				if (event.getActionCommand().equals("About")) {
					System.out.println("About");
				}

				// Exit(0)
				if (event.getActionCommand().equals("Exit")) {
					System.out.println("Exit");
					ExitProc();
				}
			}
		};

		// menu
		for (int i = 0; i < fileItems.length; i++) {
			item = new JMenuItem(fileItems[i]);
			item.addActionListener(printListener);
			fileMenu.add(item);
		}

		mbar.add(fileMenu);
///----[/MenuBar]----///

///----[jpfrm1]----///		
		jpfrm1 = new JPanel();
		frame.getRootPane().add(jpfrm1);
		jpfrm1.setLayout(null);

		
		File file = new File(JSON_PATH);

		if (file.exists()) {
		    System.out.println("File exists");
			//loadJson();
		    records = JsonUtil.load();
		} else {
		    System.out.println("File NOT found");
		}
		
		// Create your table model with 2 columns
		tableModel = new ErrorTableModel(records);

		// Create JTable
		errorTable = new JTable(tableModel);
		errorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		
//		int c1 = (int)(tableWidth();	
//		TableColumnModel columnModel =  errorTable.getColumnModel();


		// Put table inside scroll pane
		JScrollPane scrollPane = new JScrollPane(errorTable);
		scrollPane.setBounds(10, 10, 500, 200); // initial size
		jpfrm1.add(scrollPane);

		
///---[buttons]---///
		int w = frame.getContentPane().getWidth();
		int h = frame.getContentPane().getHeight();

		int btnWidth = 117;
		int btnHeight = 25;
		int padding = 20;

		// center the 3 buttons at bottom
		int totalWidth = (btnWidth * 3) + (20 * 2);
		int startX = (w - totalWidth) / 2;
		int y = h - btnHeight - padding;
       
		addBtn = new JButton("Add");
		addBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
		addBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	System.out.println("addBtn");
            	 addRecord();
            }
		});
		
		jpfrm1.add(addBtn);

		deleteBtn = new JButton("Delete");
		deleteBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
		deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	System.out.println("deleteBtn");
            	deleteRecord();
            }
		});
		jpfrm1.add(deleteBtn);

		exitBtn = new JButton("Exit");
		exitBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
		exitBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	System.out.println("exitBtn");
            	ExitProc();
            }
		});
		jpfrm1.add(exitBtn);

		// dynamic bottom buttons
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


				///---[auto resize]---///
				errorTable.setBounds(10, 10, w-20, h-120); //errorTable resize
				scrollPane.setBounds(10, 10, w-20, h-120); //scrollPane resize

				
				
				addBtn.setBounds(startX, y, btnWidth, btnHeight);
				deleteBtn.setBounds(startX + btnWidth + 20, y, btnWidth, btnHeight);
				exitBtn.setBounds(startX + (btnWidth + 20) * 2, y, btnWidth, btnHeight);
			}
		});

///---[frame - add panel]---///

		frame.setJMenuBar(mbar); // adding menu-bar --> frame
		frame.getContentPane().add(jpfrm1);

		frame.setSize(536, 326); // (536, 316) //800, 572 = full-screen
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); // frame.DO_NOTHING_ON_CLOSE); // EXIT_ON_CLOSE);

///---[/frame]---///
	}

    private static void loadJson() {
        try (FileReader reader = new FileReader(JSON_PATH)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray links = root.getAsJsonArray("links");
            
            int index = 0;
            System.out.println(links);
            for (JsonElement el : links) {
                JsonObject obj = el.getAsJsonObject();
                System.out.println(index);
                System.out.println(obj.get("name"));
                System.out.println(obj.get("path"));
                
                model.addRow(new Object[]{
                        obj.get("name").getAsString()
                       ,obj.get("path").getAsString()
                   ,"Open"
                });
                
                index++;
                System.out.println(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(">> IOException >> " + e.getMessage());
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

    private void addRecord() {
        AddErrorDialog dialog = new AddErrorDialog(frame);
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
	
	///---[ExitProc]---///
	/// * exit propmpy (yes=exit, no=Don't exit')
	private void ExitProc() {
		String stMessage = "E X I T";
		String stTitle = "Exit... Are You Sure ";
		int n = JOptionPane.showConfirmDialog(frame, stTitle, stMessage, JOptionPane.YES_NO_OPTION);
		switch (n) {
		case 0: // yes
			new GoodbyeScreen().showAndExit();
			frame.dispose();
//			System.exit(0); // Exit Program
			break;
		case 1: // no
			System.out.println("Exit - have been canceled");
			break;
		}
	}
	///---[/ExitProc]---///	
}
```

----

#### AddErrorDialog
```java
package jaron;

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

----

#### ErrorRecord
```java
package jaron;

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

----

#### ErrorTableModel
```java
package jaron;

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
//        return records.size();
 
	    if (records == null) {
	        return 0;
	    }
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

----

#### GoodbyeScreen
```java
package jaron;

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
        Timer timer = new Timer(1000, new java.awt.event.ActionListener() {
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

----

#### WelcomeScreen
```java
package jaron;

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
        panel.add(centerLabel("JMYOS", titleFont));

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
}

```

----

#### JsonUtil
```java
package jaron;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtil {
    private static final String FILE_NAME = "C:\\json\\errors.json"; /// "../../errors.json";
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

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
```

