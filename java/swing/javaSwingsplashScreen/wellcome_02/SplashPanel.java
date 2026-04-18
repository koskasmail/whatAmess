package wellcome_02;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SplashPanel extends JPanel {

	private static final int STAR_COUNT = 50;
	private Star[] stars = new Star[STAR_COUNT];
	private Random rand = new Random();

	public SplashPanel() {
		setBackground(Color.BLACK);

		// Create stars at the top area
		for (int i = 0; i < STAR_COUNT; i++) {
			int x = rand.nextInt(700);
			int y = rand.nextInt(150); // top area
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

		drawStars(g);
		drawText(g);
		drawLand(g);
		drawTrees(g);
	}

	private void drawStars(Graphics g) {
		for (Star s : stars) {
			s.draw(g);
		}
	}

	private void drawText(Graphics g) {
		g.setColor(Color.WHITE);

		// Big Welcome
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Welcome", 220, 230);

		// Smaller subtitle
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		g.drawString("Have a nice day", 260, 270);
	}

	private void drawLand(Graphics g) {
		g.setColor(new Color(34, 139, 34)); // green
		g.fillRect(0, 380, getWidth(), 120);
	}

	private void drawTrees(Graphics g) {
		g.setColor(new Color(139, 69, 19)); // brown trunk

		for (int i = 0; i < 5; i++) {
			int x = 80 + i * 120;

			// Trunk
			g.fillRect(x, 330, 20, 50);

			// Leaves
			g.setColor(new Color(0, 120, 0));
			g.fillOval(x - 20, 290, 60, 60);

			g.setColor(new Color(139, 69, 19));
		}
	}
}
