<a name="topage"></a>

# menu

### Overview

Below is a **single‑project JavaFX example** that does what you described:

- Splash screen: **“Welcome” + version**, shows for **1 second**, then opens main window  
- Main window:  
  - **Menu bar with icons**  
  - **Toolbar with icons**  
  - **Title**  
  - **Bookmark‑like list**  
  - **Bottom buttons**: Add, Delete, Edit, Exit  
- Each button opens its **own small screen/dialog**  
- Data saved/loaded from **JSON** using **Gson**  
- Exit: shows **“Goodbye, see you soon ✨”** splash with stars for 1 second, then closes app  

---

### Gradle (with Gson + JavaFX)

```gradle
plugins {
    id 'application'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.openjfx:javafx-controls:21'
    implementation 'com.google.code.gson:gson:2.10.1'
}

application {
    mainClass = 'app.Main'
}

run {
    jvmArgs = ['--add-modules', 'javafx.controls']
}
```

---

### Main class

```java
package app;

public class Main {
    public static void main(String[] args) {
        SplashApp.main(args);
    }
}
```

---

### Splash + Main App

```java
package app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SplashApp extends Application {

    private static final Path JSON_FILE = Path.of("bookmarks.json");
    private final Gson gson = new Gson();
    private final List<String> bookmarks = new ArrayList<>();
    private ListView<String> listView;

    @Override
    public void start(Stage primaryStage) {
        showWelcomeSplash(primaryStage);
    }

    private void showWelcomeSplash(Stage stage) {
        Label title = new Label("Welcome");
        title.setStyle("-fx-font-size: 32;");
        Label version = new Label("Version 1.0.0");

        VBox root = new VBox(10, title, version);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #222; -fx-text-fill: white;");
        Scene scene = new Scene(root, 400, 200);

        stage.setScene(scene);
        stage.setTitle("Splash");
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            stage.close();
            showMainWindow();
        });
        pause.play();
    }

    private void showMainWindow() {
        Stage stage = new Stage();
        loadBookmarks();

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem addItem = new MenuItem("Add", new ImageView());
        MenuItem deleteItem = new MenuItem("Delete", new ImageView());
        MenuItem editItem = new MenuItem("Edit", new ImageView());
        MenuItem exitItem = new MenuItem("Exit", new ImageView());
        fileMenu.getItems().addAll(addItem, deleteItem, editItem, exitItem);
        menuBar.getMenus().add(fileMenu);

        // Toolbar
        ToolBar toolBar = new ToolBar(
                new Button("", new ImageView()),
                new Button("", new ImageView()),
                new Button("", new ImageView())
        );

        Label title = new Label("Bookmark Manager");
        title.setStyle("-fx-font-size: 18; -fx-padding: 5 0 5 0;");

        listView = new ListView<>();
        listView.getItems().addAll(bookmarks);

        // Bottom buttons
        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");
        Button editBtn = new Button("Edit");
        Button exitBtn = new Button("Exit");

        HBox bottomBar = new HBox(10, addBtn, deleteBtn, editBtn, exitBtn);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10));

        // Wire actions
        addItem.setOnAction(e -> showAddScreen(stage));
        deleteItem.setOnAction(e -> showDeleteScreen(stage));
        editItem.setOnAction(e -> showEditScreen(stage));
        exitItem.setOnAction(e -> onExit(stage));

        addBtn.setOnAction(e -> showAddScreen(stage));
        deleteBtn.setOnAction(e -> showDeleteScreen(stage));
        editBtn.setOnAction(e -> showEditScreen(stage));
        exitBtn.setOnAction(e -> onExit(stage));

        VBox root = new VBox(menuBar, toolBar, title, listView, bottomBar);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Workspace Bookmark Manager");
        stage.show();
    }

    private void showAddScreen(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField nameField = new TextField();
        Button ok = new Button("OK");
        ok.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                bookmarks.add(name);
                listView.getItems().setAll(bookmarks);
                saveBookmarks();
            }
            dialog.close();
        });
        VBox root = new VBox(10, new Label("Add bookmark:"), nameField, ok);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        dialog.setScene(new Scene(root, 300, 150));
        dialog.setTitle("Add");
        dialog.showAndWait();
    }

    private void showDeleteScreen(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        ListView<String> localList = new ListView<>();
        localList.getItems().addAll(bookmarks);
        Button delete = new Button("Delete selected");
        delete.setOnAction(e -> {
            String sel = localList.getSelectionModel().getSelectedItem();
            if (sel != null) {
                bookmarks.remove(sel);
                listView.getItems().setAll(bookmarks);
                saveBookmarks();
            }
            dialog.close();
        });
        VBox root = new VBox(10, new Label("Delete bookmark:"), localList, delete);
        root.setPadding(new Insets(10));
        dialog.setScene(new Scene(root, 300, 250));
        dialog.setTitle("Delete");
        dialog.showAndWait();
    }

    private void showEditScreen(Stage owner) {
        String sel = listView.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField nameField = new TextField(sel);
        Button ok = new Button("Save");
        ok.setOnAction(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                int idx = bookmarks.indexOf(sel);
                if (idx >= 0) {
                    bookmarks.set(idx, newName);
                    listView.getItems().setAll(bookmarks);
                    saveBookmarks();
                }
            }
            dialog.close();
        });
        VBox root = new VBox(10, new Label("Edit bookmark:"), nameField, ok);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        dialog.setScene(new Scene(root, 300, 150));
        dialog.setTitle("Edit");
        dialog.showAndWait();
    }

    private void onExit(Stage mainStage) {
        saveBookmarks();
        mainStage.close();
        showGoodbyeSplash();
    }

    private void showGoodbyeSplash() {
        Stage stage = new Stage();
        Label msg = new Label("Goodbye, see you soon ✨✨✨");
        msg.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        VBox root = new VBox(msg);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #000;");
        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Goodbye");
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            stage.close();
            System.exit(0);
        });
        pause.play();
    }

    private void loadBookmarks() {
        try {
            if (Files.exists(JSON_FILE)) {
                try (FileReader reader = new FileReader(JSON_FILE.toFile())) {
                    Type type = new TypeToken<List<String>>() {}.getType();
                    List<String> loaded = gson.fromJson(reader, type);
                    if (loaded != null) {
                        bookmarks.clear();
                        bookmarks.addAll(loaded);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private void saveBookmarks() {
        try (FileWriter writer = new FileWriter(JSON_FILE.toFile())) {
            gson.toJson(bookmarks, writer);
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

You can paste this into a Gradle JavaFX project, add `gson` as shown, and you’ll have:

- Splash → main window → JSON‑backed bookmarks → goodbye splash → exit.

----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
