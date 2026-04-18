package wellcome_01;

import javax.swing.*;

public class WelcomeFrame extends JFrame {

	public WelcomeFrame() {
		setTitle("Welcome Screen");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		add(new WelcomePanel());

		setVisible(true);
	}
}
