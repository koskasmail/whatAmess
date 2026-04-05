package wsMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow2 extends JFrame {

	private JTextArea logArea;

	public MainWindow2() {
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
