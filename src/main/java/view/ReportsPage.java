package view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.AuditTrail;
import model.EthicalAnalysisResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ReportsPage implements AppPage {
    private final VBox root;
    private final BarChart<String, Number> frameworkChart;
    private final LineChart<Number, Number> trendChart;
    private final PieChart verdictChart;
    private final Label insightLabel;
    private final Supplier<List<AuditTrail>> historySupplier;

    public ReportsPage(Supplier<List<AuditTrail>> historySupplier) {
        this.historySupplier = historySupplier;
        this.root = new VBox(22);
        this.root.setPadding(new Insets(28));
        this.root.getStyleClass().add("page-content");

        Label title = new Label("Ethics Reports & Analytics");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Compare frameworks, monitor score trends, and download executive summaries.");
        subtitle.getStyleClass().add("page-subtitle");

        insightLabel = new Label("Reports refresh automatically when new audits are added.");
        insightLabel.getStyleClass().add("details-label");
        insightLabel.setWrapText(true);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Framework");
        yAxis.setLabel("Average Score");
        frameworkChart = new BarChart<>(xAxis, yAxis);
        frameworkChart.setTitle("Average Ethical Scores by Framework");
        frameworkChart.setLegendVisible(false);

        NumberAxis lineXAxis = new NumberAxis();
        NumberAxis lineYAxis = new NumberAxis();
        lineXAxis.setLabel("Analysis Index");
        lineYAxis.setLabel("Average Score");
        trendChart = new LineChart<>(lineXAxis, lineYAxis);
        trendChart.setTitle("Score Trend Over Time");
        trendChart.setLegendVisible(false);

        verdictChart = new PieChart();
        verdictChart.setTitle("Verdict Distribution");
        verdictChart.setLegendVisible(true);

        Button exportButton = new Button("Export Summary Report");
        exportButton.getStyleClass().add("primary-button");
        exportButton.setOnAction(event -> exportReport());

        HBox chartsRow = new HBox(18, frameworkChart, trendChart);
        chartsRow.setFillHeight(true);
        HBox.setHgrow(frameworkChart, Priority.ALWAYS);
        HBox.setHgrow(trendChart, Priority.ALWAYS);

        root.getChildren().addAll(title, subtitle, insightLabel, chartsRow, verdictChart, exportButton);

        refresh();
    }

    @Override
    public Node getView() {
        return root;
    }

    @Override
    public String getTitle() {
        return "Reports";
    }

    @Override
    public void refresh() {
        List<AuditTrail> history = historySupplier.get();
        if (history.isEmpty()) {
            insightLabel.setText("No audit history yet. Run an analysis to populate analytics.");
            frameworkChart.getData().clear();
            trendChart.getData().clear();
            verdictChart.getData().clear();
            return;
        }

        insightLabel.setText(String.format("Analyzing %d completed reviews for trend and framework performance.", history.size()));
        updateFrameworkChart(history);
        updateTrendChart(history);
        updateVerdictChart(history);
    }

    private void updateFrameworkChart(List<AuditTrail> history) {
        frameworkChart.getData().clear();
        Map<String, Double> averageByFramework = history.stream()
                .flatMap(audit -> audit.getResults().stream())
                .collect(Collectors.groupingBy(EthicalAnalysisResult::getFrameworkName,
                        Collectors.averagingDouble(EthicalAnalysisResult::getScore)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        averageByFramework.forEach((framework, score) ->
                series.getData().add(new XYChart.Data<>(framework, score)));
        frameworkChart.getData().add(series);
    }

    private void updateTrendChart(List<AuditTrail> history) {
        trendChart.getData().clear();
        XYChart.Series<Number, Number> scoreSeries = new XYChart.Series<>();
        for (int index = history.size() - 1; index >= 0; index--) {
            AuditTrail audit = history.get(index);
            double average = audit.getResults().stream().mapToDouble(EthicalAnalysisResult::getScore).average().orElse(0.0);
            scoreSeries.getData().add(new XYChart.Data<>(history.size() - index, average));
        }
        trendChart.getData().add(scoreSeries);
    }

    private void updateVerdictChart(List<AuditTrail> history) {
        verdictChart.getData().clear();
        Map<String, Long> counts = history.stream()
                .collect(Collectors.groupingBy(AuditTrail::getVerdict, Collectors.counting()));

        counts.forEach((verdict, count) ->
                verdictChart.getData().add(new PieChart.Data(verdict, count)));
    }

    private void exportReport() {
        Window owner = root.getScene() == null ? null : root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Analytics Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("ethical-report-summary.txt");

        File file = fileChooser.showSaveDialog(owner);
        if (file == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(buildReportSummary(historySupplier.get()));
            insightLabel.setText("Report exported to " + file.getName() + ".");
        } catch (IOException e) {
            insightLabel.setText("Unable to export report: " + e.getMessage());
        }
    }

    private String buildReportSummary(List<AuditTrail> history) {
        StringBuilder builder = new StringBuilder();
        builder.append("ETHICS ANALYTICS SUMMARY\n");
        builder.append("Total analyses: ").append(history.size()).append("\n\n");

        Map<String, Double> averages = history.stream()
                .flatMap(audit -> audit.getResults().stream())
                .collect(Collectors.groupingBy(EthicalAnalysisResult::getFrameworkName,
                        Collectors.averagingDouble(EthicalAnalysisResult::getScore)));

        averages.forEach((framework, average) -> builder.append(framework)
                .append(": ")
                .append(String.format("%.1f", average))
                .append("\n"));

        builder.append("\nTrend details:\n");
        for (int i = 0; i < history.size(); i++) {
            AuditTrail audit = history.get(i);
            builder.append(String.format("%d: %s — %.1f\n", i + 1,
                    audit.getTimestamp().toLocalDate(),
                    audit.getResults().stream().mapToDouble(EthicalAnalysisResult::getScore).average().orElse(0.0)));
        }
        return builder.toString();
    }
}
