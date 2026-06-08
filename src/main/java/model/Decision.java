package model;

/**
 * Decision class represents an ethical decision to be analyzed.
 *
 * Purpose: This class encapsulates the data for an ethical decision, including the description,
 * context, and potential stakeholders. It serves as the input for ethical analysis frameworks.
 *
 * OOP Principles Demonstrated:
 * - Encapsulation: The decision details are encapsulated within private fields with public getters/setters.
 * - Abstraction: This class abstracts the concept of an ethical decision, hiding implementation details.
 *
 * Design Pattern: Not directly applicable, but this is a basic data model class.
 */
// stores the input details for one ethical decision to be analyzed.
public class Decision {
// stores the decision description entered by the user.
    private String description;
// stores the context or background information for the decision.
    private String context;
// stores the stakeholders affected by the decision.
    private String stakeholders;
// stores the risks, tradeoffs, or likely outcomes of the decision.
    private String risks;
// stores any policies, laws, or rules that apply to the decision.
    private String applicablePolicies;

    // Default constructor
// This method performs one part of the class behavior.
    public Decision() {}

    // Constructor with parameters
// This method performs one part of the class behavior.
    public Decision(String description, String context, String stakeholders, String risks, String applicablePolicies) {
        this.description = description;
        this.context = context;
        this.stakeholders = stakeholders;
        this.risks = risks;
        this.applicablePolicies = applicablePolicies;
    }

    // Getters and Setters
// This method performs one part of the class behavior.
    public String getDescription() {
// Return this value to the method caller so the result can be used elsewhere.
        return description;
    }

// This method performs one part of the class behavior.
    public void setDescription(String description) {
        this.description = description;
    }

// This method performs one part of the class behavior.
    public String getContext() {
// Return this value to the method caller so the result can be used elsewhere.
        return context;
    }

// This method performs one part of the class behavior.
    public void setContext(String context) {
        this.context = context;
    }

// This method performs one part of the class behavior.
    public String getStakeholders() {
// Return this value to the method caller so the result can be used elsewhere.
        return stakeholders;
    }

// This method performs one part of the class behavior.
    public void setStakeholders(String stakeholders) {
        this.stakeholders = stakeholders;
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
    public String getApplicablePolicies() {
// Return this value to the method caller so the result can be used elsewhere.
        return applicablePolicies;
    }

// This method performs one part of the class behavior.
    public void setApplicablePolicies(String applicablePolicies) {
        this.applicablePolicies = applicablePolicies;
    }

    @Override
// This method performs one part of the class behavior.
    public String toString() {
        return "Decision{" +
                "description='" + description + '\'' +
                ", context='" + context + '\'' +
                ", stakeholders='" + stakeholders + '\'' +
                ", risks='" + risks + '\'' +
                ", applicablePolicies='" + applicablePolicies + '\'' +
                '}';
    }
}