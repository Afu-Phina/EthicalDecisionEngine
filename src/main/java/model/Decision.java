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
public class Decision {
    private String description;
    private String context;
    private String stakeholders;
    private String risks;
    private String applicablePolicies;

    // Default constructor
    public Decision() {}

    // Constructor with parameters
    public Decision(String description, String context, String stakeholders, String risks, String applicablePolicies) {
        this.description = description;
        this.context = context;
        this.stakeholders = stakeholders;
        this.risks = risks;
        this.applicablePolicies = applicablePolicies;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStakeholders() {
        return stakeholders;
    }

    public void setStakeholders(String stakeholders) {
        this.stakeholders = stakeholders;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    public String getApplicablePolicies() {
        return applicablePolicies;
    }

    public void setApplicablePolicies(String applicablePolicies) {
        this.applicablePolicies = applicablePolicies;
    }

    @Override
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