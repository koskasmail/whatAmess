package wellcome_04;

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
