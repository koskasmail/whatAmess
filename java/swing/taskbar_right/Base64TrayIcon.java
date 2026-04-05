package wsMain;

import java.awt.*;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Base64TrayIcon {

    public static Image decodeBase64Image(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
