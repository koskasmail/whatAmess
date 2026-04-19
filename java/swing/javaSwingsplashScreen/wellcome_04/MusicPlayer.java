package wellcome_04;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {

	private static Clip clip;

	private static final String BASE64_WAV = "UklGRlQAAABXQVZFZm10IBAAAAABAAEAESsAACJWAAACABAAZGF0YQAAAECAgICAgH9/f39/gICAgICA"
			+ "gH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICA"
			+ "gH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICA"
			+ "gH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICA"
			+ "gH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICAgH9/f39/gICAgICA" + "gH9/f39/gA==";

	public static void playLoop() {
		try {
			byte[] wavBytes = Base64.getDecoder().decode(BASE64_WAV);

			AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(wavBytes));

			clip = AudioSystem.getClip();
			clip.open(stream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (Exception e) {
			System.out.println("Audio error: " + e.getMessage());
		}
	}
}
