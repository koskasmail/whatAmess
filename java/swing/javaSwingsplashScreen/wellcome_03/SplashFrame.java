package wellcome_03;

import javax.swing.*;

public class SplashFrame extends JFrame {

	public SplashFrame() {
		setTitle("Splash Screen");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		add(new SplashPanel());

		setVisible(true);
	}
}

//import javax.swing.*;
//
//public class SplashFrame extends JFrame {
//
//	public SplashFrame() {
//		setTitle("Splash Screen");
//		setSize(800, 500);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
//
//		SplashPanel panel = new SplashPanel();
//		add(panel);
//
//		// Start background music
//		MusicPlayer.playLoop();
//
//		setVisible(true);
//	}
//}
