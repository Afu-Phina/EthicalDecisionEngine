package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AuditTrail captures the inputs and outputs of a single ethical analysis session.
 */
public class AuditTrail {
    private Decision decision;
    private List<EthicalAnalysisResult> results;
    private String verdict;
    private String conflictSummary;
    private String remediation;
    private List<String> solutionPaths;
    private LocalDateTime timestamp;

    public AuditTrail(Decision decision,
                      List<EthicalAnalysisResult> results,
                      String verdict,
                      String conflictSummary,
                      String remediation,
                      List<String> solutionPaths) {
        this.decision = decision;
        this.results = results;
        this.verdict = verdict;
        this.conflictSummary = conflictSummary;
        this.remediation = remediation;
        this.solutionPaths = solutionPaths;
        this.timestamp = LocalDateTime.now();
    }

    public String toTextReport() {
        StringBuilder sb = new StringBuilder();
        String timeLabel = timestamp.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));

        sb.append("ETHICAL DECISION ENGINE AUDIT TRAIL\n");
        sb.append("Generated: ").append(timeLabel).append("\n\n");

        sb.append("Decision Description:\n");
        sb.append(decision.getDescription()).append("\n\n");

        sb.append("Context:\n");
        sb.append(decision.getContext()).append("\n\n");

        sb.append("Stakeholders:\n");
        sb.append(decision.getStakeholders()).append("\n\n");

        sb.append("Risks/Outcomes:\n");
        sb.append(decision.getRisks()).append("\n\n");

        sb.append("Applicable Policies:\n");
        sb.append(decision.getApplicablePolicies()).append("\n\n");

        sb.append("FRAMEWORK SCORES:\n");
        for (EthicalAnalysisResult result : results) {
            sb.append(String.format("- %s: %.1f/10\n", result.getFrameworkName(), result.getScore()));
            sb.append("  Explanation: ").append(result.getExplanation()).append("\n");
            sb.append("  Risks: ").append(result.getRisks()).append("\n");
            sb.append("  Violations: ").append(result.getViolations()).append("\n\n");
        }

        sb.append("VERDICT:\n");
        sb.append(verdict).append("\n\n");

        sb.append("CONFLICT ANALYSIS:\n");
        sb.append(conflictSummary).append("\n\n");

        sb.append("REMEDIATION:\n");
        sb.append(remediation).append("\n\n");

        sb.append("SOLUTION PATHS:\n");
        for (String path : solutionPaths) {
            sb.append("- ").append(path).append("\n");
        }

        return sb.toString();
    }
}
