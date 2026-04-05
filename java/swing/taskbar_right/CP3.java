package wsMain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CP3 {

    private static MainWindow mainWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CP3::createAndShow);
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

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    showMainWindow();
                }
            }
        });

        
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


//public class CP {
//
//	public CP() {
//		// TODO Auto-generated constructor stub
//	}
//
//}
