package strategy;

import model.Decision;
import model.EthicalAnalysisResult;

/**
 * UtilitarianStrategy implements the Utilitarian ethical framework.
 *
 * Purpose: This class analyzes decisions based on Utilitarian ethics, which focuses on
 * maximizing overall happiness and minimizing suffering for the greatest number of people.
 *
 * Design Pattern Used: Strategy Pattern
 * Why: Implements the EthicalStrategy interface, allowing it to be used interchangeably
 * with other ethical frameworks.
 *
 * OOP Principles Demonstrated:
 * - Polymorphism: Implements the analyzeDecision method defined in the interface.
 * - Encapsulation: Contains logic specific to Utilitarian analysis.
 */
// implements the utilitarian ethical framework, focused on the greatest good for the greatest number.
public class UtilitarianStrategy implements EthicalStrategy {

    @Override
// runs the ethical analysis for each framework on a given decision.
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        // Simple implementation - in a real system, this would involve more complex analysis
        double score = calculateUtilitarianScore(decision);
        String explanation = generateUtilitarianExplanation();
        String risks = identifyUtilitarianRisks();
        String violations = identifyUtilitarianViolations();

// Return this value to the method caller so the result can be used elsewhere.
        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
// This method performs one part of the class behavior.
    public String getFrameworkName() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Utilitarian Ethics";
    }

// This method performs one part of the class behavior.
    private double calculateUtilitarianScore(Decision decision) {
        String risks = safeLower(decision.getRisks());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0; // Neutral starting point

// If the condition inside the parentheses is true, the code inside the block will run.
        if (risks.contains("benefit") || risks.contains("help") || risks.contains("positive")) {
            score += 2.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (risks.contains("harm") || risks.contains("damage") || risks.contains("negative")) {
            score -= 2.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (risks.contains("many") || risks.contains("majority")) {
            score += 1.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("safety") || policies.contains("health") || policies.contains("public good")) {
            score += 1.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if ((policies.contains("cost cutting") || policies.contains("efficiency")) && risks.contains("harm")) {
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
    private String generateUtilitarianExplanation() {
        return "Utilitarian analysis evaluates the decision based on its ability to maximize overall happiness " +
               "and minimize suffering. The score reflects the net positive impact on all affected parties.";
    }

// This method performs one part of the class behavior.
    private String identifyUtilitarianRisks() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Risk of overlooking minority interests if the majority benefits.";
    }

// This method performs one part of the class behavior.
    private String identifyUtilitarianViolations() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Potential violation if the decision causes significant harm to individuals for minor collective gain.";
    }
}