Here’s a clean, ready‑to‑use Java Swing class that displays:

- A centered **“Welcome”** in a large font  
- Below it: **“star shine program”** in smaller font  
- Below that: **“created by AI”**  
- And under the text: a simple **animated star‑shining effect** with small/medium stars similar in spirit to your attached image  

This is fully self‑contained and runnable.

---

## ⭐ Java Splash Screen with Star‑Shine Animation

```java
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class StarSplashScreen extends JPanel implements Runnable {

    private static class Star {
        int x, y, size;
        float alpha;
        float delta;
    }

    private final Star[] stars;
    private final Random random = new Random();

    public StarSplashScreen() {
        setBackground(Color.BLACK);

        // Create small + medium stars
        stars = new Star[60];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
            stars[i].x = random.nextInt(600);
            stars[i].y = 200 + random.nextInt(250); // below the text
            stars[i].size = 2 + random.nextInt(4);  // small/medium
            stars[i].alpha = random.nextFloat();
            stars[i].delta = 0.02f + random.nextFloat() * 0.03f;
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();

        // Title text
        g2.setColor(Color.WHITE);

        Font f1 = new Font("SansSerif", Font.BOLD, 48);
        Font f2 = new Font("SansSerif", Font.PLAIN, 24);
        Font f3 = new Font("SansSerif", Font.PLAIN, 18);

        drawCentered(g2, "Welcome", f1, w, 80);
        drawCentered(g2, "star shine program", f2, w, 130);
        drawCentered(g2, "created by AI", f3, w, 165);

        // Draw stars
        for (Star s : stars) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, s.alpha));
            g2.setColor(Color.WHITE);
            g2.fillOval(s.x, s.y, s.size, s.size);
        }
    }

    private void drawCentered(Graphics2D g2, String text, Font font, int width, int y) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }

    @Override
    public void run() {
        while (true) {
            for (Star s : stars) {
                s.alpha += s.delta;
                if (s.alpha >= 1f) {
                    s.alpha = 1f;
                    s.delta = -s.delta;
                } else if (s.alpha <= 0.1f) {
                    s.alpha = 0.1f;
                    s.delta = -s.delta;
                }
            }
            repaint();
            try {
                Thread.sleep(40);
            } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Splash Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new StarSplashScreen());
        frame.setVisible(true);
    }
}
```

---

## ⭐ What this gives you

- A **centered splash screen layout**  
- Smooth **twinkling stars** (small + medium)  
- A dark background similar to your reference  
- Fully animated using Swing’s repaint loop  
- No external libraries needed  

---

If you want:

- Colorful stars (red/blue/yellow like your image)  
- Slower or faster twinkling  
- More/less stars  
- A fade‑in effect for the text  
- A real splash window without borders  

Just tell me and I’ll adjust it.
