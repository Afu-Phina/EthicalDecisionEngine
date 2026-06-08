package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AuditTrail captures the inputs and outputs of a single ethical analysis session.
 */
// stores the complete history of one completed ethical analysis session.
public class AuditTrail {
    private final Decision decision;
    private final List<EthicalAnalysisResult> results;
    private final String verdict;
    private final String conflictSummary;
    private final String remediation;
    private final List<String> solutionPaths;
    private final LocalDateTime timestamp;

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

// This method performs one part of the class behavior.
    public Decision getDecision() {
// Return this value to the method caller so the result can be used elsewhere.
        return decision;
    }

// This method performs one part of the class behavior.
    public List<EthicalAnalysisResult> getResults() {
// Return this value to the method caller so the result can be used elsewhere.
        return results;
    }

// This method performs one part of the class behavior.
    public String getVerdict() {
// Return this value to the method caller so the result can be used elsewhere.
        return verdict;
    }

// This method performs one part of the class behavior.
    public String getConflictSummary() {
// Return this value to the method caller so the result can be used elsewhere.
        return conflictSummary;
    }

// This method performs one part of the class behavior.
    public String getRemediation() {
// Return this value to the method caller so the result can be used elsewhere.
        return remediation;
    }

// This method performs one part of the class behavior.
    public List<String> getSolutionPaths() {
// Return this value to the method caller so the result can be used elsewhere.
        return solutionPaths;
    }

// This method performs one part of the class behavior.
    public LocalDateTime getTimestamp() {
// Return this value to the method caller so the result can be used elsewhere.
        return timestamp;
    }

// builds a plain text report from the audit trail data.
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
// Repeat the code inside this block for each item or while the loop condition remains true.
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
// Repeat the code inside this block for each item or while the loop condition remains true.
        for (String path : solutionPaths) {
            sb.append("- ").append(path).append("\n");
        }

// Return this value to the method caller so the result can be used elsewhere.
        return sb.toString();
    }
}
