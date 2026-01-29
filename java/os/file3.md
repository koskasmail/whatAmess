Perfect — here is a **clean, Java 1.8 Swing welcome screen** that **automatically closes after 3 seconds** and then **opens the main window**.
No buttons, no user action needed.

---

## ✅ Welcome Screen (Auto-Close + Open Main Window)

### `WelcomeScreen.java`

```java
import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Welcome");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("WELCOME", titleFont));
        panel.add(centerLabel("TO", titleFont));
        panel.add(centerLabel("MYOS", titleFont));

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("written by : Radmidocs Team", textFont));

        panel.add(Box.createVerticalStrut(25));

        panel.add(centerLabel("Have a nice day.", textFont));

        add(panel);
    }

    private JLabel centerLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final WelcomeScreen welcome = new WelcomeScreen();
                welcome.setVisible(true);

                // Auto-close after 3 seconds and open main window
                Timer timer = new Timer(3000, new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        welcome.dispose();       // close welcome screen
                        new MainApp().setVisible(true); // open main window
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }
}
```

---

## 🔁 Program Flow

1. Application starts
2. Welcome screen appears:

   ```
   WELCOME
   TO
   MYOS

   written by : Radmidocs Team

   Have a nice day.
   ```
3. After **3 seconds**
4. Welcome screen closes automatically
5. **MainApp window opens**

---

## ⚠️ Important

* **Start the program using `WelcomeScreen`**
* You can remove or ignore the `main()` method inside `MainApp`

---

If you want next:

* ⏳ change delay time
* 🎨 background color / gradient
* 🖼️ logo image
* ✨ fade-in / fade-out animation

Just say the word 😄
