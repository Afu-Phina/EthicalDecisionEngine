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
public class UtilitarianStrategy implements EthicalStrategy {

    @Override
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        // Simple implementation - in a real system, this would involve more complex analysis
        double score = calculateUtilitarianScore(decision);
        String explanation = generateUtilitarianExplanation(decision);
        String risks = identifyUtilitarianRisks(decision);
        String violations = identifyUtilitarianViolations(decision);

        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
    public String getFrameworkName() {
        return "Utilitarian Ethics";
    }

    private double calculateUtilitarianScore(Decision decision) {
        String risks = safeLower(decision.getRisks());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0; // Neutral starting point

        if (risks.contains("benefit") || risks.contains("help") || risks.contains("positive")) {
            score += 2.0;
        }
        if (risks.contains("harm") || risks.contains("damage") || risks.contains("negative")) {
            score -= 2.0;
        }
        if (risks.contains("many") || risks.contains("majority")) {
            score += 1.0;
        }
        if (policies.contains("safety") || policies.contains("health") || policies.contains("public good")) {
            score += 1.0;
        }
        if ((policies.contains("cost cutting") || policies.contains("efficiency")) && risks.contains("harm")) {
            score -= 1.0;
        }

        return Math.max(0, Math.min(10, score));
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private String generateUtilitarianExplanation(Decision decision) {
        return "Utilitarian analysis evaluates the decision based on its ability to maximize overall happiness " +
               "and minimize suffering. The score reflects the net positive impact on all affected parties.";
    }

    private String identifyUtilitarianRisks(Decision decision) {
        return "Risk of overlooking minority interests if the majority benefits.";
    }

    private String identifyUtilitarianViolations(Decision decision) {
        return "Potential violation if the decision causes significant harm to individuals for minor collective gain.";
    }
}