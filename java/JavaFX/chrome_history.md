<a name="topage"></a>

# chrome_history

## Project structure

```text
workspace-cleaner/
├── build.gradle
├── settings.gradle
└── src
    └── main
        ├── java
        │   └── app
        │       ├── Main.java
        │       ├── WorkspaceCleanerApp.java
        │       └── MainController.java
        └── resources
            └── ui
                └── main.fxml
```

---

## Gradle files

**`settings.gradle`**

```gradle
rootProject.name = 'workspace-cleaner'
```

**`build.gradle`**

```gradle
plugins {
    id 'application'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.openjfx:javafx-controls:21'
    implementation 'org.openjfx:javafx-fxml:21'
}

application {
    mainClass = 'app.Main'
}

run {
    jvmArgs = [
            '--add-modules', 'javafx.controls,javafx.fxml'
    ]
}
```

---

## Java classes

**`Main.java`**

```java
package app;

public class Main {
    public static void main(String[] args) {
        WorkspaceCleanerApp.main(args);
    }
}
```

**`WorkspaceCleanerApp.java`**

```java
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WorkspaceCleanerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ui/main.fxml"));
        Scene scene = new Scene(loader.load(), 600, 500);
        stage.setTitle("Eclipse Workspace Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

**`MainController.java`**

```java
package app;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class MainController {

    @FXML
    private TextField workspaceRootField;

    @FXML
    private TextField daysField;

    @FXML
    private TextArea logArea;

    @FXML
    private void onBrowse() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Workspace Root");
        Window window = logArea.getScene().getWindow();
        File dir = chooser.showDialog(window);
        if (dir != null) {
            workspaceRootField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    private void onScan() {
        log("Scan workspaces (stub)");
    }

    @FXML
    private void onDeleteOld() {
        log("Delete old workspaces (stub)");
    }

    @FXML
    private void onMove() {
        log("Move remaining workspaces (stub)");
    }

    @FXML
    private void onResetConfig() {
        log("Reset Eclipse config (stub)");
    }

    @FXML
    private void onCleanHistory() {
        log("Clean recent workspace history (stub)");
    }

    private void log(String msg) {
        logArea.appendText(msg + "\n");
    }
}
```

---

## FXML layout

**`main.fxml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>

<VBox spacing="12" xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="app.MainController"
      alignment="TOP_LEFT" style="-fx-padding: 20;">

    <Label text="Eclipse Workspace Manager" style="-fx-font-size: 22;" />

    <HBox spacing="10">
        <Label text="Workspace Root:" />
        <TextField fx:id="workspaceRootField" prefWidth="300"/>
        <Button text="Browse" onAction="#onBrowse"/>
    </HBox>

    <HBox spacing="10">
        <Label text="Delete older than (days):" />
        <TextField fx:id="daysField" text="30" prefWidth="80"/>
    </HBox>

    <Button text="Scan Workspaces" onAction="#onScan"/>
    <Button text="Delete Old Workspaces" onAction="#onDeleteOld"/>
    <Button text="Move Remaining Workspaces" onAction="#onMove"/>
    <Button text="Reset Eclipse Configuration" onAction="#onResetConfig"/>
    <Button text="Clean Recent Workspace History" onAction="#onCleanHistory"/>

    <Label text="Log Output:" style="-fx-font-size: 16;" />
    <TextArea fx:id="logArea" prefHeight="250" wrapText="true"/>
</VBox>
```

---

You can now open this folder in IntelliJ/Eclipse, run `gradle run`, and start wiring in your workspace‑cleaning logic.

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
