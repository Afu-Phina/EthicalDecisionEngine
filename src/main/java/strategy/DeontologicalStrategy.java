package strategy;

import model.Decision;
import model.EthicalAnalysisResult;

/**
 * DeontologicalStrategy implements the Deontological ethical framework.
 *
 * Purpose: This class analyzes decisions based on Deontological ethics, which focuses on
 * adherence to rules, duties, and moral laws regardless of consequences.
 *
 * Design Pattern Used: Strategy Pattern
 * Why: Implements the EthicalStrategy interface for interchangeable use.
 *
 * OOP Principles Demonstrated:
 * - Polymorphism: Implements the analyzeDecision method.
 * - Encapsulation: Contains Deontological-specific logic.
 */
// implements the deontological ethical framework, focused on rules and duties.
public class DeontologicalStrategy implements EthicalStrategy {

    @Override
// runs the ethical analysis for each framework on a given decision.
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateDeontologicalScore(decision);
        String explanation = generateDeontologicalExplanation();
        String risks = identifyDeontologicalRisks();
        String violations = identifyDeontologicalViolations();

// Return this value to the method caller so the result can be used elsewhere.
        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
// This method performs one part of the class behavior.
    public String getFrameworkName() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Deontological Ethics";
    }

// This method performs one part of the class behavior.
    private double calculateDeontologicalScore(Decision decision) {
        String description = safeLower(decision.getDescription());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

        // Check for rule adherence
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.contains("duty") || description.contains("obligation") || description.contains("rule")) {
            score += 2.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.contains("lie") || description.contains("deceive") || description.contains("break rule")) {
            score -= 3.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (description.contains("respect") || description.contains("rights")) {
            score += 1.5;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("law") || policies.contains("rights") || policies.contains("privacy")) {
            score += 1.0;
        }
// If the condition inside the parentheses is true, the code inside the block will run.
        if (policies.contains("violate") || description.contains("violate")) {
            score -= 1.5;
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
    private String generateDeontologicalExplanation() {
        return "Deontological analysis evaluates whether the decision adheres to moral rules and duties. " +
               "The score reflects compliance with ethical principles regardless of outcomes.";
    }

// This method performs one part of the class behavior.
    private String identifyDeontologicalRisks() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Risk of rigid application that may lead to harmful consequences in exceptional situations.";
    }

// This method performs one part of the class behavior.
    private String identifyDeontologicalViolations() {
// Return this value to the method caller so the result can be used elsewhere.
        return "Violation if the decision breaks established moral rules or duties.";
    }
}