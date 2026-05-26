package strategy;

import model.Decision;
import model.EthicalAnalysisResult;

/**
 * EthicalStrategy interface defines the contract for all ethical analysis frameworks.
 *
 * Purpose: This interface allows different ethical frameworks to be implemented as strategies,
 * enabling the system to apply various ethical analyses interchangeably.
 *
 * Design Pattern Used: Strategy Pattern
 * Why: The Strategy pattern is ideal here because we have multiple algorithms (ethical frameworks)
 * that can be selected at runtime. It promotes code reusability, maintainability, and allows
 * for easy addition of new frameworks without modifying existing code.
 *
 * OOP Principles Demonstrated:
 * - Abstraction: Defines an abstract interface for ethical analysis.
 * - Polymorphism: Different implementations will provide their own versions of analyzeDecision.
 */
public interface EthicalStrategy {
    /**
     * Analyzes a given decision using the specific ethical framework.
     *
     * @param decision The decision to analyze
     * @return The result of the ethical analysis
     */
    EthicalAnalysisResult analyzeDecision(Decision decision);

    /**
     * Returns the name of the ethical framework.
     *
     * @return The framework name
     */
    String getFrameworkName();
}