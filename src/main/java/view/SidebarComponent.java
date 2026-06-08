package view;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// builds the sidebar navigation buttons used in the app.
public class SidebarComponent {
    private final VBox root;
    private final Map<String, Button> buttons = new LinkedHashMap<>();

// This method performs one part of the class behavior.
    public SidebarComponent() {
        root = new VBox(14);
        root.getStyleClass().add("sidebar");
        root.setPadding(new Insets(18));
        root.setPrefWidth(220);

        Label logoTitle = new Label("ETHICS INTEL");
        logoTitle.getStyleClass().add("sidebar-logo");
        root.getChildren().add(logoTitle);
    }

// This method performs one part of the class behavior.
    public void addItem(String title, Runnable action) {
        Button button = new Button(title);
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> {
            setActive(title);
            action.run();
        });
        buttons.put(title, button);
        root.getChildren().add(button);
    }

// This method performs one part of the class behavior.
    public void addExitItem(String title, Runnable action) {
        Button button = new Button("⏻ " + title);
        button.getStyleClass().addAll("sidebar-button", "sidebar-button-exit");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> action.run());
        root.getChildren().add(button);
    }

// This method performs one part of the class behavior.
    public void setActive(String title) {
        buttons.forEach((name, button) -> {
            button.getStyleClass().remove("sidebar-button-active");
// If the condition inside the parentheses is true, the code inside the block will run.
            if (name.equals(title)) {
                button.getStyleClass().add("sidebar-button-active");
            }
        });
    }

// This method performs one part of the class behavior.
    public VBox getRoot() {
// Return this value to the method caller so the result can be used elsewhere.
        return root;
    }
}
