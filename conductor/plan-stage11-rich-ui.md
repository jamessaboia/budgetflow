# Plan - Stage 11: Rich UI & Educational Details (Marco 8)

## Objective
Enhance the user experience with detailed financial insights and educational context. This stage focuses on making the Dashboard more granular (spending by category), improving the Transaction History with category names, and adding contextual educational hints during transaction entry.

## Scope & Impact
- Update `DashboardSummary` and `GetDashboardSummaryUseCase` to include per-category spending data.
- Refactor `DashboardScreen` to implement expandable group cards.
- Update `TransactionRepository` and `TransactionsViewModel` to provide transactions with their associated category names.
- Enhance `TransactionsScreen` to display category names for each entry.
- Implement contextual "Financial Literacy" hints in `AddTransactionScreen` when a category is selected.
- Add a set of more comprehensive default categories based on Raul Sena and Bruno Perini's philosophies.
- (Optional/Bonus) Implement a simple "Add Category" dialog.

## Implementation Steps

### 1. Granular Dashboard Logic (`domain/model/` & `domain/usecase/`)
- Update `GroupSummary` to include a list of `CategorySpent` (name, amount).
- Update `GetDashboardSummaryUseCase` to calculate spending for each category within its group.

### 2. Expandable UI in Dashboard (`ui/features/dashboard/`)
- Modify `GroupCard` to use `AnimatedVisibility` for showing/hiding category details.
- Add a toggle button (expand icon) to each group card.

### 3. Category Names in History (`domain/repository/` & `ui/features/transactions/`)
- Create a `TransactionWithCategory` domain model.
- Update `TransactionRepository` to return `Flow<List<TransactionWithCategory>>`.
- Update `TransactionsScreen` UI to show the category name alongside the description.

### 4. Contextual Hints in Transaction Entry (`ui/features/transactions/`)
- Define informative strings for each default category in `strings.xml`.
- In `AddTransactionScreen`, display a small informative card or text below the category selector describing what typically fits in the selected category.

### 5. Expand Default Categories
- Add more default categories during onboarding: "Education", "Health", "Subscriptions", "Emergency Fund", "Opportunities", etc.
- Link them to the correct `BudgetGroup`.

## Verification & Testing
- Manual check: Verify that clicking a group in the Dashboard reveals the correct categories and their individual totals.
- Manual check: Verify that transaction history now shows which category each expense belongs to.
- Run `./gradlew build` to ensure no UI regressions.
