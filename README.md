# Ethical Decision Engine

A JavaFX-based ethical analysis prototype that evaluates decisions through multiple ethical frameworks and makes the reasoning visible.

## What it does

- Accepts a decision description, context, stakeholders, potential outcomes, and applicable policies.
- Runs the decision through four ethical lenses:
  - Utilitarian Ethics
  - Deontological Ethics
  - Justice/Fairness Ethics
  - Virtue Ethics
- Produces:
  - A scored verdict card
  - Conflict analysis across frameworks
  - Remediation guidance
  - Practical solution paths
  - Exportable audit trail for traceability

## What changed

- Added support for an `applicablePolicies` field in `model.Decision`.
- Expanded `model.EthicalAnalysisResult` to store framework names explicitly.
- Improved `controller.EthicalAnalysisController` to generate:
  - clearer verdict categories
  - conflict analysis with named frameworks
  - remediation guidance
  - solution paths
- Updated UI input to capture applicable rules/policies.

## Run the app

From the project root:

```bash
apache-maven-3.9.15\bin\mvn.cmd compile javafx:run
```

Or using the provided Maven wrapper if configured.

## Project structure

- `src/main/java/com/example/Main.java` — JavaFX entry point
- `src/main/java/view/DashboardView.java` — UI layer
- `src/main/java/controller/EthicalAnalysisController.java` — analysis coordination
- `src/main/java/factory/EthicalStrategyFactory.java` — framework factory
- `src/main/java/model/Decision.java` — input model
- `src/main/java/model/EthicalAnalysisResult.java` — analysis result model
- `src/main/java/strategy/*` — four ethical framework implementations

## Next improvements

- Add additional decision metadata such as risk level and stakeholder weights.
- Expand scoring with real policy/rule parsing rather than keyword matching.
- Add exportable audit trail (text-based evidence) and richer policy-aware scoring.
- Add unit tests to validate framework scoring and conflict detection.
