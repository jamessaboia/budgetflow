# Plan - Stage 20: UX Refinement and Visual Excellence

## Objective
Polishing the final visual and interactive details of the MVP. This includes making card shadows more prominent and visible, increasing the agility of the transaction saving flow, and improving the budget settings UX with "dirty state" detection and repeatable feedback.

## Scope & Impact
- **Dashboard UI:** Increase `GroupCard` elevation and ensure shadows are fully rendered on the background.
- **Transaction Flow:** Reduce navigation delay after saving a transaction from 1500ms to 500ms for a more "snappy" feel.
- **Settings UX:**
    - Update `BudgetSettingsViewModel` to track the initial saved state.
    - Enable the "Save" button only when changes are detected AND percentages sum to 100%.
    - Implement a reset mechanism for the success state to allow consecutive save feedbacks.

## Implementation Steps

### 1. Prominent Shadows in Dashboard
- In `DashboardScreen.kt`, update `GroupCard`:
    - Increase `defaultElevation` from `2.dp` to `4.dp` or `6.dp`.
    - Consider using `ElevatedCard` instead of a generic `Card`.

### 2. Snap Navigation in AddTransaction
- In `AddTransactionScreen.kt`, find the `LaunchedEffect` monitoring `uiState.isSuccess`.
- Change `kotlinx.coroutines.delay(1500)` to `delay(500)`. This provides a brief visual confirmation without making the app feel slow.

### 3. Advanced Settings UX
- **ViewModel (`BudgetSettingsViewModel.kt`):**
    - Add an `initialState: BudgetSettingsUiState?` variable.
    - Update `loadCurrentBudget` to store this initial state.
    - Add a `isDirty` computed property or state that compares current UI state with the initial state.
    - Add `resetUpdateSuccess()` function to set `isUpdateSuccess` to false.
- **UI (`BudgetSettingsScreen.kt`):**
    - Update the "Save" button `enabled` logic to: `total == 100 && uiState.isDirty && !uiState.isLoading`.
    - Call `viewModel.resetUpdateSuccess()` after the Snackbar is shown.

## Verification
- **Shadows:** Visually confirm that `GroupCard`s appear elevated with a clear shadow on the bottom.
- **Agility:** Verify that the "Add Transaction" screen returns to the Dashboard significantly faster.
- **Settings:**
    - Verify "Save" button is disabled when you first enter Settings.
    - Change a value -> Button enables.
    - Save -> Button disables and Snackbar appears.
    - Change again -> Button enables again.
