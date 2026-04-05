Alright — you want this behavior:

### ✔ Right‑click on tray icon → **same popup menu as before**  
### ✔ Left‑click on tray icon → **open a NEW Swing window**  
with:
- a **menubar**
- a **toolbar**
- a **main content panel**
- all coming from a **separate class** (clean architecture)

Perfect. Below is a clean, modern, safe implementation.

---

# ✅ **Step 1 — Create a separate window class**

This class represents the window that opens when the user left‑clicks the tray icon.

```java
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("Main Window");

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // ----- MENUBAR -----
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open"));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem("Exit"));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("About"));

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // ----- TOOLBAR -----
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("Action 1"));
        toolBar.add(new JButton("Action 2"));
        toolBar.add(new JButton("Action 3"));

        // ----- MAIN CONTENT -----
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Main application content goes here"));

        // Layout
        add(toolBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
}
```

This window stays hidden until the user left‑clicks the tray icon.

---

# ✅ **Step 2 — Modify your TrayDemo to open MainWindow on left‑click**

This version keeps your right‑click popup menu exactly the same.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrayDemo2 {

    private static MainWindow mainWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TrayDemo2::createAndShow);
    }

    private static void createAndShow() {

        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "System tray not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();

        // Load your icon (Base64, file, or resource)
        Image image = Toolkit.getDefaultToolkit().getImage("icon16.png");

        PopupMenu popup = new PopupMenu();

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(e -> showMainWindow());
        popup.add(showItem);

        MenuItem hideItem = new MenuItem("Hide");
        hideItem.addActionListener(e -> {
            if (mainWindow != null) mainWindow.setVisible(false);
        });
        popup.add(hideItem);

        popup.addSeparator();

        MenuItem exitItem = new MenuItem("Exit");
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
        trayIcon.setImageAutoSize(true);

        // ⭐ LEFT CLICK → open MainWindow
        trayIcon.addActionListener(e -> showMainWindow());

        // ⭐ RIGHT CLICK → popup menu (handled automatically)

        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });

        try {
            tray.add(trayIcon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showMainWindow() {
        if (mainWindow == null) {
            mainWindow = new MainWindow();
        }
        mainWindow.setVisible(true);
        mainWindow.toFront();
    }
}
```

---

# 🎯 What this gives you

### ✔ Left‑click tray icon  
Opens your new Swing window with:
- menubar  
- toolbar  
- main content panel  
- all from a separate class (`MainWindow`)

### ✔ Right‑click tray icon  
Shows the same popup menu as before.

### ✔ Clean architecture  
Tray logic stays in `TrayDemo2`  
UI logic stays in `MainWindow`

---

# Want me to integrate your Base64 icon directly into this code?

Just say the word and I’ll embed it cleanly into the `Image` loader.