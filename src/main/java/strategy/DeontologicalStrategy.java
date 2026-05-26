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
public class DeontologicalStrategy implements EthicalStrategy {

    @Override
    public EthicalAnalysisResult analyzeDecision(Decision decision) {
        double score = calculateDeontologicalScore(decision);
        String explanation = generateDeontologicalExplanation(decision);
        String risks = identifyDeontologicalRisks(decision);
        String violations = identifyDeontologicalViolations(decision);

        return new EthicalAnalysisResult(getFrameworkName(), score, explanation, risks, violations);
    }

    @Override
    public String getFrameworkName() {
        return "Deontological Ethics";
    }

    private double calculateDeontologicalScore(Decision decision) {
        String description = safeLower(decision.getDescription());
        String policies = safeLower(decision.getApplicablePolicies());
        double score = 5.0;

        // Check for rule adherence
        if (description.contains("duty") || description.contains("obligation") || description.contains("rule")) {
            score += 2.0;
        }
        if (description.contains("lie") || description.contains("deceive") || description.contains("break rule")) {
            score -= 3.0;
        }
        if (description.contains("respect") || description.contains("rights")) {
            score += 1.5;
        }
        if (policies.contains("law") || policies.contains("rights") || policies.contains("privacy")) {
            score += 1.0;
        }
        if (policies.contains("violate") || description.contains("violate")) {
            score -= 1.5;
        }

        return Math.max(0, Math.min(10, score));
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private String generateDeontologicalExplanation(Decision decision) {
        return "Deontological analysis evaluates whether the decision adheres to moral rules and duties. " +
               "The score reflects compliance with ethical principles regardless of outcomes.";
    }

    private String identifyDeontologicalRisks(Decision decision) {
        return "Risk of rigid application that may lead to harmful consequences in exceptional situations.";
    }

    private String identifyDeontologicalViolations(Decision decision) {
        return "Violation if the decision breaks established moral rules or duties.";
    }
}