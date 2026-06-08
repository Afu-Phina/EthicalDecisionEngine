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
// implements the virtue ethical framework, focused on moral character.
public class VirtueStrategy implements EthicalStrategy {

    @Override
// runs the ethical analysis for each framework on a given decision.
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateVirtueScore(decision);
        String explanation = generateVirtueExplanation();
        String risks = identifyVirtueRisks();
        String violations = identifyVirtueViolations();

// Return this value to the method caller so the result can be used elsewhere.
        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
// This method performs one part of the class behavior.
    public String getFrameworkName() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Virtue Ethics";
    }

// This method performs one part of the class behavior.
    private double calculateVirtueScore(Decision decision) {
        String description = safeLower(decision.getDescription());
        String context = safeLower(decision.getContext());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

        // Check for virtuous qualities
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.contains("compassion") || description.contains("courage") || description.contains("wisdom")) {
            score += 2.5;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.contains("selfish") || description.contains("coward") || description.contains("foolish")) {
            score -= 2.5;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (context.contains("character") || context.contains("integrity") || context.contains("honor")) {
            score += 1.5;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("transparency") || policies.contains("accountability") || policies.contains("honesty")) {
            score += 1.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("deception") || policies.contains("manipulation")) {
            score -= 1.0;
        }

// Return this value to the method caller so the result can be used elsewhere.
        return Math.max(0, Math.min(10, score));
    }

// This method performs one part of the class behavior.
    private String safeLower(String value) {
// Return this value to the method caller so the result can be used elsewhere.
        return value == null ? "" : value.toLowerCase();
    }

// This method performs one part of the class behavior.
    private String generateVirtueExplanation() {
        return "Virtue analysis evaluates the decision based on cultivation of moral character. " +
               "The score reflects alignment with virtues like compassion, courage, and wisdom.";
    }

// This method performs one part of the class behavior.
    private String identifyVirtueRisks() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Risk of subjective interpretation of virtues leading to inconsistent judgments.";
    }

// This method performs one part of the class behavior.
    private String identifyVirtueViolations() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Violation if the decision reflects vices rather than virtues.";
    }
}