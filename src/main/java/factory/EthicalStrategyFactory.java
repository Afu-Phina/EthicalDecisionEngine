package factory;

/**
 * EthicalStrategyFactory implements the Factory pattern to create ethical strategy objects.
 *
 * Purpose: This class provides a centralized way to create instances of different ethical
 * frameworks without exposing the instantiation logic to the client code.
 *
 * Design Pattern Used: Factory Pattern
 * Why: The Factory pattern is chosen to encapsulate the creation of strategy objects,
 * making it easy to add new frameworks or change implementations without modifying
 * the code that uses these strategies. It promotes loose coupling and scalability.
 *
 * OOP Principles Demonstrated:
 * - Abstraction: Hides the complexity of object creation.
 * - Polymorphism: Returns objects that implement the same interface but have different behaviors.
 */
public class EthicalStrategyFactory {

    /**
     * Creates an ethical strategy based on the framework name.
     *
     * @param frameworkName The name of the ethical framework
     * @return An instance of the corresponding EthicalStrategy, or null if not found
     */
    public static strategy.EthicalStrategy createStrategy(String frameworkName) {
        return switch (frameworkName.toLowerCase()) {
            case "utilitarian", "utilitarianism" -> new strategy.UtilitarianStrategy();
            case "deontological", "deontology" -> new strategy.DeontologicalStrategy();
            case "justice", "fairness" -> new strategy.JusticeStrategy();
            case "virtue" -> new strategy.VirtueStrategy();
            default -> {
                System.err.println("Unknown ethical framework: " + frameworkName);
                yield null;
            }
        };
    }

    /**
     * Returns an array of all available ethical strategies.
     *
     * @return Array of EthicalStrategy instances
     */
    public static strategy.EthicalStrategy[] getAllStrategies() {
        return new strategy.EthicalStrategy[] {
            new strategy.UtilitarianStrategy(),
            new strategy.DeontologicalStrategy(),
            new strategy.JusticeStrategy(),
            new strategy.VirtueStrategy()
        };
    }
}