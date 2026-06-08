package model;

/**
 * EthicalAnalysisResult class represents the result of an ethical analysis by a framework.
 *
 * Purpose: This class encapsulates the output of an ethical framework analysis, including
 * the score, explanation, risks, and violations identified.
 *
 * OOP Principles Demonstrated:
 * - Encapsulation: The analysis results are encapsulated within private fields.
 * - Abstraction: Abstracts the concept of an analysis result.
 */
// stores the output from one ethical framework analysis.
public class EthicalAnalysisResult {
// stores the name of the ethical framework used for this analysis.
    private String frameworkName;
// stores the numeric score assigned by the ethical framework.
    private double score; // 0-10 scale
// stores a short text explanation of why the score was assigned.
    private String explanation;
// stores the risks, tradeoffs, or likely outcomes of the decision.
    private String risks;
// stores any potential policy or ethics violations detected by this analysis.
    private String violations;

    // Default constructor
// This method performs one part of the class behavior.
    public EthicalAnalysisResult() {}

    // Constructor with parameters
// This method performs one part of the class behavior.
    public EthicalAnalysisResult(String frameworkName, double score, String explanation, String risks, String violations) {
        this.frameworkName = frameworkName;
        this.score = score;
        this.explanation = explanation;
        this.risks = risks;
        this.violations = violations;
    }

    // Getters and Setters
// This method performs one part of the class behavior.
    public String getFrameworkName() {
// Return this value to the method caller so the result can be used elsewhere.
        return frameworkName;
    }

// This method performs one part of the class behavior.
    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

// This method performs one part of the class behavior.
    public double getScore() {
// Return this value to the method caller so the result can be used elsewhere.
        return score;
    }

// This method performs one part of the class behavior.
    public void setScore(double score) {
        this.score = score;
    }

// This method performs one part of the class behavior.
    public String getExplanation() {
// Return this value to the method caller so the result can be used elsewhere.
        return explanation;
    }

// This method performs one part of the class behavior.
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

// This method performs one part of the class behavior.
    public String getRisks() {
// Return this value to the method caller so the result can be used elsewhere.
        return risks;
    }

// This method performs one part of the class behavior.
    public void setRisks(String risks) {
        this.risks = risks;
    }

// This method performs one part of the class behavior.
    public String getViolations() {
// Return this value to the method caller so the result can be used elsewhere.
        return violations;
    }

// This method performs one part of the class behavior.
    public void setViolations(String violations) {
        this.violations = violations;
    }

    @Override
// This method performs one part of the class behavior.
    public String toString() {
        return "EthicalAnalysisResult{" +
                "frameworkName='" + frameworkName + '\'' +
                ", score=" + score +
                ", explanation='" + explanation + '\'' +
                ", risks='" + risks + '\'' +
                ", violations='" + violations + '\'' +
                '}';
    }
}