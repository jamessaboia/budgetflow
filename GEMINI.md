# BudgetFlow - Android Tech Lead Guidelines

## Project Overview
BudgetFlow is a native Android application designed for personal finance management. It helps users organize monthly income using a flexible 50/30/20 budget rule.
- **Goal:** Didactic, robust, and scalable MVP.
- **Tech Stack:** Kotlin, Jetpack Compose (Material 3), MVVM + Pragmatic Clean Architecture.

## Core Mandates & Workflow
1. **Source Control:** NEVER perform `git commit` or `git push` automatically. 
   - **Strategy:** As Tech Lead, evaluate if the current progress deserves a commit. If so, notify the user and suggest a professional commit message in English.
2. **Architecture:** Follow Unidirectional Data Flow (UDF).
   - **UI Layer:** Compose + ViewModels (UiState exposure).
   - **Domain Layer:** Only for complex business logic (UseCases).
   - **Data Layer:** Repository pattern as the single source of truth (Room/DataStore).
3. **Clean Code & Standards:**
   - Prioritize readability and maintainability over "clever" code.
   - Use meaningful naming (Dagger/Hilt, Room, etc., following official Google naming conventions).
   - Keep Composables stateless (state hoisting) and provide `@Preview` for UI components.
4. **Dependency Management:** Always use `gradle/libs.versions.toml`. Define dependencies in the version catalog first, then reference them in `build.gradle.kts`.

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
- **Pragmatism:** Avoid overengineering. Do not create interfaces for everything if not needed for testing or scalability.
- **Scalability:** Ensure the architecture supports a future backend migration with minimal friction.
- **Verification:** Always run `./gradlew build` or relevant tests after structural changes.
- **Documentation:** Maintain `GEMINI.md` as the source of truth for engineering standards.
