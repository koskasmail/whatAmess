<a name="topage"></a>

# folder bookmark

Here is the complete Java 1.8 implementation of your **Path Manager** application using **JavaFX**. It matches your PowerShell WPF application feature-for-feature, including the JSON storage, search filter, column sorting, custom splash screens, and toast notifications.

### Prerequisites

Ensure you are using **JDK 8 with JavaFX built-in** (Standard Oracle JDK 8 or OpenJDK 8 bundled with OpenJFX). The `gson-2.8.9.jar` library is used for JSON handling.

---

### Step 1: `PathRecord.java` (Model Class)

Create a POJO model to represent each record row:

```java
package com.ramidocs.pathmanager;

public class PathRecord {
    private String name;
    private String description;
    private String path;

    public PathRecord() {
        this.name = "";
        this.description = "";
        this.path = "";
    }

    public PathRecord(String name, String description, String path) {
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}

```

---

### Step 2: `PathManagerApp.java` (Main Application)

This is the primary JavaFX entry point containing the full GUI, logic, splash screen, and file handlers:

```java
package com.ramidocs.pathmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadowEffect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PathManagerApp extends Application {

    private static final String JSON_FILE_NAME = "paths3fld.json";
    
    // UI Controls
    private TextField txtName;
    private TextField txtDescription;
    private TextField txtPath;
    private TextField txtFilter;
    private TableView<PathRecord> tableRecords;
    private StackPane toastBorder;
    private Text txtToast;

    // Data Collections
    private final ObservableList<PathRecord> masterRecords = FXCollections.observableArrayList();
    private FilteredList<PathRecord> filteredData;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Show Welcome Splash Screen
        showSplashScreen("RamiDocs AnyWhere", "Path Manager", 3000, () -> {
            
            // Initialize Main Window UI after splash
            initMainWindow(primaryStage);
        });
    }

    private void initMainWindow(Stage stage) {
        stage.setTitle("Path Manager");

        // Input Fields Grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(8);
        formGrid.setPadding(new Insets(10));

        txtName = new TextField();
        txtDescription = new TextField();
        txtPath = new TextField();
        Button btnBrowse = new Button("Browse...");

        formGrid.add(new Label("Name:"), 0, 0);
        formGrid.add(txtName, 1, 0, 2, 1);
        GridPane.setHgrow(txtName, Priority.ALWAYS);

        formGrid.add(new Label("Description:"), 0, 1);
        formGrid.add(txtDescription, 1, 1, 2, 1);

        formGrid.add(new Label("Folder Path:"), 0, 2);
        formGrid.add(txtPath, 1, 2);
        formGrid.add(btnBrowse, 2, 2);
        GridPane.setHgrow(txtPath, Priority.ALWAYS);

        TitledPane groupDetails = new TitledPane("Record Details", formGrid);
        groupDetails.setCollapsible(false);

        // Action Buttons Bar
        HBox buttonBar = new HBox(8);
        buttonBar.setPadding(new Insets(5, 0, 10, 0));

        Button btnOpen = new Button("Open Folder");
        Button btnSave = new Button("Save / Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear Inputs");
        Button btnExit = new Button("Exit");

        btnOpen.setPrefWidth(100);
        btnSave.setPrefWidth(100);
        btnDelete.setPrefWidth(80);
        btnClear.setPrefWidth(90);
        btnExit.setPrefWidth(80);

        buttonBar.getChildren().addAll(btnOpen, btnSave, btnDelete, btnClear, btnExit);

        // Filter Bar
        HBox filterBar = new HBox(8);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(0, 0, 8, 0));

        Label lblFilter = new Label("Filter:");
        lblFilter.setFont(Font.font("System", FontWeight.BOLD, 12));
        txtFilter = new TextField();
        Button btnClearFilter = new Button("Clear");

        HBox.setHgrow(txtFilter, Priority.ALWAYS);
        filterBar.getChildren().addAll(lblFilter, txtFilter, btnClearFilter);

        // Data TableView Setup
        tableRecords = new TableView<>();
        
        TableColumn<PathRecord, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(180);

        TableColumn<PathRecord, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDesc.setPrefWidth(220);

        TableColumn<PathRecord, String> colPath = new TableColumn<>("Folder Path");
        colPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        colPath.setPrefWidth(370);

        tableRecords.getColumns().addAll(colName, colDesc, colPath);

        // Filter & Sort Logic Setup
        filteredData = new FilteredList<>(masterRecords, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerFilter = newValue.toLowerCase().trim();
                return (record.getName() != null && record.getName().toLowerCase().contains(lowerFilter)) ||
                       (record.getDescription() != null && record.getDescription().toLowerCase().contains(lowerFilter)) ||
                       (record.getPath() != null && record.getPath().toLowerCase().contains(lowerFilter));
            });
        });

        SortedList<PathRecord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableRecords.comparatorProperty());
        tableRecords.setItems(sortedData);

        // Overlay Toast Notification
        toastBorder = new StackPane();
        toastBorder.setStyle("-fx-background-color: #333333; -fx-background-radius: 5px; -fx-padding: 8px 15px;");
        toastBorder.setMaxWidth(Region.USE_PREF_SIZE);
        toastBorder.setMaxHeight(Region.USE_PREF_SIZE);
        toastBorder.setOpacity(0);

        txtToast = new Text("Open folder - please wait...");
        txtToast.setFill(Color.WHITE);
        txtToast.setFont(Font.font("System", FontWeight.BOLD, 13));
        toastBorder.getChildren().add(txtToast);

        // Main Layout Container
        VBox contentBox = new VBox(formGrid, groupDetails, buttonBar, filterBar, tableRecords);
        VBox.setVgrow(tableRecords, Priority.ALWAYS);

        StackPane mainRoot = new StackPane(contentBox, toastBorder);
        StackPane.setAlignment(toastBorder, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toastBorder, new Insets(0, 0, 20, 0));

        // Event Listeners
        btnBrowse.setOnAction(e -> handleBrowse(stage));
        btnSave.setOnAction(e -> handleSave());
        btnDelete.setOnAction(e -> handleDelete());
        btnOpen.setOnAction(e -> handleOpenFolder());
        btnClear.setOnAction(e -> clearFields());
        btnClearFilter.setOnAction(e -> txtFilter.setText(""));
        btnExit.setOnAction(e -> stage.close());

        tableRecords.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtName.setText(newVal.getName());
                txtDescription.setText(newVal.getDescription());
                txtPath.setText(newVal.getPath());
            }
        });

        tableRecords.setRowFactory(tv -> {
            TableRow<PathRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleOpenFolder();
                }
            });
            return row;
        });

        // Window Closing Hook (Triggers Exit Splash)
        stage.setOnCloseRequest(event -> {
            event.consume(); // Prevent immediate closing
            showSplashScreen("See you soon", "RamiDocs...", 3000, Platform::exit);
        });

        // Load JSON Data and Show Main Stage
        loadJsonData();
        Scene scene = new Scene(mainRoot, 820, 560);
        stage.setScene(scene);
        stage.show();
    }

    // --- Action Handlers ---

    private void handleBrowse(Stage ownerStage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Folder");
        File selectedDirectory = chooser.showDialog(ownerStage);
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void handleSave() {
        String name = txtName.getText().trim();
        String desc = txtDescription.getText().trim();
        String path = txtPath.getText().trim();

        if (name.isEmpty() || path.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter both a Name and a Folder Path.");
            return;
        }

        // Search for existing entry (Edit mode)
        PathRecord existing = masterRecords.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setDescription(desc);
            existing.setPath(path);
            tableRecords.refresh();
        } else {
            masterRecords.add(new PathRecord(name, desc, path));
        }

        saveJsonData();
        clearFields();
    }

    private void handleDelete() {
        PathRecord selected = tableRecords.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select Record", "Please select a record from the list to delete.");
            return;
        }

        masterRecords.remove(selected);
        saveJsonData();
        clearFields();
    }

    private void handleOpenFolder() {
        String path = txtPath.getText().trim();
        if (!path.isEmpty() && Files.exists(Paths.get(path))) {
            showToast("Open folder - please wait...");
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(new File(path));
                } catch (IOException e) {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Path Error", "Failed to open directory."));
                }
            }).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Path Error", "The path does not exist or is empty.");
        }
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtPath.clear();
        tableRecords.getSelectionModel().clearSelection();
    }

    // --- JSON Persistence ---

    private void loadJsonData() {
        masterRecords.clear();
        File file = new File(JSON_FILE_NAME);
        if (!file.exists()) {
            saveJsonData(); // Create empty JSON file if missing
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<PathRecord>>() {}.getType();
            List<PathRecord> loadedList = gson.fromJson(reader, listType);
            if (loadedList != null) {
                masterRecords.addAll(loadedList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveJsonData() {
        try (Writer writer = new FileWriter(JSON_FILE_NAME)) {
            gson.toJson(new ArrayList<>(masterRecords), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- UI Visual Extras ---

    private void showToast(String message) {
        txtToast.setText(message);
        toastBorder.setOpacity(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), toastBorder);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        fadeOut.play();
    }

    private void showSplashScreen(String line1, String line2, int displayMillis, Runnable onFinished) {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Text textLine1 = new Text(line1);
        textLine1.setFont(Font.font("System", FontWeight.BOLD, 26));
        textLine1.setFill(Color.web("#007ACC"));

        Text textLine2 = new Text(line2);
        textLine2.setFont(Font.font("System", FontWeight.SEMI_BOLD, 20));
        textLine2.setFill(Color.WHITE);

        vbox.getChildren().addAll(textLine1, textLine2);

        BorderPane root = new BorderPane(vbox);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-background-radius: 12px; -fx-border-color: #007ACC; -fx-border-radius: 12px; -fx-border-width: 2px;");

        DropShadowEffect shadow = new DropShadowEffect();
        shadow.setColor(Color.web("#007ACC"));
        shadow.setRadius(20);
        shadow.setSpread(0.2);
        root.setEffect(shadow);

        Scene splashScene = new Scene(root, 450, 220);
        splashScene.setFill(Color.TRANSPARENT);
        splashStage.setScene(splashScene);
        splashStage.centerOnScreen();
        splashStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(displayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                splashStage.close();
                if (onFinished != null) {
                    onFinished.run();
                }
            });
        }).start();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

```

---

### Step 3: Maven `pom.xml` Dependencies

If building via **Maven**, include the Google Gson dependency:

```xml
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.9</version>
    </dependency>
</dependencies>
```


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
