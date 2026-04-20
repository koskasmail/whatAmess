package wellcome_04;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SplashPanel extends JPanel {

    private static final int STAR_COUNT = 300;   // more stars
    private Star[] stars = new Star[STAR_COUNT];
    private Random rand = new Random();

    public SplashPanel() {
        setBackground(Color.BLACK);

        // Create stars (positions will be scaled on draw)
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(rand.nextFloat(), rand.nextFloat());
        }

        // Animation timer
        Timer timer = new Timer(80, e -> {
            for (Star s : stars) s.update();
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
        int w = getWidth();
        int h = getHeight();

        for (Star s : stars) {
            s.draw(g, w, h);
        }
    }

    private void drawMoon(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        int size = Math.min(w, h) / 6;
        int x = (int)(w * 0.75);
        int y = (int)(h * 0.10);

        g.setColor(new Color(255, 255, 220));
        g.fillOval(x, y, size, size);
    }

    private void drawText(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.WHITE);

        // Big Welcome
        g.setFont(new Font("Arial", Font.BOLD, h / 10));
        String welcome = "Welcome";
        int welcomeWidth = g.getFontMetrics().stringWidth(welcome);
        g.drawString(welcome, (w - welcomeWidth) / 2, h / 2);

        // Smaller subtitle
        g.setFont(new Font("Arial", Font.PLAIN, h / 20));
        String sub = "Have a nice day";
        int subWidth = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (w - subWidth) / 2, (h / 2) + (h / 15));
                
        // Smallest subtitle
        g.setFont(new Font("Arial", Font.PLAIN, h / 40));
        String sub2 = "Press Any Key to Continue...";
        int subWidth2 = g.getFontMetrics().stringWidth(sub2);
        g.drawString(sub2, 10, h - 100);
    }
}
