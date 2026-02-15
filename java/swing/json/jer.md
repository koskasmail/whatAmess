# jer.md

* Menu
```java
package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu {

	private JFrame frame = null;
	private JPanel panel = null;
	private JPanel jpfrm1 = null;

	private JMenuBar mbar = null;
	private JMenu fileMenu = null;
	private JMenuItem item = null;
	
	private JButton addBtn = null; 
	private JButton deleteBtn = null;
	private JButton exitBtn = null; 
	
    
	private   String[ ]    fileItems = new String[ ] { "Help", "About",  "Exit" };
	

	private JButton Frm1SelectFile = null;

	public Menu() {

		frame = new JFrame("NOF...");
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
					JOptionPane.showMessageDialog(frame, stMessage, stTitle , JOptionPane.INFORMATION_MESSAGE);
					 
//					 int n = JOptionPane.showConfirmDialog(frame, stTitle, stMessage, JOptionPane.YES_NO_OPTION);
//					helps h = new helps();
				}

				if (event.getActionCommand().equals("About")) {
					System.out.println("About");
//					l = new log("event","menu-bar","About - have been pressed");
					// System.out.println("___About___");
					// JOptionPane.showMessageDialog(frame ," Name: NOF \n Version: 1.0 \n \n
					// Developed by: Yaron Kessler \n \n Powered by: Java \n License: Free for
					// use","About", JOptionPane.INFORMATION_MESSAGE);
//					about nf = new about();
				}

				// Exit(0)
				if (event.getActionCommand().equals("Exit")) {
					System.out.println("Exit");
					ExitProc();
				}
			}
		};

		// Create the "File" menu items and add to menu
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

		// SelectFile - Gui Select //
		Frm1SelectFile = new JButton("select a file");
		Frm1SelectFile.setBounds(12, 20, 117, 25);
		Frm1SelectFile.setToolTipText("please select a source file, for the processing data job - automatically.");
		jpfrm1.add(Frm1SelectFile);	
		

        int w = frame.getContentPane().getWidth();
        int h = frame.getContentPane().getHeight();

        int btnWidth = 117;
        int btnHeight = 25;
        int padding = 20;

        // center the 3 buttons at bottom
        int totalWidth = (btnWidth * 3) + (20 * 2);
        int startX = (w - totalWidth) / 2;
        int y = h - btnHeight - padding;

		
///---[buttons]---///	       
	    addBtn = new JButton("Add");
	    addBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
	    jpfrm1.add(addBtn);
	    
	    deleteBtn = new JButton("Delete");
	    deleteBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
	    jpfrm1.add(deleteBtn);
	    
	    exitBtn = new JButton("Exit");
	    exitBtn.setToolTipText("please select a source file, for the processing data job - automatically.");
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

	            addBtn.setBounds(startX, y, btnWidth, btnHeight);
	            deleteBtn.setBounds(startX + btnWidth + 20, y, btnWidth, btnHeight);
	            exitBtn.setBounds(startX + (btnWidth + 20) * 2, y, btnWidth, btnHeight);
	        }
	    });
///---[/buttons]---///
	    package os;

import javax.swing.SwingUtilities;

public class OsMain {

	public OsMain() {
		///---[#set 1]---///
//		 public static void main(String[] args) {
//		        SwingUtilities.invokeLater(Menu_1::createUI);
//		    }
		
		///---[#set 2]---///
		new Menu();
//		new Menu_1();
	}

	public static void main(String[] args) {
		OsMain osm = new OsMain();

	}

}



///---[frame - add panel]---///
		frame.setJMenuBar(mbar); // adding menu-bar --> frame
		frame.getContentPane().add(jpfrm1);


		frame.setSize(536, 326); // (536, 316) //800, 572 = full-screen
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); // frame.DO_NOTHING_ON_CLOSE); // EXIT_ON_CLOSE);
		//
		// Order By Tab/s.Tab
		// frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new
		// Component[]{Frm1SelectFile, frm1txtSelectFile, frm1txtStringSeek1, rbfull,
		// rbFromTo, rbFromToEndOfSeekString, rbfromToEnd, frm1txtfromchar,
		// frm1txttochar, Frm1ButFind, Frm1ButSNR, Frm1Butclean, Frm1BtFileViewer,
		// Frm1BtResult, Frm1BtOsProperty, Frm1BtnExit}));
///---[/frame]---///
	}

	///----[ExitProc]----///
	/// * exit propmpy (yes=exit, no=Don't exit')
	private void ExitProc() {
		String stMessage = "E X I T";
		String stTitle = "Exit... Are You Sure ";
		int n = JOptionPane.showConfirmDialog(frame, stTitle, stMessage, JOptionPane.YES_NO_OPTION);
		switch (n) {
		case 0: // yes
			frame.dispose();
			System.exit(0); // Exit Program
			break;
		case 1: // no
			System.out.println("Exit - have been canceled");
			break;
		}
	} 
	///----[/ExitProc]----///

}
```

* OsMain.java
```java
package os;

import javax.swing.SwingUtilities;

import backup.Menu_1;
import menu.Menu;

public class OsMain {

	public OsMain() {
		///---[#set 1]---///
//		 public static void main(String[] args) {
//		        SwingUtilities.invokeLater(Menu_1::createUI);
//		    }
		
		///---[#set 2]---///
		new Menu();
//		new Menu_1();
	}

	public static void main(String[] args) {
		OsMain osm = new OsMain();

	}

}
```
