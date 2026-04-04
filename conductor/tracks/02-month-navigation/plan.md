# Plan: Month Navigation & Persistence (Track 02)

## Step 1: Core Logic Update (Domain & Data)
- [ ] **EnsureBudgetUseCase:** Create a new UseCase `EnsureBudgetForMonthUseCase` that:
    - Checks if a budget exists for a given `monthYear`.
    - If not, finds the most recent budget in the database.
    - Clones the most recent budget values (income, percentages) into a new entry for the requested month.
- [ ] **Update GetDashboardSummaryUseCase:** Modify it to accept `monthYear: String` as a parameter.
- [ ] **Update Repository Interfaces:** Ensure `BudgetRepository` has methods to find the "latest available" budget before a specific month.

## Step 2: ViewModel Integration
- [ ] **DashboardViewModel:**
    - Add a `selectedMonth: StateFlow<String>` state (formatted as "yyyy-MM").
    - Use `flatMapLatest` to reload the dashboard summary whenever `selectedMonth` changes.
    - Call `EnsureBudgetForMonthUseCase` before loading the summary to guarantee data availability.
    - Add methods `nextMonth()` and `previousMonth()`.
- [ ] **TransactionsViewModel:**
    - Similar to Dashboard, add `selectedMonth` state and navigation methods.
    - Update the transaction flow to react to month changes.

## Step 3: UI Components
- [ ] **MonthPicker Component:** Create a reusable Compose component with:
    - Left arrow (Previous).
    - Month/Year display (formatted as "MMMM yyyy").
    - Right arrow (Next).
- [ ] **Dashboard Screen:** Integrate `MonthPicker` below the `TopAppBar`.
- [ ] **Transactions Screen:** Integrate `MonthPicker` below the `TopAppBar`.

## Step 4: Refinement & Formatting
- [ ] **Date Formatting:** Centralize date formatting logic in a helper class/object to ensure consistency between "yyyy-MM" (DB) and "MMMM yyyy" (UI).
- [ ] **String Resources:** Add necessary strings for month names and accessibility.

## Step 5: Verification
- [ ] Run `./gradlew build` to ensure no regressions.
- [ ] Manual test: Navigate to a future month and verify if the budget from the current month was cloned.
- [ ] Manual test: Verify if switching months updates the values correctly on both screens.
