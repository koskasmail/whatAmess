You want **two things** added to your program:

### ✔ 1. **Left‑click = single click**, not double‑click  
### ✔ 2. **Every click anywhere in the program writes a message into `mainPanel`**

I’ll give you a clean, modern solution that works reliably on Windows.

Below is the updated structure:

---

# ✅ 1. Make left‑click open the window on **single click**

Java’s `TrayIcon` fires **ActionListener on single left‑click**, so you already get single‑click behavior.  
But Windows sometimes interprets double‑clicks unless you explicitly disable the default double‑click action.

The correct way is:

```java
trayIcon.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
            showMainWindow();
        }
    }
});
```

This guarantees **one left click = open window**.

---

# ✅ 2. Add a comment to `mainPanel` on every click in the program

We’ll add a `JTextArea` inside `mainPanel` and append messages to it.

---

# ⭐ Updated `MainWindow` class (with click logging)

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    private JTextArea logArea;

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
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("About"));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // ----- TOOLBAR -----
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("Action 1"));
        toolBar.add(new JButton("Action 2"));
        toolBar.add(new JButton("Action 3"));

        // ----- MAIN PANEL WITH LOG -----
        logArea = new JTextArea();
        logArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(logArea);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.CENTER);

        add(toolBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Log clicks anywhere in the window
        addClickLogging(this);
    }

    private void addClickLogging(Component comp) {
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logArea.append("Clicked: " + comp.getClass().getSimpleName() + "\n");
            }
        });

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                addClickLogging(child);
            }
        }
    }
}
```

This logs:

- clicks on buttons  
- clicks on menus  
- clicks on toolbar  
- clicks on empty space  
- clicks on the frame  

Everything goes into the `logArea`.

---

# ⭐ Updated tray code (single‑click left‑click)

```java
trayIcon.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
            showMainWindow();
        }
    }
});
```

Right‑click still shows the popup menu automatically.

---

# 🎯 Result

You now have:

### ✔ Single left‑click opens your new Swing window  
### ✔ Right‑click shows your popup menu  
### ✔ Every click inside the program logs a message  
### ✔ Clean separation between tray logic and UI logic  

If you want, I can also add:

- timestamps to each log entry  
- colored log messages  
- auto‑scrolling  
- a “Clear Log” button  
- logging tray clicks too  

Just tell me what you want next.