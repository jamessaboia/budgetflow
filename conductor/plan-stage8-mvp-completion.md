# Plan - Stage 8: Complete MVP Requirements

## Objective
Address the remaining requirements from the initial product scope that were not fully implemented during the onboarding flow: adding an optional extra income field and making budget percentages fully editable with quick preset buttons.

## Scope & Impact
- Update `OnboardingUiState` and `OnboardingViewModel` to handle `extraIncome`.
- Update `OnboardingScreen` (IncomeStep) to include an optional text field for extra income.
- Update `OnboardingScreen` (PercentagesStep) to include preset buttons (50/30/20, 60/20/20, 50/20/30) and sliders/steppers for custom percentage editing.
- Ensure the total percentage validation (must equal 100%) before allowing the user to proceed.
- Update `SummaryStep` to display the total income (base + extra) correctly.

## Implementation Steps

### 1. ViewModels & UI States
- Add `extraIncome: String` to `OnboardingUiState`.
- Add `onExtraIncomeChange(income: String)` to `OnboardingViewModel`.
- Modify `completeOnboarding` to parse `extraIncome` and save it to the `MonthlyBudget`.

### 2. UI Components - Income Step
- Add a second `OutlinedTextField` in `IncomeStep` for "Renda Extra (Opcional)".

### 3. UI Components - Percentages Step
- Add a `Row` with `FilterChip` or `Button` for the presets: "50/30/20", "60/20/20", "50/20/30".
- Add `Slider` components for Needs, Wants, and Savings.
- Add a text indicator showing the current total percentage (e.g., "Total: 100%").
- Disable the "Next" button if the sum of percentages is not exactly 100.

### 4. Summary & Verification
- Update `SummaryStep` to calculate the final values using `baseIncome + extraIncome`.
- Run `./gradlew build` to ensure no regressions.
