package view;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import controller.EthicalAnalysisController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;
import report.AuditReportService;

// provides the form for entering a new decision and running a fresh analysis.
public class NewAnalysisPage implements AppPage {
    private final VBox root;
    private final EthicalAnalysisController controller;
    private final Consumer<AuditTrail> auditSink;
    private final AppNavigator navigator;
    private final Label liveScoreLabel;
    private final Label liveVerdictLabel;
    private final Label liveConflictLabel;
    private final Label statusLabel;
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

// This method performs one part of the class behavior.
    public NewAnalysisPage(EthicalAnalysisController controller, Consumer<AuditTrail> auditSink, AppNavigator navigator) {
        this.controller = controller;
        this.auditSink = auditSink;
        this.root = new VBox(22);
        this.root.setPadding(new Insets(28));
        this.root.getStyleClass().add("page-content");

        liveScoreLabel = new Label("0.0/10");
        liveScoreLabel.getStyleClass().add("live-score-value");

        liveVerdictLabel = new Label("Live ethical scoring will appear here.");
        liveVerdictLabel.getStyleClass().add("details-label");
        liveVerdictLabel.setWrapText(true);

        liveConflictLabel = new Label("Conflict insights appear as you refine the decision.");
        liveConflictLabel.getStyleClass().add("details-label");
        liveConflictLabel.setWrapText(true);

        statusLabel = new Label("Provide decision details and generate a new analysis.");
        statusLabel.getStyleClass().add("status-label");

        root.getChildren().addAll(
                createHeader(),
                createFormGrid(),
                createLiveScoringCard(),
                statusLabel
        );

        // Attach live feedback listeners
        configureInputListeners();
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
        return "New Analysis";
    }

    @Override
// updates the page contents when the user navigates to it.
    public void refresh() {
        updateLiveScore();
    }

// This method performs one part of the class behavior.
    private VBox createHeader() {
        Label title = new Label("New Ethical Analysis");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Capture decision context, assign stakeholders, and validate the ethical profile in real time.");
        subtitle.getStyleClass().add("page-subtitle");

// Return this value to the method caller so the result can be used elsewhere.
        return new VBox(8, title, subtitle);
    }

// This method performs one part of the class behavior.
    private GridPane createFormGrid() {
        decisionInput = createTextArea("Decision summary and required action...");
        contextInput = createTextArea("Context, environment, and background details...");
        stakeholdersInput = createTextArea("Stakeholders, beneficiaries, and affected parties...");
        risksInput = createTextArea("Material risks, tradeoffs, and edge conditions...");
        policiesInput = createTextArea("Applicable policies, codes, laws, and governance...");

        VBox left = new VBox(14,
                createFieldGroup("Decision", decisionInput),
                createFieldGroup("Context", contextInput),
                createFieldGroup("Stakeholders", stakeholdersInput)
        );
        VBox right = new VBox(14,
                createFieldGroup("Risks", risksInput),
                createFieldGroup("Policies", policiesInput),
                createActionRow()
        );

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.add(left, 0, 0);
        formGrid.add(right, 1, 0);
        formGrid.setPrefWidth(Double.MAX_VALUE);

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);
        leftColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);
        rightColumn.setHgrow(Priority.ALWAYS);
        formGrid.getColumnConstraints().addAll(leftColumn, rightColumn);

        GridPane.setHgrow(left, Priority.ALWAYS);
        GridPane.setHgrow(right, Priority.ALWAYS);
        left.setMaxWidth(Double.MAX_VALUE);
        right.setMaxWidth(Double.MAX_VALUE);
// Return this value to the method caller so the result can be used elsewhere.
        return formGrid;
    }

// This method performs one part of the class behavior.
    private VBox createFieldGroup(String labelText, TextArea field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
// Return this value to the method caller so the result can be used elsewhere.
        return new VBox(10, label, field);
    }

// This method performs one part of the class behavior.
    private TextArea createTextArea(String placeholder) {
        TextArea area = new TextArea();
        area.setPromptText(placeholder);
        area.getStyleClass().add("input-area");
        area.setWrapText(true);
        area.setPrefRowCount(4);
// Return this value to the method caller so the result can be used elsewhere.
        return area;
    }

// This method performs one part of the class behavior.
    private HBox createActionRow() {
        Button analyzeButton = new Button("Analyze Ethics");
        analyzeButton.getStyleClass().add("primary-button");
        analyzeButton.setOnAction(event -> executeAnalysis());

        Button exportButton = new Button("Export PDF Report");
        exportButton.getStyleClass().add("secondary-button");
        exportButton.setOnAction(event -> exportAuditTrail());

        HBox row = new HBox(14, analyzeButton, exportButton);
        row.getStyleClass().add("action-row");
// Return this value to the method caller so the result can be used elsewhere.
        return row;
    }

// This method performs one part of the class behavior.
    private VBox createLiveScoringCard() {
        Label title = new Label("Live Ethical Score");
        title.getStyleClass().add("card-title");

        VBox scoreCard = new VBox(16,
                title,
                liveScoreLabel,
                new Label("Verdict:"),
                liveVerdictLabel,
                new Label("Conflict Guidance:"),
                liveConflictLabel
        );
        scoreCard.getStyleClass().add("dashboard-card");
// Return this value to the method caller so the result can be used elsewhere.
        return scoreCard;
    }

// This method performs one part of the class behavior.
    private void configureInputListeners() {
        decisionInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        contextInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        stakeholdersInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        risksInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        policiesInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
    }

// This method performs one part of the class behavior.
    private void updateLiveScore() {
        String description = decisionInput.getText().trim();
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.isEmpty()) {
            liveScoreLabel.setText("0.0/10");
            liveVerdictLabel.setText("Enter a decision summary to see live scoring.");
            liveConflictLabel.setText("Conflict details will refresh as you enter more context.");
            return;
        }

        Decision decision = new Decision(
                description,
                contextInput.getText().trim(),
                stakeholdersInput.getText().trim(),
                risksInput.getText().trim(),
                policiesInput.getText().trim()
        );

        List<EthicalAnalysisResult> results = controller.analyzeDecision(decision);
        double average = controller.calculateOverallScore(results);

        liveScoreLabel.setText(String.format("%.1f/10", average));
        liveVerdictLabel.setText(controller.generateRecommendation(results));
        liveConflictLabel.setText(controller.detectConflicts(results));
    }

// This method performs one part of the class behavior.
    private void executeAnalysis() {
        String description = decisionInput.getText().trim();
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.isEmpty()) {
            statusLabel.setText("Please include a decision summary before analyzing.");
            return;
        }

        Decision decision = new Decision(
                description,
                contextInput.getText().trim(),
                stakeholdersInput.getText().trim(),
                risksInput.getText().trim(),
                policiesInput.getText().trim()
        );
        List<EthicalAnalysisResult> results = controller.analyzeDecision(decision);
        AuditTrail auditTrail = controller.buildAuditTrail(decision, results);
        auditSink.accept(auditTrail);
        statusLabel.setText("Analysis saved to history and available in the dashboard.");
        updateLiveScore();
    }

// This method performs one part of the class behavior.
    @SuppressWarnings("unused")
    private void exportAuditTrail() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (decisionInput.getText().trim().isEmpty()) {
            statusLabel.setText("Run an analysis before exporting the PDF report.");
            return;
        }

        Decision decision = new Decision(
                decisionInput.getText().trim(),
                contextInput.getText().trim(),
                stakeholdersInput.getText().trim(),
                risksInput.getText().trim(),
                policiesInput.getText().trim()
        );
        List<EthicalAnalysisResult> results = controller.analyzeDecision(decision);
        AuditTrail reportTrail = controller.buildAuditTrail(decision, results);

        Window owner = root.getScene() == null ? null : root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Ethics Audit Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Ethical_Decision_Report.pdf");

        File file = fileChooser.showSaveDialog(owner);
// If the condition inside the parentheses is true, the code inside the block will run.
        if (file == null) {
            return;
        }

        try {
            AuditReportService.exportAuditReportToPath(reportTrail, file.getAbsolutePath());
            statusLabel.setText("PDF report exported to " + file.getName() + ".");
        } catch (IOException e) {
            statusLabel.setText("Failed to export PDF report: " + e.getMessage());
        }
    }

}
