# fm_1

#### Myos
```java
package myos;

import jaron.Jaron;

public class Myos {

	public Myos() {
//		MainApp ma = new MainApp();
//		MainScreen ms = new MainScreen();
		Jaron jr = new Jaron();
	}

	public static void main(String[] args) {
		Myos ms = new Myos();
	}
}

```

----

#### Jaron
```java
package jaron;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Jaron {
	
	public Jaron() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final WelcomeScreen welcome = new WelcomeScreen();
				welcome.setVisible(true);

				// Auto-close after 3 seconds and open main window
				Timer timer = new Timer(1000, new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						welcome.dispose(); // close welcome screen
                 // new MainScreen().setVisible(true); // open main window
						new MainScreen();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		});
	}

///---[main]---///
	public static void main(String[] args) {
		Jaron jaron = new Jaron();
	}
}
```

----

####
```java
```

----

####
```java
```

----

####
```java
```

----

####
```java
```

----

####
```java
```

----

####
```java
```
