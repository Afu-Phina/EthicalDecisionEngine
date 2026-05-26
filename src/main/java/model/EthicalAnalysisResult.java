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
public class EthicalAnalysisResult {
    private String frameworkName;
    private double score; // 0-10 scale
    private String explanation;
    private String risks;
    private String violations;

    // Default constructor
    public EthicalAnalysisResult() {}

    // Constructor with parameters
    public EthicalAnalysisResult(String frameworkName, double score, String explanation, String risks, String violations) {
        this.frameworkName = frameworkName;
        this.score = score;
        this.explanation = explanation;
        this.risks = risks;
        this.violations = violations;
    }

    // Getters and Setters
    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    public String getViolations() {
        return violations;
    }

    public void setViolations(String violations) {
        this.violations = violations;
    }

    @Override
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