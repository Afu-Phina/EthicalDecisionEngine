package controller;

import factory.EthicalStrategyFactory;
import model.AuditTrail;
import model.Decision;
import model.EthicalAnalysisResult;
import strategy.EthicalStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * EthicalAnalysisController handles the business logic for ethical decision analysis.
 *
 * Purpose: This class coordinates the analysis of decisions using multiple ethical frameworks,
 * detects conflicts between frameworks, and generates recommendations.
 *
 * OOP Principles Demonstrated:
 * - Encapsulation: Contains the logic for analysis coordination.
 * - Abstraction: Provides a high-level interface for decision analysis.
 *
 * Design Pattern: MVC - Controller layer, coordinating Model and View.
 */
// coordinates the ethical analysis logic and builds recommendations from multiple frameworks.
public class EthicalAnalysisController {

    /**
     * Analyzes a decision using all available ethical frameworks.
     *
     * @param decision The decision to analyze
     * @return List of analysis results from all frameworks
     */
// runs the ethical analysis for each framework on a given decision.
    public List<EthicalAnalysisResult> analyzeDecision(Decision decision) {
        List<EthicalAnalysisResult> results = new ArrayList<>();
        EthicalStrategy[] strategies = EthicalStrategyFactory.getAllStrategies();

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (EthicalStrategy strategy : strategies) {
            EthicalAnalysisResult result = strategy.analyzeDecision(decision);
            results.add(result);
        }

// Return this value to the method caller so the result can be used elsewhere.
        return results;
    }

    /**
     * Calculates the overall ethical score by averaging all framework scores.
     *
     * @param results List of analysis results
     * @return Overall score (0-10)
     */
// computes the average score from all framework results.
    public double calculateOverallScore(List<EthicalAnalysisResult> results) {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (results.isEmpty()) return 0.0;

        double total = 0.0;
// Repeat the code inside this block for each item or while the loop condition remains true.
        for (EthicalAnalysisResult result : results) {
            total += result.getScore();
        }
// Return this value to the method caller so the result can be used elsewhere.
        return total / results.size();
    }

    /**
     * Detects conflicts between ethical frameworks.
     *
     * @param results List of analysis results
     * @return Description of conflicts, or empty string if no conflicts
     */
// finds differences between framework scores and explains any disagreement.
    public String detectConflicts(List<EthicalAnalysisResult> results) {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (results.size() < 2) return "No conflict analysis available.";

        EthicalAnalysisResult highest = results.stream().max((a, b) -> Double.compare(a.getScore(), b.getScore())).orElse(null);
        EthicalAnalysisResult lowest = results.stream().min((a, b) -> Double.compare(a.getScore(), b.getScore())).orElse(null);

// If the condition inside the parentheses is true, the code inside the block will run.
        if (highest == null || lowest == null) {
// Return this value to the method caller so the result can be used elsewhere.
            return "No conflict analysis available.";
        }

        double difference = highest.getScore() - lowest.getScore();

// If the condition inside the parentheses is true, the code inside the block will run.
        if (difference >= 3.0) {
            return String.format("Significant conflict detected between %s (%.1f) and %s (%.1f). " +
                    "This indicates the decision is ethically ambiguous and needs a clearer priority framework.",
                    highest.getFrameworkName(), highest.getScore(), lowest.getFrameworkName(), lowest.getScore());
        } else if (difference >= 1.5) {
            return String.format("Moderate disagreement between %s (%.1f) and %s (%.1f). " +
                    "Review these perspectives before finalizing the decision.",
                    highest.getFrameworkName(), highest.getScore(), lowest.getFrameworkName(), lowest.getScore());
        }

// Return this value to the method caller so the result can be used elsewhere.
        return "Frameworks are largely in agreement.";
    }

    /**
     * Generates ethical recommendations based on the analysis.
     *
     * @param results List of analysis results
     * @return Recommendation string
     */
// returns a high-level recommendation based on the average score.
    public String generateRecommendation(List<EthicalAnalysisResult> results) {
        double overallScore = calculateOverallScore(results);

// If the condition inside the parentheses is true, the code inside the block will run.
        if (overallScore >= 8.0) {
// Return this value to the method caller so the result can be used elsewhere.
            return "Ethically Defensible: The decision aligns strongly with multiple ethical frameworks.";
        } else if (overallScore >= 5.0) {
// Return this value to the method caller so the result can be used elsewhere.
            return "Acceptable with Conditions: The decision has some ethical merit, but should be refined before adoption.";
        } else {
// Return this value to the method caller so the result can be used elsewhere.
            return "Not Recommended: The decision raises serious ethical concerns and needs revision.";
        }
    }

    /**
     * Generates remediation guidance for the decision.
     *
     * @param results List of analysis results
     * @return Remediation guidance string
     */
// suggests steps to improve the decision or resolve conflicts.
    public String generateRemediation(List<EthicalAnalysisResult> results) {
        double overallScore = calculateOverallScore(results);
        String conflictMessage = detectConflicts(results);

// If the condition inside the parentheses is true, the code inside the block will run.
        if (overallScore < 5.0) {
            return "Revisit the decision with special attention to the lowest-scoring frameworks. " +
                   "Strengthen fairness, rights protection, and duty-based rules, and document any policy changes.";
        } else if (conflictMessage.contains("Significant conflict")) {
// Return this value to the method caller so the result can be used elsewhere.
            return "Resolve the conflict by clarifying which ethical priorities will govern this case and adjust the decision accordingly.";
        } else if (conflictMessage.contains("Moderate disagreement")) {
// Return this value to the method caller so the result can be used elsewhere.
            return "Refine the decision to better balance competing ethical values, especially where one framework scores notably lower.";
        }

// Return this value to the method caller so the result can be used elsewhere.
        return "Proceed carefully and continue monitoring stakeholder impacts, with documentation of the reasoning that led to this decision.";
    }

    /**
     * Generates risk mitigation actions based on the analysis.
     *
     * @param results List of analysis results
     * @return A list of risk mitigation actions
     */
// lists actions that reduce potential risks for the decision.
    public List<String> generateRiskMitigationActions(List<EthicalAnalysisResult> results) {
        double overallScore = calculateOverallScore(results);
        List<String> actions = new ArrayList<>();

// If the condition inside the parentheses is true, the code inside the block will run.
        if (overallScore >= 8.0) {
            actions.add("Monitor implementation for unintended impacts.");
            actions.add("Document stakeholder feedback and revisit if conditions change.");
        } else if (overallScore >= 5.0) {
            actions.add("Validate the decision against policies and fairness checks.");
            actions.add("Add procedural safeguards to reduce bias and harm.");
        } else {
            actions.add("Pause the decision and revise it to address the lowest-scoring framework.");
            actions.add("Engage compliance or ethics review before proceeding.");
        }

// Return this value to the method caller so the result can be used elsewhere.
        return actions;
    }

    /**
     * Generates alternative ethical approaches based on the analysis.
     *
     * @param results List of analysis results
     * @return A list of alternative approaches
     */
// lists other ethical approaches to consider for the decision.
    public List<String> generateAlternativeApproaches(List<EthicalAnalysisResult> results) {
        double overallScore = calculateOverallScore(results);
        List<String> approaches = new ArrayList<>();

// If the condition inside the parentheses is true, the code inside the block will run.
        if (overallScore >= 8.0) {
            approaches.add("Proceed while maintaining transparency and documentation.");
            approaches.add("Use the analysis as a benchmark for future decisions.");
        } else if (overallScore >= 5.0) {
            approaches.add("Adjust the implementation to improve fairness and rights protections.");
            approaches.add("Consult with impacted stakeholders before finalizing the decision.");
        } else {
            approaches.add("Explore other options that better preserve rights and equity.");
            approaches.add("Design a new approach with explicit ethical constraints.");
        }

// Return this value to the method caller so the result can be used elsewhere.
        return approaches;
    }

    /**
     * Generates general solution paths based on the analysis.
     *
     * @param results List of analysis results
     * @return A list of practical solution options
     */
// lists practical next steps based on the analysis.
    public List<String> generateSolutionPaths(List<EthicalAnalysisResult> results) {
        double overallScore = calculateOverallScore(results);
        List<String> paths = new ArrayList<>();

        paths.add("Document the ethical tradeoffs and keep the decision traceable for review.");

// If the condition inside the parentheses is true, the code inside the block will run.
        if (overallScore >= 8.0) {
            paths.add("Proceed with the decision while monitoring implementation for any unintended impacts.");
            paths.add("Communicate the ethical rationale clearly to stakeholders.");
        } else if (overallScore >= 5.0) {
            paths.add("Adjust the decision to reduce harm, increase fairness, or better respect duties.");
            paths.add("Engage affected stakeholders or a review board before moving forward.");
        } else {
            paths.add("Explore alternatives that preserve rights, reduce harm, and improve fairness.");
            paths.add("Pause implementation until a more ethically defensible option is defined.");
        }

// Return this value to the method caller so the result can be used elsewhere.
        return paths;
    }

    /**
     * Formats the analysis results for display.
     *
     * @param results List of analysis results
     * @return Formatted string for UI display
     */
// builds a text summary of the analysis results for display.
    public String formatResultsForDisplay(List<EthicalAnalysisResult> results) {
        StringBuilder sb = new StringBuilder();

        sb.append("ETHICAL ANALYSIS RESULTS\n");
        sb.append("========================\n\n");

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (EthicalAnalysisResult result : results) {
            sb.append("Framework: ").append(result.getFrameworkName()).append("\n");
            sb.append("Score: ").append(String.format("%.1f", result.getScore())).append("/10\n");
            sb.append("Explanation: ").append(result.getExplanation()).append("\n");
            sb.append("Risks: ").append(result.getRisks()).append("\n");
            sb.append("Potential Violations: ").append(result.getViolations()).append("\n\n");
        }

        double overallScore = calculateOverallScore(results);
        sb.append("VERDICT CARD: ").append(generateRecommendation(results)).append("\n");
        sb.append("OVERALL SCORE: ").append(String.format("%.1f", overallScore)).append("/10\n\n");

        sb.append("CONFLICT ANALYSIS:\n").append(detectConflicts(results)).append("\n\n");
        sb.append("REMEDIATION:\n").append(generateRemediation(results)).append("\n\n");

        sb.append("SOLUTION PATHS:\n");
// Repeat the code inside this block for each item or while the loop condition remains true.
        for (String path : generateSolutionPaths(results)) {
            sb.append("- ").append(path).append("\n");
        }

// Return this value to the method caller so the result can be used elsewhere.
        return sb.toString();
    }

// creates an audit trail object that contains the full analysis history.
    public AuditTrail buildAuditTrail(Decision decision, List<EthicalAnalysisResult> results) {
        String verdict = generateRecommendation(results);
        String conflictSummary = detectConflicts(results);
        String remediation = generateRemediation(results);
        List<String> solutionPaths = generateSolutionPaths(results);

// Return this value to the method caller so the result can be used elsewhere.
        return new AuditTrail(decision, results, verdict, conflictSummary, remediation, solutionPaths);
    }
}