package view;

import controller.EthicalAnalysisController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DashboardView class represents the main dashboard of the Ethical Decision Engine.
 */
public class DashboardView {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TextArea decisionInput;
    private TextArea contextInput;
    private TextArea stakeholdersInput;
    private TextArea risksInput;
    private TextArea policiesInput;
    private Label analysisStatusLabel;

    private VBox utilCard;
    private Label utilScoreLabel;
    private Label utilSummaryLabel;

    private VBox justiceCard;
    private Label justiceScoreLabel;
    private Label justiceSummaryLabel;

    private VBox deontCard;
    private Label deontScoreLabel;
    private Label deontSummaryLabel;

    private VBox virtueCard;
    private Label virtueScoreLabel;
    private Label virtueSummaryLabel;

    private Label conflictLabel;
    private Label verdictLabel;
    private Label improvementsLabel;
    private VBox mitigationList;
    private VBox alternativeList;

    private EthicalAnalysisController controller;
    private Decision lastDecision;
    private List<EthicalAnalysisResult> lastResults;

    public DashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new EthicalAnalysisController();
        initializeUI();
    }

    private void initializeUI() {
        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("root-pane");

        rootLayout.setLeft(createSidebar());
        rootLayout.setCenter(createMainContent());

        Scene scene = new Scene(rootLayout, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ethical Decision Engine");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(820);
    }

    private VBox createSidebar() {
        Label logoTitle = new Label("ETHICS HUB");
        logoTitle.getStyleClass().add("sidebar-logo");

        Button dashboardButton = createSidebarButton("Dashboard");
        Button analysisButton = createSidebarButton("New Analysis");
        Button auditButton = createSidebarButton("Audit History");
        Button reportsButton = createSidebarButton("Reports");
        Button settingsButton = createSidebarButton("Settings");

        VBox sidebarBox = new VBox(22, logoTitle, dashboardButton, analysisButton, auditButton, reportsButton, settingsButton);
        sidebarBox.getStyleClass().add("sidebar");
        sidebarBox.setPadding(new Insets(28));
        sidebarBox.setPrefWidth(240);

        return sidebarBox;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");
        return button;
    }

    private ScrollPane createMainContent() {
        VBox contentWrapper = new VBox(24);
        contentWrapper.setPadding(new Insets(28));

        contentWrapper.getChildren().addAll(
                createPageHeader(),
                createBodyContent()
        );

        ScrollPane scrollPane = new ScrollPane(contentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }

    private VBox createPageHeader() {
        Label pageTitle = new Label("Ethical Analysis Platform");
        pageTitle.getStyleClass().add("page-title");

        Label pageSubtitle = new Label("Enterprise-grade ethics oversight with transparent decision intelligence.");
        pageSubtitle.getStyleClass().add("page-subtitle");

        VBox headerText = new VBox(8, pageTitle, pageSubtitle);

        VBox headerBox = new VBox(headerText);
        headerBox.getStyleClass().add("page-header");

        return headerBox;
    }

    private HBox createBodyContent() {
        VBox leftPane = createAnalysisPanel();
        VBox rightPane = createResultsPanel();

        HBox body = new HBox(22, leftPane, rightPane);
        body.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        HBox.setHgrow(leftPane, Priority.SOMETIMES);

        return body;
    }

    private VBox createAnalysisPanel() {
        Label panelTitle = new Label("New Ethical Analysis");
        panelTitle.getStyleClass().add("panel-title");

        decisionInput = new TextArea();
        decisionInput.setPromptText("Describe the decision...");
        decisionInput.getStyleClass().add("input-area");
        decisionInput.setPrefRowCount(3);

        contextInput = new TextArea();
        contextInput.setPromptText("Context and background...");
        contextInput.getStyleClass().add("input-area");
        contextInput.setPrefRowCount(3);

        stakeholdersInput = new TextArea();
        stakeholdersInput.setPromptText("Stakeholders affected by this decision...");
        stakeholdersInput.getStyleClass().add("input-area");
        stakeholdersInput.setPrefRowCount(3);

        risksInput = new TextArea();
        risksInput.setPromptText("Risks, tradeoffs, and likely outcomes...");
        risksInput.getStyleClass().add("input-area");
        risksInput.setPrefRowCount(3);

        policiesInput = new TextArea();
        policiesInput.setPromptText("Applicable rules, policies, laws, or codes...");
        policiesInput.getStyleClass().add("input-area");
        policiesInput.setPrefRowCount(3);

        Button analyzeButton = new Button("Run Ethical Analysis");
        analyzeButton.getStyleClass().add("primary-button");
        analyzeButton.setOnAction(e -> analyzeDecision());

        Button exportButton = new Button("Export Audit Trail");
        exportButton.getStyleClass().add("secondary-button");
        exportButton.setOnAction(e -> exportAuditTrail());

        HBox actionRow = new HBox(12, analyzeButton, exportButton);
        actionRow.getStyleClass().add("action-row");

        analysisStatusLabel = new Label("Enter case details and run the analysis.");
        analysisStatusLabel.getStyleClass().add("status-label");

        VBox analysisBox = new VBox(18,
                panelTitle,
                createFieldGroup("Decision", decisionInput),
                createFieldGroup("Context", contextInput),
                createFieldGroup("Stakeholders", stakeholdersInput),
                createFieldGroup("Risks", risksInput),
                createFieldGroup("Policies", policiesInput),
                actionRow,
                analysisStatusLabel
        );
        analysisBox.getStyleClass().add("panel");
        analysisBox.setPrefWidth(520);

        return analysisBox;
    }

    private VBox createFieldGroup(String labelText, Control field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");

        VBox group = new VBox(10, label, field);
        group.getStyleClass().add("field-group");
        return group;
    }

    private VBox createResultsPanel() {
        utilScoreLabel = new Label("0.0/10");
        utilScoreLabel.getStyleClass().add("score-value");
        utilSummaryLabel = new Label("Awaiting analysis...");
        utilSummaryLabel.setWrapText(true);

        utilCard = createScoreCard("Utilitarian Score", utilScoreLabel, utilSummaryLabel);

        justiceScoreLabel = new Label("0.0/10");
        justiceScoreLabel.getStyleClass().add("score-value");
        justiceSummaryLabel = new Label("Awaiting analysis...");
        justiceSummaryLabel.setWrapText(true);

        justiceCard = createScoreCard("Justice Score", justiceScoreLabel, justiceSummaryLabel);

        deontScoreLabel = new Label("0.0/10");
        deontScoreLabel.getStyleClass().add("score-value");
        deontSummaryLabel = new Label("Awaiting analysis...");
        deontSummaryLabel.setWrapText(true);

        deontCard = createScoreCard("Deontological Score", deontScoreLabel, deontSummaryLabel);

        virtueScoreLabel = new Label("0.0/10");
        virtueScoreLabel.getStyleClass().add("score-value");
        virtueSummaryLabel = new Label("Awaiting analysis...");
        virtueSummaryLabel.setWrapText(true);

        virtueCard = createScoreCard("Virtue Ethics Score", virtueScoreLabel, virtueSummaryLabel);

        HBox scoreGrid = new HBox(16, utilCard, justiceCard, deontCard, virtueCard);
        scoreGrid.getStyleClass().add("score-grid");

        conflictLabel = new Label("No analysis run yet.");
        conflictLabel.getStyleClass().add("details-label");
        VBox conflictBox = createDashboardCard("Conflict Detection", conflictLabel);

        verdictLabel = new Label("Awaiting verdict...");
        verdictLabel.getStyleClass().add("verdict-text");
        VBox verdictBox = createDashboardCard("Final Verdict", verdictLabel);

        improvementsLabel = new Label("Recommended ethical improvements will appear here.");
        improvementsLabel.setWrapText(true);
        improvementsLabel.getStyleClass().add("details-label");

        mitigationList = new VBox(8);
        alternativeList = new VBox(8);

        VBox recommendationCard = createDashboardCard("Recommendations", new VBox(
                createSectionLabel("Required improvements"), improvementsLabel,
                createSectionLabel("Risk mitigation actions"), mitigationList,
                createSectionLabel("Alternative approaches"), alternativeList
        ));

        VBox resultsPane = new VBox(22,
                createSectionHeader("Score Dashboard"),
                scoreGrid,
                conflictBox,
                verdictBox,
                recommendationCard
        );
        resultsPane.setMaxWidth(Double.MAX_VALUE);
        resultsPane.getStyleClass().add("panel");

        return resultsPane;
    }

    private VBox createScoreCard(String title, Label scoreLabel, Label summaryLabel) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        VBox card = new VBox(14, titleLabel, scoreLabel, summaryLabel);
        card.getStyleClass().addAll("score-card", "score-medium");
        card.setPrefWidth(260);
        return card;
    }

    private VBox createDashboardCard(String title, Node content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        VBox card = new VBox(14, titleLabel, content);
        card.getStyleClass().add("dashboard-card");
        return card;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-label");
        return label;
    }

    private Label createSectionHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-header");
        return label;
    }

    private void analyzeDecision() {
        String decisionText = decisionInput.getText().trim();
        String context = contextInput.getText().trim();
        String stakeholders = stakeholdersInput.getText().trim();
        String risks = risksInput.getText().trim();
        String policies = policiesInput.getText().trim();

        if (decisionText.isEmpty()) {
            analysisStatusLabel.setText("Please enter a decision description.");
            return;
        }

        Decision decision = new Decision(decisionText, context, stakeholders, risks, policies);
        lastDecision = decision;
        lastResults = controller.analyzeDecision(decision);

        updateResultDashboard(lastResults);
        analysisStatusLabel.setText("Analysis completed successfully.");
    }

    private void updateResultDashboard(List<EthicalAnalysisResult> results) {
        utilScoreLabel.setText("0.0/10");
        justiceScoreLabel.setText("0.0/10");
        deontScoreLabel.setText("0.0/10");
        virtueScoreLabel.setText("0.0/10");

        utilSummaryLabel.setText("Awaiting analysis...");
        justiceSummaryLabel.setText("Awaiting analysis...");
        deontSummaryLabel.setText("Awaiting analysis...");
        virtueSummaryLabel.setText("Awaiting analysis...");

        for (EthicalAnalysisResult result : results) {
            String name = result.getFrameworkName().toLowerCase();
            if (name.contains("utilitarian")) {
                updateScoreCard(utilCard, utilScoreLabel, utilSummaryLabel, result);
            } else if (name.contains("justice")) {
                updateScoreCard(justiceCard, justiceScoreLabel, justiceSummaryLabel, result);
            } else if (name.contains("deontological")) {
                updateScoreCard(deontCard, deontScoreLabel, deontSummaryLabel, result);
            } else if (name.contains("virtue")) {
                updateScoreCard(virtueCard, virtueScoreLabel, virtueSummaryLabel, result);
            }
        }

        conflictLabel.setText(controller.detectConflicts(results));
        verdictLabel.setText(controller.generateRecommendation(results));
        improvementsLabel.setText(controller.generateRemediation(results));

        updateBulletList(mitigationList, controller.generateRiskMitigationActions(results));
        updateBulletList(alternativeList, controller.generateAlternativeApproaches(results));
    }

    private void updateScoreCard(VBox card, Label scoreLabel, Label summaryLabel, EthicalAnalysisResult result) {
        double score = result.getScore();
        scoreLabel.setText(String.format("%.1f/10", score));
        summaryLabel.setText(result.getExplanation());
        card.getStyleClass().removeAll("score-high", "score-medium", "score-low");
        card.getStyleClass().add(getScoreClass(score));
    }

    private String getScoreClass(double score) {
        if (score >= 8.0) {
            return "score-high";
        } else if (score >= 5.0) {
            return "score-medium";
        }
        return "score-low";
    }

    private void updateBulletList(VBox container, List<String> items) {
        container.getChildren().clear();
        if (items.isEmpty()) {
            container.getChildren().add(new Label("No recommendations available."));
            return;
        }
        for (String item : items) {
            Label bullet = new Label("- " + item);
            bullet.getStyleClass().add("bullet-item");
            bullet.setWrapText(true);
            container.getChildren().add(bullet);
        }
    }

    private void exportAuditTrail() {
        if (lastDecision == null || lastResults == null || lastResults.isEmpty()) {
            analysisStatusLabel.setText("Run an analysis first before exporting an audit trail.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Audit Trail");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("ethical-audit-trail.txt");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) {
            return;
        }

        AuditTrail auditTrail = controller.buildAuditTrail(lastDecision, lastResults);
        try {
            saveReportToFile(auditTrail.toTextReport(), file);
            analysisStatusLabel.setText("Audit trail exported to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            analysisStatusLabel.setText("Failed to save audit trail: " + ex.getMessage());
        }
    }

    private void saveReportToFile(String content, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(content);
        }
    }

    public void show() {
        primaryStage.show();
    }
}
