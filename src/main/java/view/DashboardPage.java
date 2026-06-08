package view;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import controller.EthicalAnalysisController;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.AuditTrail;

// shows summary statistics, recent analyses, and high level insights to the user.
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

// This method performs one part of the class behavior.
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
// This method performs one part of the class behavior.
    public Node getView() {
// Return this value to the method caller so the result can be used elsewhere.
        return root;
    }

    @Override
// This method performs one part of the class behavior.
    public String getTitle() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Dashboard";
    }

    @Override
// updates the page contents when the user navigates to it.
    public void refresh() {
        List<AuditTrail> history = auditHistorySupplier.get();
        totalAnalysesLabel.setText(String.valueOf(history.size()));

// If the condition inside the parentheses is true, the code inside the block will run.
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

// This method performs one part of the class behavior.
    private VBox createPageHeader() {
        Label welcome = new Label("Welcome to the Ethics Intelligence Platform");
        welcome.getStyleClass().add("hero-welcome");

        Label title = new Label("Ethics Intelligence Dashboard");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Monitor enterprise ethics performance, emerging conflicts, and decision momentum.");
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(10, welcome, title, subtitle);
// Return this value to the method caller so the result can be used elsewhere.
        return header;
    }

// This method performs one part of the class behavior.
    private FlowPane createOverviewGrid() {
        VBox totalCard = createStatCard("Completed Analyses", totalAnalysesLabel, new Label("Live operational scorecard for every reviewed decision."));
        VBox scoreCard = createStatCard("Average Ethical Score", averageScoreLabel, new Label("Performance across all frameworks."));
        VBox conflictCard = createStatCard("Latest Conflict Alert", conflictSummaryLabel, new Label("Most recent conflict detected in your analyses."));
        VBox verdictCard = createStatCard("Latest Verdict", verdictSummaryLabel, new Label("Ethical framework consensus from latest review."));

        FlowPane row = new FlowPane(Orientation.HORIZONTAL, 18, 18, totalCard, scoreCard, conflictCard, verdictCard);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPrefWrapLength(1200);
        row.setMaxWidth(Double.MAX_VALUE);
        totalCard.setMaxWidth(360);
        scoreCard.setMaxWidth(360);
        conflictCard.setMaxWidth(360);
        verdictCard.setMaxWidth(360);
// Return this value to the method caller so the result can be used elsewhere.
        return row;
    }

// This method performs one part of the class behavior.
    private VBox createStatCard(String labelText, Label valueLabel, Label descriptionLabel) {
        Label title = new Label(labelText);
        title.getStyleClass().add("card-title");

        VBox card = new VBox(10, title, valueLabel, descriptionLabel);
        card.getStyleClass().add("dashboard-card");
        addCardMotion(card, 6, 6.8);
        card.setOnMouseEntered(event -> {
            card.setScaleX(1.01);
            card.setScaleY(1.01);
        });
        card.setOnMouseExited(event -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });
// Return this value to the method caller so the result can be used elsewhere.
        return card;
    }

// This method performs one part of the class behavior.
    private void addCardMotion(Node card, double distance, double durationSeconds) {
        TranslateTransition floatTransition = new TranslateTransition(Duration.seconds(durationSeconds), card);
        floatTransition.setByY(distance);
        floatTransition.setAutoReverse(true);
        floatTransition.setCycleCount(Animation.INDEFINITE);
        floatTransition.setInterpolator(Interpolator.EASE_BOTH);
        floatTransition.play();
    }

// This method performs one part of the class behavior.
    private FlowPane createInsightsRow() {
        VBox recentBox = new VBox(14,
                createSectionHeader("Recent Analyses"),
                recentAnalysisSection
        );
        recentBox.getStyleClass().add("panel");
        recentBox.setMaxWidth(520);
        recentBox.setMinWidth(320);

        VBox timelineBox = new VBox(14,
                createSectionHeader("Activity Timeline"),
                timelineList
        );
        timelineBox.getStyleClass().add("panel");
        timelineList.setPrefHeight(260);
        timelineBox.setMaxWidth(360);
        timelineBox.setMinWidth(280);

        VBox chartBox = new VBox(14,
                createSectionHeader("Verdict Distribution"),
                verdictPie
        );
        chartBox.getStyleClass().add("chart-card");
        chartBox.setMaxWidth(360);
        chartBox.setMinWidth(280);

        FlowPane row = new FlowPane(Orientation.HORIZONTAL, 18, 18, recentBox, timelineBox, chartBox);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPrefWrapLength(1200);
        row.setMaxWidth(Double.MAX_VALUE);
// Return this value to the method caller so the result can be used elsewhere.
        return row;
    }

// This method performs one part of the class behavior.
    private String formatTimelineEntry(AuditTrail trail) {
// Return this value to the method caller so the result can be used elsewhere.
        return String.format("%s — %s — %s", trail.getTimestamp().toLocalDate(), trail.getVerdict(), trail.getDecision().getDescription());
    }

// This method performs one part of the class behavior.
    private String formatRecentAnalysis(AuditTrail trail) {
// Return this value to the method caller so the result can be used elsewhere.
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

// This method performs one part of the class behavior.
    private VBox createRecentCard(String text) {
        Label copy = new Label(text);
        copy.getStyleClass().add("details-label");
        copy.setWrapText(true);
        VBox card = new VBox(copy);
        card.getStyleClass().add("dashboard-card");
// Return this value to the method caller so the result can be used elsewhere.
        return card;
    }

// This method performs one part of the class behavior.
    private VBox createEmptyDetail(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("details-label");
// Return this value to the method caller so the result can be used elsewhere.
        return new VBox(label);
    }

// This method performs one part of the class behavior.
    private Label createSectionHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-header");
// Return this value to the method caller so the result can be used elsewhere.
        return label;
    }
}
