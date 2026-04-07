# Spec: Unit Testing Coverage

## 1. Objective
Achieve robust test coverage for the Data, Domain, and Utils layers of the BudgetFlow application. This track implements the "Golden Rule" of engineering defined in the project, ensuring that the core business logic, database operations, and utility functions are resilient against regressions.

## 2. Functional Requirements
- **Data Layer Coverage:**
    - `BudgetDao`: Test insertions and retrievals for monthly budgets.
    - `TransactionDao`: Test insertions, retrievals, sums, and counts.
    - `UserPreferencesStore`: Test reads and updates using DataStore in a test environment.
- **Domain Layer Coverage (UseCases):**
    - `CheckTransactionsBeforeDeleteUseCase`
    - `DeleteCategoryUseCase`
    - `SaveCategoryUseCase`
    - `EnsureBudgetForMonthUseCase`
    - `GetDashboardSummaryUseCase` (Update existing or write comprehensive tests validating actual vs planned logic).
- **Utils/Core Layer Coverage:**
    - `DateUtils`: Test parsing and formatting of month/year strings.
    - `CurrencyVisualTransformation`: Test correct formatting and offset mappings.
    - `CategoryMapper`: Test display name and description mappings.

## 3. Technical Requirements
- Utilize the modern testing infrastructure established in Track 04 (`JUnit`, `Robolectric`, `MockK`, `Turbine`, `kotlinx-coroutines-test`).
- Write clean, Arrange-Act-Assert structured test methods.
- Ensure isolated test scopes (e.g., using `runTest` and isolated `MockK` mocks).

## 4. UI/UX Considerations
- No direct UI changes. This track strictly impacts the underlying stability of the app.

## 5. Acceptance Criteria
- [ ] Comprehensive unit test suites exist for `BudgetDao`, `TransactionDao`, and `UserPreferencesStore`.
- [ ] Comprehensive unit test suites exist for all UseCases.
- [ ] Comprehensive unit test suites exist for core utilities (`DateUtils`, `CurrencyVisualTransformation`, `CategoryMapper`).
- [ ] All unit tests pass locally (`./gradlew test`).
- [ ] `./gradlew build` passes cleanly.
