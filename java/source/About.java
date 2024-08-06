package gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class About
{
	private   JFrame    frmAbout      =   null;
	private   JPanel    panel         =   null;
	private   JLabel    lblgif1       =   null;
    private   JLabel    lblTSof       =   null;
    private   JLabel    lblVSof       =   null;
    private   JLabel    lblTVer       =   null;
    private   JLabel    lblVVer       =   null;
    private   JLabel    lblTDev       =   null;
    private   JLabel    lblVDev       =   null;    
    private   JLabel    lblTqa        =   null;
    private   JLabel    lblVqa        =   null;
    private   JLabel    lblTpb        =   null;
    private   JLabel    lblVpb        =   null;


 

	public About()
	{
        //create form
		frmAbout = new JFrame("NOF...About");
		frmAbout.setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/icons/star.jpg")));
		frmAbout.setResizable(false);

        //create panel
		panel = new JPanel();
		panel.setForeground(Color.BLACK);

		//create bound/s
		panel.setLayout(null);
		frmAbout.getContentPane().add(panel);


		lblgif1 = new JLabel("");
		lblgif1.setBackground(Color.PINK);
		lblgif1.setIcon(new ImageIcon(About.class.getResource("/icons/yamon.jpg")));
		lblgif1.setBounds(10, 10, 201, 163);
		panel.add(lblgif1);



		lblTSof = new JLabel("Name : ");
		lblTSof.setBounds(230, 25, 120, 15);
		panel.add(lblTSof);

		lblVSof = new JLabel("NOF");
		lblVSof.setForeground(Color.GRAY);
		lblVSof.setBounds(345, 25, 60, 15);
		panel.add(lblVSof);

		

		lblTVer = new JLabel("Version : ");
		lblTVer.setBounds(230, 55, 120, 15);
		panel.add(lblTVer);

		lblVVer = new JLabel("1.1");
		lblVVer.setForeground(Color.GRAY);
		lblVVer.setBounds(345, 55, 60, 15);
		panel.add(lblVVer);

		
		
		lblTpb = new JLabel("License : "); //75
		lblTpb.setBounds(230, 85, 120, 15);
		panel.add(lblTpb);

		lblVpb = new JLabel("Free for use");
		lblVpb.setForeground(Color.GRAY);
		lblVpb.setBounds(345, 85, 110, 15);
		panel.add(lblVpb);

		
		
		lblTpb = new JLabel("Powered by : ");
		lblTpb.setBounds(230, 115, 120, 15); //105
		panel.add(lblTpb);

		lblVpb = new JLabel("Java 1.6 / 1.7");
		lblVpb.setForeground(Color.GRAY);
		lblVpb.setBounds(345, 115, 110, 15);
		panel.add(lblVpb);

	
		
		lblTDev = new JLabel("Developed by : ");
		lblTDev.setBounds(10, 185, 140, 15);
		panel.add(lblTDev);

		lblVDev = new JLabel("Yaron Kessler .");
		lblVDev.setForeground(Color.GRAY);
		lblVDev.setBounds(180, 185, 280, 15);
		panel.add(lblVDev);

		
		
		lblTDev = new JLabel("Quality Assurance by : ");
		lblTDev.setBounds(10, 210, 140, 15);
		panel.add(lblTDev);

		lblVDev = new JLabel("Yaron Kessler .");
		lblVDev.setForeground(Color.GRAY);
		lblVDev.setBounds(180, 210, 280, 15);
		panel.add(lblVDev);
		
				

		frmAbout.setSize(480, 280);
		frmAbout.setVisible(true);

		frmAbout.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		
		frmAbout.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent w)
			{
				frmAbout.dispose();
				System.out.println("Exit Current Window.");
				//System.exit(0); // Exit Program
			}
		});

		
	}

/*
	public static void main (String args [])
	{
		about jfa = new about();
	}
*/

}
//eof about.java
