# BudgetFlow - Android Tech Lead Guidelines

## Project Overview
BudgetFlow is a native Android application designed for personal finance management. It helps users organize monthly income using a flexible 50/30/20 budget rule.
- **Goal:** Didactic, robust, and scalable MVP.
- **Tech Stack:** Kotlin, Jetpack Compose (Material 3), MVVM + Pragmatic Clean Architecture.

## Core Mandates & Workflow
1. **Source Control:** NEVER perform `git commit` or `git push` automatically. 
   - **Strategy:** As Tech Lead, evaluate if the current progress deserves a commit. If so, notify the user and suggest a professional commit message in English.
2. **Architecture:** Follow Unidirectional Data Flow (UDF).
   - **UiState:** Single source of truth for the screen.
   - **UiEvent:** Actions sent from UI to ViewModel.
   - **UiEffect:** One-time side effects (e.g., navigation, Toast) sent from ViewModel to UI.
3. **Clean Code & Android Best Practices:**
   - **S.O.L.I.D.:** Every class must have a single responsibility. Use Dependency Inversion (Hilt) to decouple layers.
   - **Naming:** Follow Kotlin/Android conventions. Use names that reveal intent (e.g., `calculateMonthlyRemaining` instead of `calc`).
   - **No Comments:** Code must be self-explanatory. Avoid adding comments (`//` or `/* */`) to ensure clean, professional code. AI-style explanatory comments are strictly prohibited.
   - **Stateless Composables:** Hoist state to make UI components testable and reusable.

   - **No Logic in UI:** Composables must not contain business or validation logic.
4. **Dependency Management:** Always use `gradle/libs.versions.toml`.

## Development Workflow
- **Plan Before Implementing:** Create or update a plan file in `conductor/` before starting any implementation.
- **Incremental & Iterative:** Break down tasks into small, verifiable chunks.
- **Explain "Why":** Document the rationale behind architectural choices, especially when trade-offs are involved.
- **Didactic & Scalable:** Prefer solutions that are easy to explain/understand but don't compromise future growth.
- **Error Handling:** Use a `Result` wrapper (or similar) to propagate errors from the Data layer to the UI, avoiding scattered try-catches.
- **Unit Testing (Mandatory Coverage):** 
    - **Data Layer:** Implement tests for `data/repository` (data logic) and `data/dao` (Room access).
    - **Utilities:** Implement tests for `core` and `utils` classes.
    - **Domain Layer:** Implement tests for UseCases and Mappers.
    - **Golden Rule:** If you modify code in a layer that already has tests, you MUST ensure they continue to pass. If you add new logic to these layers, you MUST create the corresponding new tests.

## Build and Validation
- **Local Verification:** Always run `./gradlew build` before finalizing any task. The build MUST include the execution of all unit tests.
- **Zero Regressions:** If a build or test fails, it must be fixed before the task is considered finished.
- **Pre-Commit Check:** Verify that structural changes don't break the Hilt dependency graph, Room compilation, or any existing test cases.

## Technical Stack & Tools
- **DI:** Hilt (Dependency Injection).
- **Persistence:** Room (Complex data) and DataStore (Preferences).
- **Navigation:** Navigation Compose.
- **Async:** Coroutines & Flow (StateFlow for UI state).
- **Processing:** KSP (Kotlin Symbol Processing).

## MVP Functional Scope
- Monthly Income Registration (Base + Extra).
- Editable Budget Percentages (Default: 50/30/20).
- Groups: Needs (Essenciais), Wants (Estilo de Vida), Savings (Reserva/Investimentos).
- Manual Transaction Entry (Value, Type, Category, Date, Description).
- Dashboard: Visual progress, remaining balance, and limit alerts.

## Implementation Principles
- **Educational Value (Hints):** Every major feature should include educational "hints" or tips. Standardize hints using a "Definition + Practical Tip" pattern to improve user financial literacy.
- **Pragmatism:** Avoid overengineering. Do not create interfaces for everything if not needed for testing or scalability.
- **Scalability:** Ensure the architecture supports a future backend migration with minimal friction.
- **Documentation:** Maintain `GEMINI.md` as the source of truth for engineering standards.
