package wellcome_01;

import java.awt.*;
import java.util.Random;

public class Star {

	private int x, y;
	private int size;
	private float brightness;
	private boolean increasing;
	private Random rand = new Random();

	public Star(int x, int y) {
		this.x = x;
		this.y = y;
		this.size = 2 + rand.nextInt(3);
		this.brightness = rand.nextFloat();
		this.increasing = rand.nextBoolean();
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

	public void draw(Graphics g) {
		int alpha = (int) (brightness * 255);

		// Clamp alpha to valid range
		alpha = Math.max(0, Math.min(255, alpha));

		g.setColor(new Color(255, 255, 200, alpha));
		g.fillOval(x, y, size, size);
	}
}
