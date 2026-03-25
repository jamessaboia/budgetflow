# Plan - Stage 3: Persistence & Models (Marco 2)

## Objective
Implement the complete persistence layer of the BudgetFlow app, including Room Database for transactions and budgets, and DataStore for user preferences. This stage focuses on the "Single Source of Truth" within the Data Layer, ensuring clear separation between Local Entities (Room) and Domain Models.

## Scope & Impact
- Defines Room entities: `MonthlyBudgetEntity`, `CategoryEntity`, `TransactionEntity`.
- Defines Domain models: `MonthlyBudget`, `Category`, `Transaction`.
- Implements `BudgetFlowDatabase`, `BudgetDao`, `CategoryDao`, `TransactionDao`.
- Sets up `UserPreferencesStore` using DataStore.
- Implements `BudgetRepository` and `TransactionRepository` interfaces in the Domain layer and their implementations in the Data layer.
- Provides Hilt modules for Database, DataStore, and Repositories.

## Implementation Steps

### 1. Domain Models (`domain/model/`)
- Create `BudgetGroup` enum (NEEDS, WANTS, SAVINGS).
- Create `TransactionType` enum (INCOME, EXPENSE).
- Define `MonthlyBudget`, `Category`, and `Transaction` as pure Kotlin data classes.

### 2. Local Entities (`data/local/entities/`)
- Create `MonthlyBudgetEntity`, `CategoryEntity`, and `TransactionEntity` with Room annotations.
- Add necessary foreign keys (e.g., `TransactionEntity` linked to `CategoryEntity`).

### 3. DAOs & Database (`data/local/dao/` & `data/local/`)
- Implement `BudgetDao`: Manage monthly budget settings.
- Implement `CategoryDao`: Manage spending categories.
- Implement `TransactionDao`: CRUD for income and expenses.
- Create `BudgetFlowDatabase` extending `RoomDatabase`.

### 4. DataStore for Preferences (`data/local/`)
- Implement `UserPreferencesStore`: Store `isOnboardingComplete` and initial budget defaults.

### 5. Repository Interfaces & Implementations (`domain/repository/` & `data/repository/`)
- Define `BudgetRepository` interface (Domain).
- Define `TransactionRepository` interface (Domain).
- Implement `BudgetRepositoryImpl` (Data): Orchestrates Room and DataStore access.
- Implement `TransactionRepositoryImpl` (Data): Orchestrates Transaction Room access.

### 6. Dependency Injection (`di/`)
- Create `DatabaseModule`: Provides Room instance and DAOs.
- Create `DataStoreModule`: Provides DataStore instance.
- Create `RepositoryModule`: Binds repository interfaces to implementations.

## Verification & Testing
- Use `ExampleUnitTest.kt` to verify that Room entities and domain models can be mapped correctly.
- Verify Hilt dependency graph with a temporary test injection.
- Run `./gradlew build` to ensure KSP generates DAOs correctly.
