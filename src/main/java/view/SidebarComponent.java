package view;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SidebarComponent {
    private final VBox root;
    private final Map<String, Button> buttons = new LinkedHashMap<>();

    public SidebarComponent() {
        root = new VBox(18);
        root.getStyleClass().add("sidebar");
        root.setPadding(new Insets(28));
        root.setPrefWidth(260);

        Label logoTitle = new Label("ETHICS INTEL");
        logoTitle.getStyleClass().add("sidebar-logo");
        root.getChildren().add(logoTitle);
    }

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

    public void setActive(String title) {
        buttons.forEach((name, button) -> {
            button.getStyleClass().remove("sidebar-button-active");
            if (name.equals(title)) {
                button.getStyleClass().add("sidebar-button-active");
            }
        });
    }

    public VBox getRoot() {
        return root;
    }
}
