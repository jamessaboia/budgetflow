# Plan - Stage 6: Feature Transactions (Marco 5)

## Objective
Implement the complete transaction management flow in the BudgetFlow app. This stage focuses on allowing the user to add new income or expenses and view a clear history of their transactions for the current month.

## Scope & Impact
- Create `AddTransactionViewModel` to handle input validation and persistence.
- Implement `AddTransactionScreen` for user entry (Value, Type, Category, Date, Description).
- Create `TransactionsViewModel` to manage the list of transactions.
- Implement `TransactionsScreen` to display the history of the current month.
- Update `AppNavigation` with new routes for adding and listing transactions.
- Integrate a Floating Action Button (FAB) on the Dashboard to quickly navigate to "Add Transaction".

## Implementation Steps

### 1. ViewModels & UI States (`ui/features/transactions/`)
- **AddTransaction:**
    - Define `AddTransactionUiState` (loading, available categories, validation errors, success).
    - Implement `AddTransactionViewModel`:
        - Fetch categories from `BudgetRepository`.
        - Handle user inputs for transaction fields.
        - Logic to save the transaction through `TransactionRepository`.
- **TransactionsList:**
    - Define `TransactionsUiState` (loading, list of transactions, error).
    - Implement `TransactionsViewModel`:
        - Use `TransactionRepository` to observe transactions for the current month.

### 2. UI Components (`ui/features/transactions/`)
- Create `AddTransactionScreen.kt`:
    - Form with `OutlinedTextField` for value and description.
    - Radio buttons or Segmented Buttons for Income/Expense selection.
    - Dropdown menu for Category selection.
    - Date picker (Material 3) for choosing the transaction date.
- Create `TransactionsScreen.kt`:
    - List with `LazyColumn` showing transaction items (formatted date, category name, amount, type).
    - Group items by date for better readability.

### 3. Navigation & Dashboard Updates
- Update `NavRoutes.kt` with `AddTransaction` and `Transactions` routes.
- Update `AppNavigation.kt` to include the new destinations.
- Modify `DashboardScreen.kt` to:
    - Add a Floating Action Button (FAB) that navigates to the `AddTransaction` screen.
    - Add a button or navigation item to view the full transaction history.

### 4. Use Case (Optional/Pragmatic)
- If complex sorting or filtering is needed, create a `GetTransactionsByMonthUseCase` in the Domain layer. For the MVP, direct repository usage is acceptable.

## Verification & Testing
- Unit Test for `AddTransactionViewModel` to verify input validation (e.g., cannot save with 0 amount).
- Manual UI check: Ensure added transactions immediately reflect on the Dashboard totals.
- Run `./gradlew build` to verify all new UI and Hilt dependencies are correctly wired.
