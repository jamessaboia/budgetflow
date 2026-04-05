# BudgetFlow - SDD Workflow

We use **Spec Driven Development (SDD)** with the Conductor extension to build BudgetFlow methodically and reliably.

## The SDD Lifecycle

Every new feature, architectural change, or major bug fix must be managed as a "Track". The life cycle of a track is as follows:

1. **Spec Creation (`spec.md`)**
   - **Goal:** Define *what* we are building and *why*.
   - **Contents:** User stories, functional requirements, technical constraints, UI/UX considerations, and acceptance criteria.
   - **Rule:** Do not write code until the spec is clear and approved.

2. **Planning (`plan.md`)**
   - **Goal:** Define *how* we are going to build it.
   - **Contents:** A step-by-step checklist of the implementation details. Break down the spec into small, testable chunks (e.g., Database changes -> UseCases -> ViewModel -> UI).
   - **Rule:** Keep tasks granular. Include verification steps for each task.

3. **Implementation**
   - **Goal:** Execute the plan.
   - **Execution:** Follow the plan sequentially. Use iterative verification (local testing, `./gradlew build`) after completing chunks.
   - **Rule:** Adhere strictly to the project's Architecture (UDF, MVVM, Clean Architecture) and Guidelines. Avoid scope creep.

4. **Verification (Testing & Build)**
   - **Goal:** Validate the final outcome against the initial `spec.md` acceptance criteria and ensure zero regressions.
   - **Execution:** 
     - Run all unit tests for the affected layers (Data, Domain, Utils).
     - Ensure the "Golden Rule" is followed: existing tests must pass and new logic must have its own tests.
     - Execute `./gradlew build` to confirm overall project health.
   - **Rule:** If any test or build task fails, it must be fixed. Completion is only achieved with a 100% successful build.

## Track Structure
A new track should be created under `conductor/tracks/<track_id>/` and must contain:
- `spec.md`
- `plan.md`
