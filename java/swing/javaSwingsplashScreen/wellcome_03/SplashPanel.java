package wellcome_03;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SplashPanel extends JPanel {

	private static final int STAR_COUNT = 150; // more stars
	private Star[] stars = new Star[STAR_COUNT];
	private Random rand = new Random();

	public SplashPanel() {
		setBackground(Color.BLACK);

		// Create stars across the whole sky
		for (int i = 0; i < STAR_COUNT; i++) {
			int x = rand.nextInt(800);
			int y = rand.nextInt(200); // top half only
			stars[i] = new Star(x, y);
		}

		// Animation timer
		Timer timer = new Timer(100, e -> {
			for (Star s : stars)
				s.update();
			repaint();
		});
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawStars(g);
		drawMoon(g);
		drawText(g);
	}

	private void drawStars(Graphics g) {
		for (Star s : stars)
			s.draw(g);
	}

	private void drawMoon(Graphics g) {
		g.setColor(new Color(255, 255, 220));
		g.fillOval(600, 40, 90, 90);
	}

	private void drawText(Graphics g) {
		g.setColor(Color.WHITE);

		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Welcome", 270, 260);

		g.setFont(new Font("Arial", Font.PLAIN, 26));
		g.drawString("Have a nice day", 300, 300);
	}
}

//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Random;
//
//public class SplashPanel extends JPanel {
//
//	private static final int STAR_COUNT = 60;
//	private Star[] stars = new Star[STAR_COUNT];
//	private Random rand = new Random();
//
//	public SplashPanel() {
//		setBackground(Color.BLACK);
//
//		// Create stars at the top
//		for (int i = 0; i < STAR_COUNT; i++) {
//			int x = rand.nextInt(800);
//			int y = rand.nextInt(150);
//			stars[i] = new Star(x, y);
//		}
//
//		// Animation timer
//		Timer timer = new Timer(120, e -> {
//			for (Star s : stars)
//				s.update();
//			repaint();
//		});
//		timer.start();
//	}
//
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//
//		drawStars(g);
//		drawMoon(g);
//		drawMountains(g);
//		drawLand(g);
//		drawTrees(g);
//		drawText(g);
//	}
//
//	private void drawStars(Graphics g) {
//		for (Star s : stars)
//			s.draw(g);
//	}
//
//	private void drawMoon(Graphics g) {
//		g.setColor(new Color(255, 255, 200));
//		g.fillOval(600, 40, 80, 80);
//	}
//
//	private void drawMountains(Graphics g) {
//		g.setColor(new Color(60, 60, 60));
//
//		int[] x1 = { 0, 150, 300 };
//		int[] y1 = { 380, 200, 380 };
//		g.fillPolygon(x1, y1, 3);
//
//		int[] x2 = { 250, 450, 650 };
//		int[] y2 = { 380, 180, 380 };
//		g.fillPolygon(x2, y2, 3);
//
//		int[] x3 = { 500, 650, 800 };
//		int[] y3 = { 380, 220, 380 };
//		g.fillPolygon(x3, y3, 3);
//	}
//
//	private void drawLand(Graphics g) {
//		g.setColor(new Color(34, 139, 34));
//		g.fillRect(0, 380, getWidth(), 120);
//	}
//
//	private void drawTrees(Graphics g) {
//		for (int i = 0; i < 6; i++) {
//			int x = 60 + i * 120;
//
//			g.setColor(new Color(139, 69, 19));
//			g.fillRect(x, 330, 20, 50);
//
//			g.setColor(new Color(0, 120, 0));
//			g.fillOval(x - 20, 290, 60, 60);
//		}
//	}
//
//	private void drawText(Graphics g) {
//		g.setColor(Color.WHITE);
//
//		g.setFont(new Font("Arial", Font.BOLD, 48));
//		g.drawString("Welcome", 270, 230);
//
//		g.setFont(new Font("Arial", Font.PLAIN, 26));
//		g.drawString("Have a nice day", 300, 270);
//	}
//}
