package view;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.AuditTrail;
import report.AuditReportService;

@SuppressWarnings({"deprecation", "unchecked"})
// shows a table of previous analyses and allows exporting audit reports.
public class AuditHistoryPage implements AppPage {
    private final VBox root;
    private final TableView<AuditTrail> historyTable;
    private final ObservableList<AuditTrail> tableData;
    private final FilteredList<AuditTrail> filteredData;
    private final Label overviewLabel;
    private final Supplier<List<AuditTrail>> historySupplier;

// This method performs one part of the class behavior.
    public AuditHistoryPage(Supplier<List<AuditTrail>> historySupplier) {
        this.historySupplier = historySupplier;
        this.root = new VBox(22);
        this.root.setPadding(new Insets(28));
        this.root.getStyleClass().add("page-content");

        Label title = new Label("Audit History");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Review past analyses, search verdicts, and export compliance timelines.");
        subtitle.getStyleClass().add("page-subtitle");

        overviewLabel = new Label("Audit history is empty. Run an analysis to begin tracking results.");
        overviewLabel.getStyleClass().add("details-label");
        overviewLabel.setWrapText(true);

        historyTable = new TableView<>();
        historyTable.getStyleClass().add("history-table");
        tableData = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(tableData, audit -> true);
        historyTable.setItems(filteredData);

        initializeTableColumns();

        TextField searchField = new TextField();
        searchField.setPromptText("Search by decision, verdict, or policy...");
        searchField.getStyleClass().add("input-area");
        searchField.textProperty().addListener((observable, oldText, newText) -> {
            String query = newText == null ? "" : newText.toLowerCase();
            filteredData.setPredicate(audit -> {
// If the condition inside the parentheses is true, the code inside the block will run.
                if (query.isBlank()) {
// Return this value to the method caller so the result can be used elsewhere.
                    return true;
                }
                return audit.getDecision().getDescription().toLowerCase().contains(query)
                        || audit.getVerdict().toLowerCase().contains(query)
                        || audit.getConflictSummary().toLowerCase().contains(query);
            });
        });

        Button exportButton = new Button("Export PDF Reports");
        exportButton.getStyleClass().add("primary-button");
        exportButton.setOnAction(event -> exportHistory());

        HBox actionRow = new HBox(14, searchField, exportButton);
        actionRow.setFillHeight(true);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        root.getChildren().addAll(title, subtitle, overviewLabel, actionRow, historyTable);
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
        return "Audit History";
    }

    @Override
// updates the page contents when the user navigates to it.
    public void refresh() {
        List<AuditTrail> history = historySupplier.get();
        tableData.setAll(history);
        overviewLabel.setText(String.format("%d entries in audit history.", history.size()));
    }

// This method performs one part of the class behavior.
    private void initializeTableColumns() {
        TableColumn<AuditTrail, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTimestamp().toLocalDate().toString()));
        dateColumn.setMinWidth(120);
        dateColumn.setMaxWidth(180);

        TableColumn<AuditTrail, String> decisionColumn = new TableColumn<>("Decision");
        decisionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDecision().getDescription()));
        decisionColumn.setMinWidth(220);
        decisionColumn.setPrefWidth(360);
        decisionColumn.setMaxWidth(Double.MAX_VALUE);

        TableColumn<AuditTrail, String> verdictColumn = new TableColumn<>("Verdict");
        verdictColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVerdict()));
        verdictColumn.setMinWidth(120);
        verdictColumn.setPrefWidth(220);

        TableColumn<AuditTrail, String> scoreColumn = new TableColumn<>("Average Score");
        scoreColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.1f", controllerAverageScore(cell.getValue()))));
        scoreColumn.setMinWidth(100);
        scoreColumn.setPrefWidth(140);

        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        historyTable.getColumns().addAll(dateColumn, decisionColumn, verdictColumn, scoreColumn);
    }

// This method performs one part of the class behavior.
    private double controllerAverageScore(AuditTrail auditTrail) {
// Return this value to the method caller so the result can be used elsewhere.
        return auditTrail.getResults().stream().mapToDouble(result -> result.getScore()).average().orElse(0.0);
    }

// This method performs one part of the class behavior.
    private void exportHistory() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (tableData.isEmpty()) {
            overviewLabel.setText("There is nothing to export yet.");
            return;
        }

        Window owner = root.getScene() == null ? null : root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Ethics Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Ethical_Audit_Report.pdf");

        File file = fileChooser.showSaveDialog(owner);
// If the condition inside the parentheses is true, the code inside the block will run.
        if (file == null) {
            return;
        }

        try {
            AuditTrail latest = tableData.get(0);
            AuditReportService.exportAuditReportToPath(latest, file.getAbsolutePath());
            overviewLabel.setText("Latest audit report exported to " + file.getName() + ".");
        } catch (IOException e) {
            overviewLabel.setText("Unable to export report: " + e.getMessage());
        }
    }
}
