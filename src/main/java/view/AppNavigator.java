package view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import controller.EthicalAnalysisController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.AuditTrail;
import view.TransitionUtils;
import view.BackgroundVideoPane;

public class AppNavigator {
    private final Stage primaryStage;
    private final BorderPane rootLayout;
    private final StackPane centerPane;
    private final SidebarComponent sidebar;
    private final EthicalAnalysisController controller;
    private final Map<String, AppPage> pages = new LinkedHashMap<>();
    private final List<AuditTrail> auditHistory = new ArrayList<>();
    private boolean darkTheme = true;

    public AppNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new EthicalAnalysisController();
        this.rootLayout = new BorderPane();
        this.rootLayout.getStyleClass().add("root-pane");
        this.centerPane = new StackPane();
        this.centerPane.getStyleClass().add("page-container");
        this.sidebar = new SidebarComponent();

        buildPages();
        configureSidebar();

        rootLayout.setLeft(sidebar.getRoot());
        rootLayout.setCenter(centerPane);

        StackPane masterPane = new StackPane();
        masterPane.getChildren().addAll(new BackgroundVideoPane(), rootLayout);

        Scene scene = new Scene(masterPane, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ethical Decision Engine");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(820);

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
    }
}
