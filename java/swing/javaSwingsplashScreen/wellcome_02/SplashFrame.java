package wellcome_02;

import javax.swing.*;

public class SplashFrame extends JFrame {

	public SplashFrame() {
		setTitle("Splash Screen");
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		add(new SplashPanel());

		setVisible(true);
	}
}
