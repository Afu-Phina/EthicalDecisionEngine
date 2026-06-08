package view;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// shows UI controls for application settings such as theme and database details.
public class SettingsPage implements AppPage {
    private final VBox root;
    private final CheckBox themeToggle;
    private final TextField userNameField;
    private final TextField emailField;
    private final TextField hostField;
    private final TextField portField;
    private final TextField databaseField;
    private final CheckBox notificationsToggle;
    private final ChoiceBox<String> frameworkChoice;
    private final Label statusLabel;
    private final Consumer<Boolean> themeSwitcher;
    private final Supplier<Boolean> currentThemeSupplier;

// This method performs one part of the class behavior.
    public SettingsPage(Consumer<Boolean> themeSwitcher, Supplier<Boolean> currentThemeSupplier) {
        this.themeSwitcher = themeSwitcher;
        this.currentThemeSupplier = currentThemeSupplier;
        this.root = new VBox(22);
        this.root.setPadding(new Insets(28));
        this.root.getStyleClass().add("page-content");

        Label title = new Label("Platform Settings");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Configure theme, profile settings, database connections, and framework behavior.");
        subtitle.getStyleClass().add("page-subtitle");

        themeToggle = new CheckBox("Enable dark theme");
        themeToggle.getStyleClass().add("toggle-control");
        themeToggle.setOnAction(event -> themeSwitcher.accept(themeToggle.isSelected()));

        userNameField = createTextField("User display name");
        emailField = createTextField("User email address");
        hostField = createTextField("Database host (localhost)");
        portField = createTextField("Database port (3306)");
        databaseField = createTextField("Database schema name");
        notificationsToggle = new CheckBox("Enable notification alerts");
        notificationsToggle.getStyleClass().add("toggle-control");

        frameworkChoice = new ChoiceBox<>();
        frameworkChoice.getItems().addAll("Balanced", "Conservative", "Risk-sensitive", "Transparency-focused");
        frameworkChoice.getSelectionModel().selectFirst();
        frameworkChoice.getStyleClass().add("input-area");

        statusLabel = new Label("Update settings and save your configuration.");
        statusLabel.getStyleClass().add("status-label");

        Button saveButton = new Button("Save Settings");
        saveButton.getStyleClass().add("primary-button");
        saveButton.setOnAction(event -> saveSettings());

        root.getChildren().addAll(
                title,
                subtitle,
                createSection("User Profile", userNameField, emailField),
                createSection("Database Settings", hostField, portField, databaseField),
                createSection("Notification Settings", notificationsToggle),
                createSection("Ethical Framework", frameworkChoice),
                themeToggle,
                saveButton,
                statusLabel
        );
    }

    @Override
// This method performs one part of the class behavior.
    public Node getView() {
// Return this value to the method caller so the result can be used elsewhere.
        return root;
    }

    @Override
// This method performs one part of the class behavior.
    public String getTitle() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Settings";
    }

    @Override
// updates the page contents when the user navigates to it.
    public void refresh() {
        themeToggle.setSelected(currentThemeSupplier.get());
    }

// This method performs one part of the class behavior.
    private VBox createSection(String titleText, Node... children) {
        Label title = new Label(titleText);
        title.getStyleClass().add("card-title");
        VBox sectionBody = new VBox(10, children);
// Return this value to the method caller so the result can be used elsewhere.
        return new VBox(8, title, sectionBody);
    }

// This method performs one part of the class behavior.
    private TextField createTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.getStyleClass().add("input-area");
// Return this value to the method caller so the result can be used elsewhere.
        return field;
    }

// This method performs one part of the class behavior.
    private void saveSettings() {
        statusLabel.setText("Settings saved. Theme changes will apply immediately.");
    }
}
