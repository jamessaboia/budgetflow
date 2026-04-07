# Plan: Unit Testing Coverage (Track 06)

## Step 1: Core Utilities Testing
- [x] Test `DateUtils.kt`: Create `DateUtilsTest.kt`. Verify formatting, parsing, and current month functionality.
- [x] Test `CurrencyVisualTransformation.kt`: Create `CurrencyVisualTransformationTest.kt`. Verify mask formatting and cursor offset.
- [x] Test `CategoryMapper.kt`: Create `CategoryMapperTest.kt`. Verify that `getCategoryDisplayName` and `getCategoryDescription` return expected localized strings or fallback names.

## Step 2: Data Layer Testing
- [x] Test `BudgetDao`: Create `BudgetDaoTest.kt` (Robolectric). Verify insertions, retrievals by month, and `getLatestBudgetBefore`.
- [x] Test `TransactionDao`: Create `TransactionDaoTest.kt` (Robolectric). Verify insertions, deletions, totals by month (Income vs Expense), and category counts.
- [x] Test `UserPreferencesStore`: Create `UserPreferencesStoreTest.kt` (Robolectric). Verify DataStore reads/updates.

## Step 3: Domain Layer Testing
- [x] Test `EnsureBudgetForMonthUseCase`: Create `EnsureBudgetForMonthUseCaseTest.kt`. Verify budget creation and inheritance logic.
- [x] Test `DeleteCategoryUseCase` & `SaveCategoryUseCase`: Create simple tests.
- [x] Test `CheckTransactionsBeforeDeleteUseCase`: Create tests validating the logic.
- [x] Test `GetDashboardSummaryUseCase`: Refactor/Complete existing tests to reflect the new `actualIncome` and Cash Flow logic from Track 10.

## Step 4: Verification
- [x] Run all unit tests (`./gradlew test`) and ensure 100% test success.
- [x] Ensure the overall build works cleanly (`./gradlew build`).
