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
// implements the justice ethical framework, focused on fairness and equality.
public class JusticeStrategy implements EthicalStrategy {

    @Override
// runs the ethical analysis for each framework on a given decision.
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateJusticeScore(decision);
        String explanation = generateJusticeExplanation();
        String risks = identifyJusticeRisks();
        String violations = identifyJusticeViolations();

// Return this value to the method caller so the result can be used elsewhere.
        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
// This method performs one part of the class behavior.
    public String getFrameworkName() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Justice/Fairness Ethics";
    }

// This method performs one part of the class behavior.
    private double calculateJusticeScore(Decision decision) {
        String stakeholders = safeLower(decision.getStakeholders());
        String risks = safeLower(decision.getRisks());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

// If the condition inside the parentheses is true, the code inside the block will run.
        if (stakeholders.contains("equal") || risks.contains("fair") || risks.contains("equitable")) {
            score += 2.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (risks.contains("unequal") || risks.contains("unfair") || risks.contains("discriminate")) {
            score -= 3.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (stakeholders.contains("minority") || stakeholders.contains("vulnerable")) {
            score += 1.0; // Bonus for considering vulnerable groups
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("anti-discrimination") || policies.contains("equity") || policies.contains("fairness")) {
            score += 1.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("preferential") || risks.contains("exclusive")) {
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
    private String generateJusticeExplanation() {
        return "Justice analysis evaluates fairness in the distribution of benefits and burdens. " +
               "The score reflects equitable treatment of all stakeholders.";
    }

// This method performs one part of the class behavior.
    private String identifyJusticeRisks() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Risk of overemphasizing equality at the expense of efficiency or merit.";
    }

// This method performs one part of the class behavior.
    private String identifyJusticeViolations() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Violation if the decision creates or perpetuates unfair inequalities.";
    }
}