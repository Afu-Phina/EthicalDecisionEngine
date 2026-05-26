package strategy;

import model.Decision;
import model.EthicalAnalysisResult;

/**
 * JusticeStrategy implements the Justice/Fairness ethical framework.
 *
 * Purpose: This class analyzes decisions based on principles of justice and fairness,
 * ensuring equitable treatment and distribution of benefits and burdens.
 *
 * Design Pattern Used: Strategy Pattern
 * Why: Implements the EthicalStrategy interface.
 *
 * OOP Principles Demonstrated:
 * - Polymorphism: Implements analyzeDecision.
 * - Encapsulation: Justice-specific analysis logic.
 */
public class JusticeStrategy implements EthicalStrategy {

    @Override
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateJusticeScore(decision);
        String explanation = generateJusticeExplanation(decision);
        String risks = identifyJusticeRisks(decision);
        String violations = identifyJusticeViolations(decision);

        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
    public String getFrameworkName() {
        return "Justice/Fairness Ethics";
    }

    private double calculateJusticeScore(Decision decision) {
        String stakeholders = safeLower(decision.getStakeholders());
        String risks = safeLower(decision.getRisks());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

        if (stakeholders.contains("equal") || risks.contains("fair") || risks.contains("equitable")) {
            score += 2.0;
        }
        if (risks.contains("unequal") || risks.contains("unfair") || risks.contains("discriminate")) {
            score -= 3.0;
        }
        if (stakeholders.contains("minority") || stakeholders.contains("vulnerable")) {
            score += 1.0; // Bonus for considering vulnerable groups
        }
        if (policies.contains("anti-discrimination") || policies.contains("equity") || policies.contains("fairness")) {
            score += 1.0;
        }
        if (policies.contains("preferential") || risks.contains("exclusive")) {
            score -= 1.0;
        }

        return Math.max(0, Math.min(10, score));
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private String generateJusticeExplanation(Decision decision) {
        return "Justice analysis evaluates fairness in the distribution of benefits and burdens. " +
               "The score reflects equitable treatment of all stakeholders.";
    }

    private String identifyJusticeRisks(Decision decision) {
        return "Risk of overemphasizing equality at the expense of efficiency or merit.";
    }

    private String identifyJusticeViolations(Decision decision) {
        return "Violation if the decision creates or perpetuates unfair inequalities.";
    }
}