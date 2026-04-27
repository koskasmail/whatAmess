



Got it — you want a **Java Swing window** that shows:

- A big **“Welcome”** text  
- And **shining / twinkling stars** *around the bottom of the screen*, similar to a glowing animated effect  

Below is a clean, self‑contained Swing program that does exactly that.

The stars are animated using a `Timer`, each star randomly changes brightness to create a “shining” effect.

---

# ⭐ Java Swing Program — Welcome + Shining Stars at Bottom

```java
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WelcomeWithStars extends JFrame {

    public WelcomeWithStars() {
        setTitle("Welcome Screen");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        WelcomePanel panel = new WelcomePanel();
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeWithStars::new);
    }
}

class WelcomePanel extends JPanel {

    private static final int STAR_COUNT = 40;
    private final Star[] stars = new Star[STAR_COUNT];
    private final Random rand = new Random();

    public WelcomePanel() {
        setBackground(Color.BLACK);

        // Create stars at random positions near bottom
        for (int i = 0; i < STAR_COUNT; i++) {
            int x = rand.nextInt(600);
            int y = 250 + rand.nextInt(120); // bottom area
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

class Star {
    private int x, y;
    private int size;
    private float brightness;
    private boolean increasing;
    private final Random rand = new Random();

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 2 + rand.nextInt(3);
        this.brightness = rand.nextFloat();
        this.increasing = rand.nextBoolean();
    }

    public void update() {
        // Make star twinkle
        if (increasing) {
            brightness += 0.05f;
            if (brightness >= 1f) increasing = false;
        } else {
            brightness -= 0.05f;
            if (brightness <= 0.2f) increasing = true;
        }
    }

    public void draw(Graphics g) {
        int alpha = (int) (brightness * 255);
        g.setColor(new Color(255, 255, 200, alpha));
        g.fillOval(x, y, size, size);
    }
}
```

---

# ✨ What this program does

### ✔ Shows a Swing window  
### ✔ Displays **“Welcome”** at the top  
### ✔ Draws **40 stars** randomly at the bottom  
### ✔ Stars **twinkle / shine** using animation  
### ✔ Smooth and lightweight  

---

# Want to add more effects?

I can add:

- ⭐ Moving stars  
- 🌟 Glow effect  
- 🌈 Gradient background  
- 🪐 Floating planets  
- 🎵 Background music  
- 🖼 A background image behind the welcome text  

Just tell me what style you want.
