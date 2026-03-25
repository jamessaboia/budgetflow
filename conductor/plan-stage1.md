# Plan - Stage 1: Architectural Vision and Planning

## Objective
Define the project architecture, package structure, dependencies, and modeling for the BudgetFlow app.

## Project Scope
Personal finance app with 50/30/20 budget logic, local-first with Room/DataStore, and MVVM+Clean Architecture.

## Proposed Package Structure
`com.jamessaboia.budgetflow`
- **di/**: Hilt modules.
- **core/**: Theme, Common Utils, Base Classes.
- **navigation/**: NavGraph and Destinations.
- **data/**
    - **local/**: Room (DB, Entities, DAO), DataStore.
    - **repository/**: Repository implementations.
    - **model/**: Data entities (DTOs if any).
- **domain/**
    - **model/**: Domain models.
    - **usecase/**: Business logic components.
    - **repository/**: Repository interfaces.
- **ui/**
    - **features/**: Dashboard, Onboarding, Transaction, Settings.
    - **components/**: Shared UI elements.

## Modeling (Initial Entities)
1. **MonthlyBudget:** `id`, `monthYear`, `baseIncome`, `extraIncome`, `totalIncome`, `needsPercentage`, `wantsPercentage`, `savingsPercentage`.
2. **Category:** `id`, `name`, `groupType` (NEEDS, WANTS, SAVINGS), `budgetLimit`, `isDefault`.
3. **Transaction:** `id`, `amount`, `type` (INCOME, EXPENSE), `categoryId`, `description`, `date`, `monthYear`.
4. **UserPreferences:** `isOnboardingComplete`, `defaultPercentages`, etc.

## Technical Backlog
1. **Setup Core:** Hilt, Room, DataStore, Navigation dependencies.
2. **Model & Persistence:** Create Database, Entities, and DAOs.
3. **Domain Layer:** Define Repository Interfaces and basic Use Cases.
4. **UI Setup:** Base theme, Navigation graph, and Hilt setup.
5. **Feature - Onboarding:** Screen for income/budget setup.
6. **Feature - Dashboard:** Monthly summary and progress.
7. **Feature - Transactions:** Add and list transactions.

## Verification
- Code builds after setup.
- Package structure is created.
- Entities are defined as Kotlin data classes.
