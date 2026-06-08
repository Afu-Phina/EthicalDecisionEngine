package view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.EthicalAnalysisController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AuditTrail;

// sets up the main application window, the sidebar, and page switching logic.
public class AppNavigator {
    private final Stage primaryStage;
    private final BorderPane rootLayout;
    private final StackPane centerPane;
    private final SidebarComponent sidebar;
    private final EthicalAnalysisController controller;
    private final Map<String, AppPage> pages = new LinkedHashMap<>();
    private final List<AuditTrail> auditHistory = new ArrayList<>();
    private final BackgroundVideoPane backgroundPane;
// This field stores data that the class uses.
    private boolean darkTheme = true;

// This method performs one part of the class behavior.
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

        String cssPath = getClass().getResource("/dashboard.css").toExternalForm();
        System.out.println("[UI] Loading stylesheet: " + cssPath);
        Scene scene = new Scene(masterPane, 1280, 840);
        scene.getStylesheets().add(cssPath);
// If the condition inside the parentheses is true, the code inside the block will run.
        if (backgroundPane != null) {
            System.out.println("[UI] BackgroundVideoPane present: " + backgroundPane.getClass().getSimpleName());
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ethical Decision Engine");
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(760);

        switchToPage("Dashboard");
    }

// This method performs one part of the class behavior.
    private void buildPages() {
        pages.put("Dashboard", new DashboardPage(controller, this::getAuditHistory));
        pages.put("New Analysis", new NewAnalysisPage(controller, this::addAuditTrail, this));
        pages.put("Audit History", new AuditHistoryPage(this::getAuditHistory));
        pages.put("Reports", new ReportsPage(this::getAuditHistory));
        pages.put("Settings", new SettingsPage(this::setDarkTheme, () -> darkTheme));
    }

// This method performs one part of the class behavior.
    private void configureSidebar() {
        pages.keySet().forEach(page -> sidebar.addItem(page, () -> switchToPage(page)));
        sidebar.addExitItem("Exit", this::confirmExit);
    }

// This method performs one part of the class behavior.
    private void switchToPage(String title) {
        AppPage page = pages.get(title);
// If the condition inside the parentheses is true, the code inside the block will run.
        if (page == null) {
            return;
        }

        page.refresh();
        setPageContent(page.getView());
        sidebar.setActive(title);
    }

// This method performs one part of the class behavior.
    private void setPageContent(Node pageNode) {
        centerPane.getChildren().setAll(pageNode);
        TransitionUtils.applySlideFadeIn(pageNode);
    }

// This method performs one part of the class behavior.
    private void confirmExit() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (showExitConfirmation()) {
            performExit();
        }
    }

// This method performs one part of the class behavior.
    private boolean showExitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Exit Application");
        alert.setContentText("Are you sure you want to exit the Ethical Decision Engine?");

        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(exitButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
// Return this value to the method caller so the result can be used elsewhere.
        return result.isPresent() && result.get() == exitButton;
    }

// This method performs one part of the class behavior.
    private void performExit() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (backgroundPane != null) {
            backgroundPane.stop();
        }
        Platform.exit();
        System.exit(0);
    }

// This method performs one part of the class behavior.
    private List<AuditTrail> getAuditHistory() {
// Return this value to the method caller so the result can be used elsewhere.
        return List.copyOf(auditHistory);
    }

// This method performs one part of the class behavior.
    private void addAuditTrail(AuditTrail auditTrail) {
        auditHistory.add(0, auditTrail);
        pages.values().forEach(AppPage::refresh);
    }

// This method performs one part of the class behavior.
    private void setDarkTheme(boolean enabled) {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (enabled) {
            rootLayout.getStyleClass().remove("light-theme");
            darkTheme = true;
        } else {
// If the condition inside the parentheses is true, the code inside the block will run.
            if (!rootLayout.getStyleClass().contains("light-theme")) {
                rootLayout.getStyleClass().add("light-theme");
            }
            darkTheme = false;
        }
    }

// shows the current page or application window.
    public void show() {
        primaryStage.show();
        FadeTransition fade = new FadeTransition(Duration.millis(900), primaryStage.getScene().getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
