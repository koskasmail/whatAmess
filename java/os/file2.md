Nice touch — a splash / welcome screen fits this app perfectly 😄
Below is a **Java 1.8–compatible Swing welcome screen** that appears **before** the main program starts.

### What it does

* Shows a welcome window with:

  ```
  WELCOME
  TO
  MYOS
  ```

  (big font, centered, 3 lines)
* Empty line
* `written by : Radmidocs Team`
* Empty line
* `Have a nice day.`
* After a short delay (e.g. 3 seconds), it **closes and opens your MainApp**

---

## 1️⃣ Welcome Screen Class (`WelcomeScreen.java`)

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

        // Big font for title
        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font normalFont = new Font("Arial", Font.PLAIN, 16);

        panel.add(Box.createVerticalStrut(20));

        panel.add(createLabel("WELCOME", titleFont));
        panel.add(createLabel("TO", titleFont));
        panel.add(createLabel("MYOS", titleFont));

        panel.add(Box.createVerticalStrut(20));

        panel.add(createLabel("written by : Radmidocs Team", normalFont));

        panel.add(Box.createVerticalStrut(20));

        panel.add(createLabel("Have a nice day.", normalFont));

        add(panel);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WelcomeScreen welcome = new WelcomeScreen();
                welcome.setVisible(true);

                // Timer to close welcome screen and open main app
                new javax.swing.Timer(3000, new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        welcome.dispose();
                        new MainApp().setVisible(true);
                    }
                }).start();
            }
        });
    }
}
```

---

## 2️⃣ Important Change

👉 **Remove the `main()` method from `MainApp`**
(or just don’t use it)

The program should now start from **`WelcomeScreen`**, not `MainApp`.

---

## 3️⃣ Program Flow

1. App starts
2. Welcome screen appears:

   ```
   WELCOME
   TO
   MYOS

   written by : Radmidocs Team

   Have a nice day.
   ```
3. After 3 seconds → welcome screen closes
4. Main error manager window opens

---

## ✅ Java 8 Compatible

* No lambdas
* Uses `Timer`, `BoxLayout`, and standard Swing components
* Clean and simple

If you want:

* 🎨 custom colors
* 🖼️ logo image
* ⏱️ button like “Continue”
* 🔄 fade-in animation

Just tell me 👍
