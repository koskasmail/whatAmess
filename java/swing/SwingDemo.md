<a name="topage"></a>

# SwingDemo

### example #1

```
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingDemo {

	public SwingDemo() {
		// Create a new JFrame
		JFrame frame = new JFrame("Simple Form");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 150);

		// Create a panel to hold the components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));

		// Create labels and text fields
		JLabel firstNameLabel = new JLabel("First Name:");
		JTextField firstNameField = new JTextField();
		JLabel lastNameLabel = new JLabel("Last Name:");
		JTextField lastNameField = new JTextField();

		// Create buttons
		JButton okButton = new JButton("OK");
		JButton cleanButton = new JButton("Clear");

		// Add action listeners for buttons
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				JOptionPane.showMessageDialog(frame, "First Name: " + firstName + "\nLast Name: " + lastName);
			}
		});

		cleanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstNameField.setText("");
				lastNameField.setText("");
			}
		});

		// Add components to the panel
		panel.add(firstNameLabel);
		panel.add(firstNameField);
		panel.add(lastNameLabel);
		panel.add(lastNameField);
		panel.add(okButton);
		panel.add(cleanButton);

		// Add panel to the frame
		frame.add(panel);

		// Set the frame visibility to true
		frame.setVisible(true);
	}
}
```

-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
