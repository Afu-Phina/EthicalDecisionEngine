package view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import controller.EthicalAnalysisController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;

public class NewAnalysisPage implements AppPage {
    private final VBox root;
    private final EthicalAnalysisController controller;
    private final Consumer<AuditTrail> auditSink;
    private final Label liveScoreLabel;
    private final Label liveVerdictLabel;
    private final Label liveConflictLabel;
    private final Label statusLabel;
    private TextArea decisionInput;
    private TextArea contextInput;
    private TextArea stakeholdersInput;
    private TextArea risksInput;
    private TextArea policiesInput;

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
    public Node getView() {
        return root;
    }

    @Override
    public String getTitle() {
        return "New Analysis";
    }

    @Override
    public void refresh() {
        updateLiveScore();
    }

    private VBox createHeader() {
        Label title = new Label("New Ethical Analysis");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Capture decision context, assign stakeholders, and validate the ethical profile in real time.");
        subtitle.getStyleClass().add("page-subtitle");

        return new VBox(8, title, subtitle);
    }

    private HBox createFormGrid() {
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
        left.setPrefWidth(520);

        VBox right = new VBox(14,
                createFieldGroup("Risks", risksInput),
                createFieldGroup("Policies", policiesInput),
                createActionRow()
        );
        right.setPrefWidth(520);

        HBox formGrid = new HBox(20, left, right);
        formGrid.setFillHeight(true);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        return formGrid;
    }

    private VBox createFieldGroup(String labelText, TextArea field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
        return new VBox(10, label, field);
    }

    private TextArea createTextArea(String placeholder) {
        TextArea area = new TextArea();
        area.setPromptText(placeholder);
        area.getStyleClass().add("input-area");
        area.setWrapText(true);
        area.setPrefRowCount(4);
        return area;
    }

    private HBox createActionRow() {
        Button analyzeButton = new Button("Analyze Ethics");
        analyzeButton.getStyleClass().add("primary-button");
        analyzeButton.setOnAction(event -> executeAnalysis());

        Button exportButton = new Button("Export Audit Trail");
        exportButton.getStyleClass().add("secondary-button");
        exportButton.setOnAction(event -> exportAuditTrail());

        HBox row = new HBox(14, analyzeButton, exportButton);
        row.getStyleClass().add("action-row");
        return row;
    }

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
        return scoreCard;
    }

    private void configureInputListeners() {
        decisionInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        contextInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        stakeholdersInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        risksInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
        policiesInput.textProperty().addListener((observable, oldValue, newValue) -> updateLiveScore());
    }

    private void updateLiveScore() {
        String description = decisionInput.getText().trim();
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

    private void executeAnalysis() {
        String description = decisionInput.getText().trim();
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

    private void exportAuditTrail() {
        if (decisionInput.getText().trim().isEmpty()) {
            statusLabel.setText("Run an analysis before exporting the audit trail.");
            return;
        }

        Window owner = root.getScene() == null ? null : root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Audit Trail");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("ethical-decision-audit.txt");

        File file = fileChooser.showSaveDialog(owner);
        if (file == null) {
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
        AuditTrail auditTrail = controller.buildAuditTrail(decision, results);

        try {
            saveReportToFile(auditTrail.toTextReport(), file);
            statusLabel.setText("Audit trail exported to " + file.getName() + ".");
        } catch (IOException e) {
            statusLabel.setText("Failed to export audit trail: " + e.getMessage());
        }
    }

    private void saveReportToFile(String content, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
