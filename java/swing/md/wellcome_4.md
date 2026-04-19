Yaron, I hear you — you want:

- ⭐ **Stars everywhere** (full‑screen sky)  
- ⭐ **Text inside the star area**  
- ⭐ **More stars, yellow → white**  
- 📱 **Responsive splash screen** (adapts to window resize)  
- 🌙 **Moon stays**  
- 🧹 No trees, no mountains, no sound  

Perfect.  
Below is your **clean, responsive, full‑screen star‑sky splash screen**.

Everything remains in **separate classes**.

---

# 📁 Project Structure

```
src/
 ├── Main.java
 ├── SplashFrame.java
 ├── SplashPanel.java
 └── Star.java
```

---

# ✅ **Main.java**
```java
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(SplashFrame::new);
    }
}
```

---

# ✅ **SplashFrame.java**
```java
import javax.swing.*;

public class SplashFrame extends JFrame {

    public SplashFrame() {
        setTitle("Splash Screen");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new SplashPanel());

        setVisible(true);
    }
}
```

---

# ⭐ **SplashPanel.java**  
Responsive, full‑screen stars + moon + centered text.

```java
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
    }
}
```

---

# ⭐ **Star.java**  
Responsive star positions + yellow→white color + twinkle.

```java
import java.awt.*;
import java.util.Random;

public class Star {

    private float xRatio, yRatio;  // relative positions (0–1)
    private int size;
    private float brightness;
    private boolean increasing;
    private Color baseColor;
    private Random rand = new Random();

    public Star(float xRatio, float yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;

        this.size = 1 + rand.nextInt(3);
        this.brightness = rand.nextFloat();
        this.increasing = rand.nextBoolean();

        // Random warm star color (yellow → white)
        int r = 220 + rand.nextInt(36);
        int g = 220 + rand.nextInt(36);
        int b = 150 + rand.nextInt(106);
        baseColor = new Color(r, g, b);
    }

    public void update() {
        if (increasing) {
            brightness += 0.05f;
            if (brightness >= 1f) {
                brightness = 1f;
                increasing = false;
            }
        } else {
            brightness -= 0.05f;
            if (brightness <= 0.2f) {
                brightness = 0.2f;
                increasing = true;
            }
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int x = (int)(xRatio * panelWidth);
        int y = (int)(yRatio * panelHeight);

        int alpha = (int)(brightness * 255);
        alpha = Math.max(0, Math.min(255, alpha));

        g.setColor(new Color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                alpha
        ));

        g.fillOval(x, y, size, size);
    }
}
```

---

# 🌟 What you now have

✔ **Stars everywhere** (full‑screen)  
✔ **Stars behind the text**  
✔ **Text centered inside star area**  
✔ **Moon stays**  
✔ **Stars twinkle**  
✔ **Stars in yellow → white gradient**  
✔ **Responsive layout**  
✔ **Window resize automatically adjusts everything**  
✔ **Clean, separate classes**  

This is now a **professional, animated, responsive splash screen**.

If you want next:

- 🌠 Shooting stars  
- 🌌 Milky Way band  
- 🌙 Moon glow halo  
- 🪟 Fade‑in animation  
- 🎬 Auto‑close after X seconds  

Just tell me, Yaron.
