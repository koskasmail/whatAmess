package wellcome_01;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WelcomePanel extends JPanel {

	private static final int STAR_COUNT = 40;
	private Star[] stars = new Star[STAR_COUNT];
	private Random rand = new Random();

	public WelcomePanel() {
		setBackground(Color.BLACK);

		// Create stars near bottom
		for (int i = 0; i < STAR_COUNT; i++) {
			int x = rand.nextInt(600);
			int y = 250 + rand.nextInt(120);
			stars[i] = new Star(x, y);
		}

		// Animation timer
		Timer timer = new Timer(120, e -> {
			for (Star s : stars) {
				s.update();
			}
			repaint();
		});
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw Welcome text
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("Welcome", 200, 100);

		// Draw stars
		for (Star s : stars) {
			s.draw(g);
		}
	}
}
