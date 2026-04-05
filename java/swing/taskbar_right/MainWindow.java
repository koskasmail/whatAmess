package wsMain;


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


//public class MainWindow {
//
//	public MainWindow() {
//		// TODO Auto-generated constructor stub
//	}
//
//}
