# Plan - Stage 5: Feature Dashboard (Marco 4)

## Objective
Implement the main Dashboard screen of the BudgetFlow app. This stage focuses on calculating and visualizing the monthly budget progress, displaying the total income, expenses, and remaining balance for each budget group (50/30/20).

## Scope & Impact
- Define `DashboardSummary` domain model to hold all calculated values.
- Implement `GetDashboardSummaryUseCase` to encapsulate the business logic of joining budgets and transactions.
- Create `DashboardViewModel` to expose the UI state and handle any dashboard-related events.
- Build the `DashboardScreen` using Jetpack Compose with custom progress indicators and cards.
- Integrate with `AppNavigation` to make the Dashboard the main entry point after onboarding.

## Implementation Steps

### 1. Domain Layer (`domain/model/` & `domain/usecase/`)
- Define `GroupSummary` data class: `limit`, `spent`, `percentageSpent`, `remaining`.
- Define `DashboardSummary` data class: `totalIncome`, `totalSpent`, `remainingBalance`, `needsSummary`, `wantsSummary`, `savingsSummary`.
- Implement `GetDashboardSummaryUseCase`:
    - Fetch the current `MonthlyBudget`.
    - Fetch all `Transaction`s for the current month.
    - Fetch all `Category`s.
    - Calculate spent amounts per `BudgetGroup`.
    - Return a `DashboardSummary` flow.

### 2. ViewModel & State (`ui/features/dashboard/`)
- Define `DashboardUiState` (loading, summary data, error).
- Implement `DashboardViewModel`:
    - Use the `GetDashboardSummaryUseCase` to keep the UI updated in real-time.
    - Format values for the UI.

### 3. UI Components (`ui/features/dashboard/`)
- Create `DashboardScreen.kt`:
    - Header with total income/balance.
    - Progress cards for each group (Needs, Wants, Savings).
    - Progress bars (LinearProgressIndicator) with color alerts (e.g., red if limit exceeded).
- Reuse components or create new ones for group progress visualization.

### 4. Navigation & MainActivity
- Replace the placeholder in `AppNavigation` with the real `DashboardScreen`.

## Verification & Testing
- Unit Test for `GetDashboardSummaryUseCase` to ensure calculations (group totals, percentages) are correct.
- Manual UI check for visual feedback (e.g., color changes when spending reaches certain thresholds).
- Run `./gradlew build` to verify Hilt and Room interactions.
