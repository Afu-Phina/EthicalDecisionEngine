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
    public Node getView() {
        return root;
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public void refresh() {
        themeToggle.setSelected(currentThemeSupplier.get());
    }

    private VBox createSection(String titleText, Node... children) {
        Label title = new Label(titleText);
        title.getStyleClass().add("card-title");
        VBox sectionBody = new VBox(10, children);
        return new VBox(8, title, sectionBody);
    }

    private TextField createTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.getStyleClass().add("input-area");
        return field;
    }

    private void saveSettings() {
        statusLabel.setText("Settings saved. Theme changes will apply immediately.");
    }
}
