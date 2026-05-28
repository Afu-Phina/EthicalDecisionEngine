package view;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import controller.EthicalAnalysisController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.AuditTrail;

public class DashboardPage implements AppPage {
    private final VBox root;
    private final Label totalAnalysesLabel;
    private final Label averageScoreLabel;
    private final Label conflictSummaryLabel;
    private final Label verdictSummaryLabel;
    private final ListView<String> timelineList;
    private final VBox recentAnalysisSection;
    private final PieChart verdictPie;
    private final Supplier<List<AuditTrail>> auditHistorySupplier;
    private final EthicalAnalysisController controller;

    public DashboardPage(EthicalAnalysisController controller, Supplier<List<AuditTrail>> auditHistorySupplier) {
        this.controller = controller;
        this.auditHistorySupplier = auditHistorySupplier;
        this.root = new VBox(24);
        this.root.setPadding(new Insets(28));
        this.root.getStyleClass().add("page-content");

        totalAnalysesLabel = new Label("0");
        totalAnalysesLabel.getStyleClass().add("stat-value");

        averageScoreLabel = new Label("0.0");
        averageScoreLabel.getStyleClass().add("stat-value");

        conflictSummaryLabel = new Label("No conflict details yet.");
        conflictSummaryLabel.getStyleClass().add("details-label");
        conflictSummaryLabel.setWrapText(true);

        verdictSummaryLabel = new Label("No verdict available.");
        verdictSummaryLabel.getStyleClass().add("details-label");
        verdictSummaryLabel.setWrapText(true);

        timelineList = new ListView<>();
        timelineList.getStyleClass().add("activity-list");

        recentAnalysisSection = new VBox(10);
        recentAnalysisSection.getStyleClass().add("recent-analysis-section");

        verdictPie = new PieChart();
        verdictPie.getStyleClass().add("summary-pie");
        verdictPie.setLegendVisible(false);
        verdictPie.setLabelsVisible(true);

        root.getChildren().addAll(
                createPageHeader(),
                createOverviewGrid(),
                createInsightsRow()
        );
    }

    @Override
    public Node getView() {
        return root;
    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }

    @Override
    public void refresh() {
        List<AuditTrail> history = auditHistorySupplier.get();
        totalAnalysesLabel.setText(String.valueOf(history.size()));

        if (history.isEmpty()) {
            averageScoreLabel.setText("0.0");
            conflictSummaryLabel.setText("Run a new analysis to populate conflict insights.");
            verdictSummaryLabel.setText("Your ethical score breakdown will appear here.");
            timelineList.getItems().setAll("No activity yet. Start a new analysis.");
            recentAnalysisSection.getChildren().setAll(createEmptyDetail("No recent analyses yet."));
            verdictPie.setData(buildEmptyPieData());
            return;
        }

        double overallAverage = history.stream()
                .mapToDouble(trail -> controller.calculateOverallScore(trail.getResults()))
                .average()
                .orElse(0.0);

        averageScoreLabel.setText(String.format("%.1f", overallAverage));

        AuditTrail latest = history.get(0);
        conflictSummaryLabel.setText(latest.getConflictSummary());
        verdictSummaryLabel.setText(latest.getVerdict());

        List<String> recentItems = history.stream()
                .limit(4)
                .map(this::formatRecentAnalysis)
                .collect(Collectors.toList());
        recentAnalysisSection.getChildren().setAll(recentItems.stream().map(this::createRecentCard).collect(Collectors.toList()));

        timelineList.getItems().setAll(history.stream()
                .limit(6)
                .map(this::formatTimelineEntry)
                .collect(Collectors.toList()));

        verdictPie.setData(buildVerdictDistribution(history));
    }

    private VBox createPageHeader() {
        Label welcome = new Label("Welcome to the Ethics Intelligence Platform");
        welcome.getStyleClass().add("hero-welcome");

        Label title = new Label("Ethics Intelligence Dashboard");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Monitor enterprise ethics performance, emerging conflicts, and decision momentum.");
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(10, welcome, title, subtitle);
        return header;
    }

    private HBox createOverviewGrid() {
        VBox totalCard = createStatCard("Completed Analyses", totalAnalysesLabel, new Label("Live operational scorecard for every reviewed decision."));
        VBox scoreCard = createStatCard("Average Ethical Score", averageScoreLabel, new Label("Performance across all frameworks."));
        VBox conflictCard = createStatCard("Latest Conflict Alert", conflictSummaryLabel, new Label("Most recent conflict detected in your analyses."));
        VBox verdictCard = createStatCard("Latest Verdict", verdictSummaryLabel, new Label("Ethical framework consensus from latest review."));

        HBox row = new HBox(18, totalCard, scoreCard, conflictCard, verdictCard);
        row.setFillHeight(true);
        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(scoreCard, Priority.ALWAYS);
        HBox.setHgrow(conflictCard, Priority.ALWAYS);
        HBox.setHgrow(verdictCard, Priority.ALWAYS);
        return row;
    }

    private VBox createStatCard(String labelText, Label valueLabel, Label descriptionLabel) {
        Label title = new Label(labelText);
        title.getStyleClass().add("card-title");

        VBox card = new VBox(10, title, valueLabel, descriptionLabel);
        card.getStyleClass().add("dashboard-card");
        return card;
    }

    private HBox createInsightsRow() {
        VBox recentBox = new VBox(14,
                createSectionHeader("Recent Analyses"),
                recentAnalysisSection
        );
        recentBox.getStyleClass().add("panel");

        VBox timelineBox = new VBox(14,
                createSectionHeader("Activity Timeline"),
                timelineList
        );
        timelineBox.getStyleClass().add("panel");
        timelineList.setPrefHeight(280);

        VBox chartBox = new VBox(14,
                createSectionHeader("Verdict Distribution"),
                verdictPie
        );
        chartBox.getStyleClass().add("chart-card");

        HBox row = new HBox(18, recentBox, timelineBox, chartBox);
        row.setFillHeight(true);
        HBox.setHgrow(recentBox, Priority.ALWAYS);
        HBox.setHgrow(timelineBox, Priority.ALWAYS);
        HBox.setHgrow(chartBox, Priority.ALWAYS);
        return row;
    }

    private String formatTimelineEntry(AuditTrail trail) {
        return String.format("%s — %s — %s", trail.getTimestamp().toLocalDate(), trail.getVerdict(), trail.getDecision().getDescription());
    }

    private String formatRecentAnalysis(AuditTrail trail) {
        return String.format("%s | %.1f average | %s", trail.getTimestamp().toLocalDate(), controller.calculateOverallScore(trail.getResults()), trail.getVerdict());
    }

    private javafx.collections.ObservableList<PieChart.Data> buildEmptyPieData() {
        return javafx.collections.FXCollections.observableArrayList(
                new PieChart.Data("No Data", 1)
        );
    }

    private javafx.collections.ObservableList<PieChart.Data> buildVerdictDistribution(List<AuditTrail> history) {
        Map<String, Long> verdictCounts = history.stream()
                .collect(Collectors.groupingBy(AuditTrail::getVerdict, Collectors.counting()));

        return javafx.collections.FXCollections.observableArrayList(
                verdictCounts.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );
    }

    private VBox createRecentCard(String text) {
        Label copy = new Label(text);
        copy.getStyleClass().add("details-label");
        copy.setWrapText(true);
        VBox card = new VBox(copy);
        card.getStyleClass().add("dashboard-card");
        return card;
    }

    private VBox createEmptyDetail(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("details-label");
        return new VBox(label);
    }

    private Label createSectionHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-header");
        return label;
    }
}
