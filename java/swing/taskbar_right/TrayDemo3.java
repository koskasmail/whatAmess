package wsMain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrayDemo3 {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TrayDemo3::createAndShow);
	}

	private static void createAndShow() {

		String base64Icon = "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"; // your
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																							// Base64
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "System tray not supported");
            return;
        }																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																							// PNG

		JFrame frame = new JFrame("Tray Demo");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.add(new JLabel("Hello from tray app", SwingConstants.CENTER));

		if (!SystemTray.isSupported()) {
			JOptionPane.showMessageDialog(frame, "System tray not supported");
			frame.setVisible(true);
			return;
		}

		SystemTray tray = SystemTray.getSystemTray();
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

		Image image = Base64TrayIcon.decodeBase64Image(base64Icon);

		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
		trayIcon.setImageAutoSize(true);

		// ⭐ Exit safely — no NullPointerException
		exitItem.addActionListener(e -> {
			tray.remove(trayIcon);
			System.exit(0);
		});
		popup.add(exitItem);

		// Left-click toggles window
		trayIcon.addActionListener(e -> {
			boolean visible = !frame.isVisible();
			frame.setVisible(visible);
			if (visible) {
				frame.setState(Frame.NORMAL);
				frame.toFront();
			}
		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Could not add tray icon");
			frame.setVisible(true);
		}
	}
}
