package strategy;

import model.Decision;
import model.EthicalAnalysisResult;

/**
 * VirtueStrategy implements the Virtue ethical framework.
 *
 * Purpose: This class analyzes decisions based on Virtue ethics, which focuses on
 * cultivating good character traits and moral virtues in decision-making.
 *
 * Design Pattern Used: Strategy Pattern
 * Why: Implements the EthicalStrategy interface.
 *
 * OOP Principles Demonstrated:
 * - Polymorphism: Implements analyzeDecision.
 * - Encapsulation: Virtue-specific logic.
 */
public class VirtueStrategy implements EthicalStrategy {

    @Override
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateVirtueScore(decision);
        String explanation = generateVirtueExplanation(decision);
        String risks = identifyVirtueRisks(decision);
        String violations = identifyVirtueViolations(decision);

        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
    public String getFrameworkName() {
        return "Virtue Ethics";
    }

    private double calculateVirtueScore(Decision decision) {
        String description = safeLower(decision.getDescription());
        String context = safeLower(decision.getContext());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

        // Check for virtuous qualities
        if (description.contains("compassion") || description.contains("courage") || description.contains("wisdom")) {
            score += 2.5;
        }
        if (description.contains("selfish") || description.contains("coward") || description.contains("foolish")) {
            score -= 2.5;
        }
        if (context.contains("character") || context.contains("integrity") || context.contains("honor")) {
            score += 1.5;
        }
        if (policies.contains("transparency") || policies.contains("accountability") || policies.contains("honesty")) {
            score += 1.0;
        }
        if (policies.contains("deception") || policies.contains("manipulation")) {
            score -= 1.0;
        }

        return Math.max(0, Math.min(10, score));
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private String generateVirtueExplanation(Decision decision) {
        return "Virtue analysis evaluates the decision based on cultivation of moral character. " +
               "The score reflects alignment with virtues like compassion, courage, and wisdom.";
    }

    private String identifyVirtueRisks(Decision decision) {
        return "Risk of subjective interpretation of virtues leading to inconsistent judgments.";
    }

    private String identifyVirtueViolations(Decision decision) {
        return "Violation if the decision reflects vices rather than virtues.";
    }
}