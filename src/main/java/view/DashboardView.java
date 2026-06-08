package view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import controller.EthicalAnalysisController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;

/**
 * DashboardView class represents the main dashboard of the Ethical Decision Engine.
 */
// contains an alternate dashboard-style interface for running and viewing analysis results.
public class DashboardView {
    private final Stage primaryStage;
// stores the main layout container for the window.
    private BorderPane rootLayout;
// This field stores data that the class uses.
    private TextArea decisionInput;
// This field stores data that the class uses.
    private TextArea contextInput;
// This field stores data that the class uses.
    private TextArea stakeholdersInput;
// This field stores data that the class uses.
    private TextArea risksInput;
// This field stores data that the class uses.
    private TextArea policiesInput;
// This field stores data that the class uses.
    private Label analysisStatusLabel;

// This field stores data that the class uses.
    private VBox utilCard;
// This field stores data that the class uses.
    private Label utilScoreLabel;
// This field stores data that the class uses.
    private Label utilSummaryLabel;

// This field stores data that the class uses.
    private VBox justiceCard;
// This field stores data that the class uses.
    private Label justiceScoreLabel;
// This field stores data that the class uses.
    private Label justiceSummaryLabel;

// This field stores data that the class uses.
    private VBox deontCard;
// This field stores data that the class uses.
    private Label deontScoreLabel;
// This field stores data that the class uses.
    private Label deontSummaryLabel;

// This field stores data that the class uses.
    private VBox virtueCard;
// This field stores data that the class uses.
    private Label virtueScoreLabel;
// This field stores data that the class uses.
    private Label virtueSummaryLabel;

// This field stores data that the class uses.
    private Label conflictLabel;
// This field stores data that the class uses.
    private Label verdictLabel;
// This field stores data that the class uses.
    private Label improvementsLabel;
// This field stores data that the class uses.
    private VBox mitigationList;
// This field stores data that the class uses.
    private VBox alternativeList;

    private final EthicalAnalysisController controller;
// stores the decision object that was analyzed.
    private Decision lastDecision;
// stores the list of ethical framework results for one decision.
    private List<EthicalAnalysisResult> lastResults;

// This method performs one part of the class behavior.
    public DashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new EthicalAnalysisController();
        initializeUI();
    }

// This method performs one part of the class behavior.
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

// This method performs one part of the class behavior.
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

// Return this value to the method caller so the result can be used elsewhere.
        return sidebarBox;
    }

// This method performs one part of the class behavior.
    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");
// Return this value to the method caller so the result can be used elsewhere.
        return button;
    }

// This method performs one part of the class behavior.
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

// Return this value to the method caller so the result can be used elsewhere.
        return scrollPane;
    }

// This method performs one part of the class behavior.
    private VBox createPageHeader() {
        Label pageTitle = new Label("Ethical Analysis Platform");
        pageTitle.getStyleClass().add("page-title");

        Label pageSubtitle = new Label("Enterprise-grade ethics oversight with transparent decision intelligence.");
        pageSubtitle.getStyleClass().add("page-subtitle");

        VBox headerText = new VBox(8, pageTitle, pageSubtitle);

        VBox headerBox = new VBox(headerText);
        headerBox.getStyleClass().add("page-header");

// Return this value to the method caller so the result can be used elsewhere.
        return headerBox;
    }

// This method performs one part of the class behavior.
    private HBox createBodyContent() {
        VBox leftPane = createAnalysisPanel();
        VBox rightPane = createResultsPanel();

        HBox body = new HBox(22, leftPane, rightPane);
        body.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        HBox.setHgrow(leftPane, Priority.SOMETIMES);

// Return this value to the method caller so the result can be used elsewhere.
        return body;
    }

// This method performs one part of the class behavior.
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

// Return this value to the method caller so the result can be used elsewhere.
        return analysisBox;
    }

// This method performs one part of the class behavior.
    private VBox createFieldGroup(String labelText, Control field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");

        VBox group = new VBox(10, label, field);
        group.getStyleClass().add("field-group");
// Return this value to the method caller so the result can be used elsewhere.
        return group;
    }

// This method performs one part of the class behavior.
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

// Return this value to the method caller so the result can be used elsewhere.
        return resultsPane;
    }

// This method performs one part of the class behavior.
    private VBox createScoreCard(String title, Label scoreLabel, Label summaryLabel) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        VBox card = new VBox(14, titleLabel, scoreLabel, summaryLabel);
        card.getStyleClass().addAll("score-card", "score-medium");
        card.setPrefWidth(260);
// Return this value to the method caller so the result can be used elsewhere.
        return card;
    }

// This method performs one part of the class behavior.
    private VBox createDashboardCard(String title, Node content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        VBox card = new VBox(14, titleLabel, content);
        card.getStyleClass().add("dashboard-card");
// Return this value to the method caller so the result can be used elsewhere.
        return card;
    }

// This method performs one part of the class behavior.
    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-label");
// Return this value to the method caller so the result can be used elsewhere.
        return label;
    }

// This method performs one part of the class behavior.
    private Label createSectionHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-header");
// Return this value to the method caller so the result can be used elsewhere.
        return label;
    }

// runs the ethical analysis for each framework on a given decision.
    private void analyzeDecision() {
        String decisionText = decisionInput.getText().trim();
        String context = contextInput.getText().trim();
        String stakeholders = stakeholdersInput.getText().trim();
        String risks = risksInput.getText().trim();
        String policies = policiesInput.getText().trim();

// If the condition inside the parentheses is true, the code inside the block will run.
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

// This method performs one part of the class behavior.
    private void updateResultDashboard(List<EthicalAnalysisResult> results) {
        utilScoreLabel.setText("0.0/10");
        justiceScoreLabel.setText("0.0/10");
        deontScoreLabel.setText("0.0/10");
        virtueScoreLabel.setText("0.0/10");

        utilSummaryLabel.setText("Awaiting analysis...");
        justiceSummaryLabel.setText("Awaiting analysis...");
        deontSummaryLabel.setText("Awaiting analysis...");
        virtueSummaryLabel.setText("Awaiting analysis...");

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (EthicalAnalysisResult result : results) {
            String name = result.getFrameworkName().toLowerCase();
// If the condition inside the parentheses is true, the code inside the block will run.
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

// This method performs one part of the class behavior.
    private void updateScoreCard(VBox card, Label scoreLabel, Label summaryLabel, EthicalAnalysisResult result) {
        double score = result.getScore();
        scoreLabel.setText(String.format("%.1f/10", score));
        summaryLabel.setText(result.getExplanation());
        card.getStyleClass().removeAll("score-high", "score-medium", "score-low");
        card.getStyleClass().add(getScoreClass(score));
    }

// This method performs one part of the class behavior.
    private String getScoreClass(double score) {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (score >= 8.0) {
// Return this value to the method caller so the result can be used elsewhere.
            return "score-high";
        } else if (score >= 5.0) {
// Return this value to the method caller so the result can be used elsewhere.
            return "score-medium";
        }
// Return this value to the method caller so the result can be used elsewhere.
        return "score-low";
    }

// This method performs one part of the class behavior.
    private void updateBulletList(VBox container, List<String> items) {
        container.getChildren().clear();
// If the condition inside the parentheses is true, the code inside the block will run.
        if (items.isEmpty()) {
            container.getChildren().add(new Label("No recommendations available."));
            return;
        }
// Repeat the code inside this block for each item or while the loop condition remains true.
        for (String item : items) {
            Label bullet = new Label("- " + item);
            bullet.getStyleClass().add("bullet-item");
            bullet.setWrapText(true);
            container.getChildren().add(bullet);
        }
    }

// This method performs one part of the class behavior.
    private void exportAuditTrail() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (lastDecision == null || lastResults == null || lastResults.isEmpty()) {
            analysisStatusLabel.setText("Run an analysis first before exporting an audit trail.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Audit Trail");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("ethical-audit-trail.txt");

        File file = fileChooser.showSaveDialog(primaryStage);
// If the condition inside the parentheses is true, the code inside the block will run.
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

// shows the current page or application window.
    public void show() {
        primaryStage.show();
    }
}
