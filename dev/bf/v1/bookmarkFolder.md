
# bookmarkFolder


#### Java

Below is a **simple Java desktop application** using **Swing** that behaves like a lightweight **bookmark manager** with:

* 📁 **Folders (left panel)**

  * Add
  * Delete
  * Rename
  * Move Up / Down

* 🔗 **Links (right panel)** inside selected folder

  * Add
  * Delete
  * Edit
  * Move Up / Down
  * Open in browser

* 💾 **File Menu**

  * Save → save to JSON file (user chooses path)
  * Import → load JSON file

* ⚙ **Configuration**

  * Stored in `config.json` in the **same folder as the program**
  * Stores the **path to the main JSON bookmarks file**
  * On startup the program loads that file automatically

---

# 📦 Project Structure

```
BookmarkManager/
 ├─ BookmarkManager.java
 ├─ ConfigManager.java
 ├─ DataModels.java
 ├─ JsonStorage.java
 └─ config.json   (auto-created)
```

Uses:

* **Swing** for UI
* **Jackson** for JSON

Dependency (if using Maven or manually download):

```
com.fasterxml.jackson.core:jackson-databind:2.17.0
```

---

# 1️⃣ Data Models

`DataModels.java`

```java
import java.util.*;

class LinkItem {
    public String name;
    public String path;
    public String comment;

    public LinkItem() {}

    public LinkItem(String name, String path, String comment) {
        this.name = name;
        this.path = path;
        this.comment = comment;
    }

    public String toString() {
        return name + " | " + path + " | " + comment;
    }
}

class FolderItem {
    public String name;
    public List<LinkItem> links = new ArrayList<>();

    public FolderItem() {}

    public FolderItem(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

class BookmarkData {
    public List<FolderItem> folders = new ArrayList<>();
}
```

---

# 2️⃣ JSON Storage

`JsonStorage.java`

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class JsonStorage {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void save(BookmarkData data, String path) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), data);
    }

    public static BookmarkData load(String path) throws Exception {
        return mapper.readValue(new File(path), BookmarkData.class);
    }
}
```

---

# 3️⃣ Configuration Manager

`ConfigManager.java`

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final String CONFIG_FILE = "config.json";
    private static ObjectMapper mapper = new ObjectMapper();

    public static String getBookmarkPath() {
        try {
            File f = new File(CONFIG_FILE);
            if (!f.exists()) return null;

            Map map = mapper.readValue(f, Map.class);
            return (String) map.get("bookmarkFile");
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveBookmarkPath(String path) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("bookmarkFile", path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CONFIG_FILE), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

# 4️⃣ Main Program

`BookmarkManager.java`

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;

public class BookmarkManager extends JFrame {

    DefaultListModel<FolderItem> folderModel = new DefaultListModel<>();
    JList<FolderItem> folderList = new JList<>(folderModel);

    DefaultListModel<LinkItem> linkModel = new DefaultListModel<>();
    JList<LinkItem> linkList = new JList<>(linkModel);

    BookmarkData data = new BookmarkData();
    String currentFile = null;

    public BookmarkManager() {

        setTitle("Bookmark Manager");
        setSize(900,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JSplitPane split = new JSplitPane();

        split.setLeftComponent(createFolderPanel());
        split.setRightComponent(createLinkPanel());

        setJMenuBar(createMenu());

        add(split);

        loadFromConfig();
    }

    private JPanel createFolderPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JScrollPane(folderList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton add = new JButton("Add");
        JButton del = new JButton("Delete");
        JButton rename = new JButton("Rename");
        JButton up = new JButton("Up");
        JButton down = new JButton("Down");

        buttons.add(add);
        buttons.add(del);
        buttons.add(rename);
        buttons.add(up);
        buttons.add(down);

        panel.add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> addFolder());
        del.addActionListener(e -> deleteFolder());
        rename.addActionListener(e -> renameFolder());
        up.addActionListener(e -> moveFolder(-1));
        down.addActionListener(e -> moveFolder(1));

        folderList.addListSelectionListener(e -> loadLinks());

        return panel;
    }

    private JPanel createLinkPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JScrollPane(linkList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton add = new JButton("Add");
        JButton del = new JButton("Delete");
        JButton edit = new JButton("Edit");
        JButton open = new JButton("Open");
        JButton up = new JButton("Up");
        JButton down = new JButton("Down");

        buttons.add(add);
        buttons.add(del);
        buttons.add(edit);
        buttons.add(open);
        buttons.add(up);
        buttons.add(down);

        panel.add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> addLink());
        del.addActionListener(e -> deleteLink());
        edit.addActionListener(e -> editLink());
        open.addActionListener(e -> openLink());
        up.addActionListener(e -> moveLink(-1));
        down.addActionListener(e -> moveLink(1));

        return panel;
    }

    private JMenuBar createMenu() {

        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem importFile = new JMenuItem("Import");

        save.addActionListener(e -> saveFile());
        importFile.addActionListener(e -> importFile());

        file.add(save);
        file.add(importFile);

        bar.add(file);

        return bar;
    }

    private void addFolder() {

        String name = JOptionPane.showInputDialog("Folder name");

        if (name == null || name.isEmpty()) return;

        FolderItem f = new FolderItem(name);
        data.folders.add(f);
        folderModel.addElement(f);
    }

    private void deleteFolder() {

        int i = folderList.getSelectedIndex();
        if (i < 0) return;

        data.folders.remove(i);
        folderModel.remove(i);
    }

    private void renameFolder() {

        int i = folderList.getSelectedIndex();
        if (i < 0) return;

        String name = JOptionPane.showInputDialog("New name");

        if (name == null) return;

        data.folders.get(i).name = name;
        folderList.repaint();
    }

    private void moveFolder(int dir) {

        int i = folderList.getSelectedIndex();
        int ni = i + dir;

        if (i < 0 || ni < 0 || ni >= folderModel.size()) return;

        FolderItem f = folderModel.remove(i);
        folderModel.add(ni, f);

        FolderItem df = data.folders.remove(i);
        data.folders.add(ni, df);

        folderList.setSelectedIndex(ni);
    }

    private void loadLinks() {

        linkModel.clear();

        int i = folderList.getSelectedIndex();
        if (i < 0) return;

        for (LinkItem l : data.folders.get(i).links)
            linkModel.addElement(l);
    }

    private void addLink() {

        int fi = folderList.getSelectedIndex();
        if (fi < 0) return;

        JTextField name = new JTextField();
        JTextField path = new JTextField();
        JTextField comment = new JTextField();

        Object[] msg = {
                "Name:", name,
                "Link/Path:", path,
                "Comment:", comment
        };

        int ok = JOptionPane.showConfirmDialog(this,msg,"Add Link",JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {

            LinkItem l = new LinkItem(name.getText(),path.getText(),comment.getText());

            data.folders.get(fi).links.add(l);
            linkModel.addElement(l);
        }
    }

    private void deleteLink() {

        int fi = folderList.getSelectedIndex();
        int li = linkList.getSelectedIndex();

        if (fi < 0 || li < 0) return;

        data.folders.get(fi).links.remove(li);
        linkModel.remove(li);
    }

    private void editLink() {

        int fi = folderList.getSelectedIndex();
        int li = linkList.getSelectedIndex();

        if (fi < 0 || li < 0) return;

        LinkItem l = data.folders.get(fi).links.get(li);

        JTextField name = new JTextField(l.name);
        JTextField path = new JTextField(l.path);
        JTextField comment = new JTextField(l.comment);

        Object[] msg = {
                "Name:", name,
                "Link/Path:", path,
                "Comment:", comment
        };

        int ok = JOptionPane.showConfirmDialog(this,msg,"Edit Link",JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {

            l.name = name.getText();
            l.path = path.getText();
            l.comment = comment.getText();

            linkList.repaint();
        }
    }

    private void moveLink(int dir) {

        int fi = folderList.getSelectedIndex();
        int li = linkList.getSelectedIndex();
        int ni = li + dir;

        if (fi < 0 || li < 0 || ni < 0 || ni >= linkModel.size()) return;

        LinkItem l = linkModel.remove(li);
        linkModel.add(ni,l);

        LinkItem dl = data.folders.get(fi).links.remove(li);
        data.folders.get(fi).links.add(ni,dl);

        linkList.setSelectedIndex(ni);
    }

    private void openLink() {

        int li = linkList.getSelectedIndex();
        int fi = folderList.getSelectedIndex();

        if (fi < 0 || li < 0) return;

        try {
            Desktop.getDesktop().browse(new URI(data.folders.get(fi).links.get(li).path));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void saveFile() {

        JFileChooser fc = new JFileChooser();

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = fc.getSelectedFile();

                JsonStorage.save(data,f.getAbsolutePath());

                ConfigManager.saveBookmarkPath(f.getAbsolutePath());

                currentFile = f.getAbsolutePath();

            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void importFile() {

        JFileChooser fc = new JFileChooser();

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = fc.getSelectedFile();

                data = JsonStorage.load(f.getAbsolutePath());

                currentFile = f.getAbsolutePath();

                ConfigManager.saveBookmarkPath(currentFile);

                refreshUI();

            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void refreshUI() {

        folderModel.clear();
        linkModel.clear();

        for (FolderItem f : data.folders)
            folderModel.addElement(f);
    }

    private void loadFromConfig() {

        try {

            String path = ConfigManager.getBookmarkPath();

            if (path != null) {
                data = JsonStorage.load(path);
                currentFile = path;
                refreshUI();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new BookmarkManager().setVisible(true);
        });
    }
}
```

---

# 🚀 Features Implemented

✔ Folder management
✔ Link management
✔ Open link in browser
✔ Move items up/down
✔ Save to JSON
✔ Import JSON
✔ Config file storing last bookmark path
✔ Auto-load on startup

---

# 💡 Possible Future Improvements

If you want, I can also add:

⭐ **Search bar for bookmarks**
⭐ **Drag & drop reordering**
⭐ **Clickable table layout instead of list**
⭐ **Icons for folders and links**
⭐ **Dark mode UI**
⭐ **Auto-save**
⭐ **Portable EXE build**

Just tell me and I can upgrade this into a **full bookmark manager app.**


------
