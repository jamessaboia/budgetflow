# Spec: Month Navigation & Persistence

## 1. Objective
Allow the user to navigate between different months on the Dashboard and Transactions screens. Additionally, ensure that when a new month is accessed, the budget configuration from the previous month is automatically inherited to provide a seamless user experience.

## 2. Functional Requirements
- **Month Picker:** Implement a visual selector (Previous/Next month) on the Dashboard and Transactions screens.
- **Dynamic Data Loading:** Update the Dashboard summary and the Transaction list based on the selected month.
- **Budget Inheritance:** When the user navigates to a month that doesn't have a budget yet:
    - Automatically check if the previous month has a budget.
    - If yes, copy the `baseIncome`, `extraIncome`, and `percentages` to the new month.
    - If no, prompt the user or use system defaults (50/30/20).
- **History Access:** The user should be able to view their spending history and remaining limits for any previous month.

## 3. Technical Requirements
- **Architecture:** 
    - Update `GetDashboardSummaryUseCase` to accept a `monthYear` (String: "yyyy-MM") parameter.
    - Update `TransactionsViewModel` and `DashboardViewModel` to manage the currently selected month state.
- **Persistence:** Use the existing `MonthlyBudgetEntity` and `BudgetRepository` to handle the cloning logic.
- **Format:** Standardize the month/year format across the app as "yyyy-MM" for database keys and "MMMM yyyy" (e.g., "Abril 2026") for display.

## 4. UI/UX Considerations
- The Month Picker should be placed at the top of the screens (below or inside the TopAppBar).
- Use smooth transitions when switching months.
- Ensure the "Inheritance" logic happens in the background to avoid blocking the UI.

## 5. Acceptance Criteria
- [ ] User can switch to the previous/next month on the Dashboard.
- [ ] Dashboard values (income, spent, remaining) update correctly according to the selected month.
- [ ] Transactions screen shows only transactions for the selected month.
- [ ] Navigating to a "future" month for the first time automatically copies the configuration from the most recent previous budget.
- [ ] `./gradlew build` passes without errors.
