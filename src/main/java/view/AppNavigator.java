package view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.EthicalAnalysisController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.AuditTrail;

public class AppNavigator {
    private final Stage primaryStage;
    private final BorderPane rootLayout;
    private final StackPane centerPane;
    private final SidebarComponent sidebar;
    private final EthicalAnalysisController controller;
    private final Map<String, AppPage> pages = new LinkedHashMap<>();
    private final List<AuditTrail> auditHistory = new ArrayList<>();
    private final BackgroundVideoPane backgroundPane;
    private boolean darkTheme = true;

    public AppNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new EthicalAnalysisController();
        this.rootLayout = new BorderPane();
        this.rootLayout.getStyleClass().add("root-pane");
        this.centerPane = new StackPane();
        this.centerPane.getStyleClass().add("page-container");
        this.sidebar = new SidebarComponent();
        this.backgroundPane = new BackgroundVideoPane();

        buildPages();
        configureSidebar();

        rootLayout.setLeft(sidebar.getRoot());

        ScrollPane contentScroll = new ScrollPane(centerPane);
        contentScroll.setFitToWidth(true);
        contentScroll.setFitToHeight(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        rootLayout.setCenter(contentScroll);

        StackPane masterPane = new StackPane();
        masterPane.getChildren().addAll(backgroundPane, rootLayout);
        masterPane.setOpacity(0);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(1400, screenBounds.getWidth() - 20);
        double height = Math.min(900, screenBounds.getHeight() - 20);

        Scene scene = new Scene(masterPane, width, height);
        scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ethical Decision Engine");
        primaryStage.setMinWidth(Math.min(1000, screenBounds.getWidth()));
        primaryStage.setMinHeight(Math.min(700, screenBounds.getHeight()));
        primaryStage.setOnCloseRequest(this::handleWindowCloseRequest);
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setMaximized(true);

        switchToPage("Dashboard");
    }

    private void buildPages() {
        pages.put("Dashboard", new DashboardPage(controller, this::getAuditHistory));
        pages.put("New Analysis", new NewAnalysisPage(controller, this::addAuditTrail, this));
        pages.put("Audit History", new AuditHistoryPage(this::getAuditHistory));
        pages.put("Reports", new ReportsPage(this::getAuditHistory));
        pages.put("Settings", new SettingsPage(this::setDarkTheme, () -> darkTheme));
    }

    private void configureSidebar() {
        pages.keySet().forEach(page -> sidebar.addItem(page, () -> switchToPage(page)));
        sidebar.addExitItem("Exit", this::confirmExit);
    }

    private void switchToPage(String title) {
        AppPage page = pages.get(title);
        if (page == null) {
            return;
        }

        page.refresh();
        setPageContent(page.getView());
        sidebar.setActive(title);
    }

    private void setPageContent(Node pageNode) {
        centerPane.getChildren().setAll(pageNode);
        TransitionUtils.applySlideFadeIn(pageNode);
    }

    private void handleWindowCloseRequest(WindowEvent event) {
        if (!showExitConfirmation()) {
            event.consume();
        } else {
            performExit();
        }
    }

    private void confirmExit() {
        if (showExitConfirmation()) {
            performExit();
        }
    }

    private boolean showExitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Exit Application");
        alert.setContentText("Are you sure you want to exit the Ethical Decision Engine?");

        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(exitButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == exitButton;
    }

    private void performExit() {
        if (backgroundPane != null) {
            backgroundPane.stop();
        }
        Platform.exit();
        System.exit(0);
    }

    private List<AuditTrail> getAuditHistory() {
        return List.copyOf(auditHistory);
    }

    private void addAuditTrail(AuditTrail auditTrail) {
        auditHistory.add(0, auditTrail);
        pages.values().forEach(AppPage::refresh);
    }

    private void setDarkTheme(boolean enabled) {
        if (enabled) {
            rootLayout.getStyleClass().remove("light-theme");
            darkTheme = true;
        } else {
            if (!rootLayout.getStyleClass().contains("light-theme")) {
                rootLayout.getStyleClass().add("light-theme");
            }
            darkTheme = false;
        }
    }

    public void show() {
        primaryStage.show();
        FadeTransition fade = new FadeTransition(Duration.millis(900), primaryStage.getScene().getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
