# Plan: Migrate to Spec Driven Development (SDD) via Conductor

## Background & Motivation
The BudgetFlow project currently uses sequential `plan-stageX.md` files for planning. To fully leverage the modern Spec Driven Development (SDD) methodology using the Conductor extension, the `conductor/` directory structure needs to be formalized. This will create a centralized source of truth for the project context, technical stack, product guidelines, and track the development life cycle via "Tracks".

## Scope & Impact
The scope is purely organizational and documentary. We will establish the foundation files required by the Conductor extension without modifying existing source code. The legacy `plan-stageX.md` files will remain in place as requested.

## Proposed Solution

We will create the following core Conductor files:

1. **`conductor/index.md`**: 
   - The central nervous system of the project. It will contain links to the Product Definition, Tech Stack, Workflow, Product Guidelines, and the Tracks Registry.

2. **`conductor/workflow.md`**: 
   - Defines the SDD lifecycle:
     - **Spec Creation:** Writing the functional and technical specification for a feature (`spec.md`).
     - **Planning:** Creating the step-by-step implementation plan (`plan.md`).
     - **Implementation:** Writing the code according to the UDF and MVVM architecture.
     - **Verification:** Testing and validating the implementation against the spec.

3. **`conductor/product-guidelines.md`**: 
   - Extracts and details the UI/UX rules, such as using Material 3, the "Definition + Practical Tip" educational hints, color schemes, and general accessibility/usability standards.

4. **`conductor/tracks.md`**: 
   - A markdown registry (table or list) of all ongoing, planned, and completed development tracks. This replaces the unstructured "stage" system for future development.

5. **`conductor/tracks/`**: 
   - Establish the directory that will host future feature/bug-fix tracks. (e.g., `conductor/tracks/01-resume-development/`).

## Implementation Steps
1. Create `conductor/index.md` and populate it with links.
2. Create `conductor/workflow.md` and document the SDD methodology for the team.
3. Create `conductor/product-guidelines.md` based on `GEMINI.md` UI/UX goals.
4. Create `conductor/tracks.md` with an initial empty registry.
5. Create the `conductor/tracks/` folder using a placeholder file or establishing the first track to resume development.

## Verification
- Verify that opening `conductor/index.md` allows seamless navigation to all other structural Conductor files.
- Ensure the Conductor extension properly recognizes the project context and paths moving forward.
