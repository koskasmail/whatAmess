package wsMain;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class TrayDemo {

//    private static TrayIcon[] trayIconHolder;
    final static TrayIcon[] trayIconHolder = new TrayIcon[1];
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(TrayDemo::createAndShow);
    }

    private static void createAndShow() {
        // Create the main window (but we’ll control visibility manually)
        JFrame frame = new JFrame("Tray Demo");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // don’t exit when closed
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.add(new JLabel("Hello from tray app", SwingConstants.CENTER));

        // Only show initially if you want:
        // frame.setVisible(true);

        // Check tray support
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(frame, "System tray not supported");
            frame.setVisible(true);
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();

        // Use any 16x16 or 24x24 icon you have
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        // Popup menu for tray icon
        PopupMenu popup = new PopupMenu();

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(e -> {
            frame.setVisible(true);
            frame.setState(Frame.NORMAL);
            frame.toFront();
        });
        popup.add(showItem);

        MenuItem hideItem = new MenuItem("Hide");
        hideItem.addActionListener(e -> frame.setVisible(false));
        popup.add(hideItem);

        popup.addSeparator();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            SystemTray.getSystemTray().remove(trayIconHolder[0]);
//            tray.remove(trayIconHolder[0]);
            System.exit(0);
        });
        popup.add(exitItem);

        // We need a holder because trayIcon is referenced in the exit listener
        final TrayIcon[] trayIconHolder = new TrayIcon[1];

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
        trayIcon.setImageAutoSize(true);

        // Left-click on tray icon toggles window
        trayIcon.addActionListener(new ActionListener() {
            private boolean visible = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                visible = !visible;
                frame.setVisible(visible);
                if (visible) {
                    frame.setState(Frame.NORMAL);
                    frame.toFront();
                }
            }
        });

        trayIconHolder[0] = trayIcon;

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not add tray icon");
            frame.setVisible(true);
        }
    }
}


