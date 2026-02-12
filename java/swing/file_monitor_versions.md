
# file_monitor_versions.md

#### #1

package folder.menu;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Menu_2 {
	
	
	private   JFrame       frame                =   null;
	private   JPanel       panel                =   null;

	private   JMenuBar     mbar                 =   null;
	private   JMenu        fileMenu             =   null;
	private   JMenuItem    item                 =   null;
	private   String[ ]    fileItems = new String[ ] { "Help", "About",  "Exit" };
	

	public Menu_2() {
		frame = new JFrame("NOF...");
		//frame.setResizable(false);
		//frame.setLocationRelativeTo(null);  //--center--
		frame.setAlwaysOnTop(true);
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(nof.class.getResource("/icons/star.jpg")));
		
		//[Frame.Menu-Bar]------------------------------------------
		// menu-bar + pop up result.
		mbar = new JMenuBar();
		fileMenu = new JMenu("Menu");

		mbar.add(fileMenu);

		
		//listers
		ActionListener printListener = new ActionListener(  )
		{
			public void actionPerformed(ActionEvent event)
			{
				if (event.getActionCommand().equals("Help"))
				{
					System.out.println("Help");
//					l = new log("event","menu-bar","Help - have been pressed");
					//System.out.println("___HELP___");
					//JOptionPane.showMessageDialog(frame ,"~~ HELP ~~ \n 1. Select a file. \n 2. select a String to find in file. \n 3. select a type of search. \n 4. press submit button -  to start string seeking process in file. ","Help", JOptionPane.INFORMATION_MESSAGE);
//					helps h = new helps();
				}

				if (event.getActionCommand().equals("About"))
				{
					System.out.println("About");
//					l = new log("event","menu-bar","About - have been pressed");
					//System.out.println("___About___");
					//JOptionPane.showMessageDialog(frame ," Name: NOF \n Version: 1.0 \n \n Developed by: Yaron Kessler \n \n Powered by: Java \n License: Free for use","About", JOptionPane.INFORMATION_MESSAGE);
//					about nf = new about();
				}

				//Exit(0)
				if (event.getActionCommand().equals("Exit"))
				{
					System.out.println("Exit");
					//ExitProc();
				}
			}
		};	

		
		
		//Create the "File" menu items and add to menu
		for (int i=0; i < fileItems.length; i++)
		{
			// item = new JMenuItem(fileItems[i]);
			item.addActionListener(printListener);
			fileMenu.add(item);
		}
		
		//
		//[frame--adding--(mbar,jpfrm1) + size + visible + order-by--]------------------------------------------
		//	
		frame.setJMenuBar(mbar); //adding menu-bar --> frame
//		frame.getContentPane().add(jpfrm1);

		frame.setSize(536, 326); //(536, 316) //800, 572 = full-screen
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); //frame.DO_NOTHING_ON_CLOSE); // EXIT_ON_CLOSE);
		//
		//Order By Tab/s.Tab
		//frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{Frm1SelectFile, frm1txtSelectFile, frm1txtStringSeek1, rbfull, rbFromTo, rbFromToEndOfSeekString, rbfromToEnd, frm1txtfromchar, frm1txttochar, Frm1ButFind, Frm1ButSNR, Frm1Butclean, Frm1BtFileViewer, Frm1BtResult, Frm1BtOsProperty,   Frm1BtnExit}));

	}

}


-----

#### #2

-----

#### #3

-----

#### #4

-----

#### #5

-----

#### #6

-----
