# Plan - Stage 10: Elite Presets & Budget Settings (Marco 7)

## Objective
Integrate real-world financial methodologies (Raul Sena & Bruno Perini) into the app and provide a settings interface where users can update their budget configuration (income and percentages) at any time without losing transaction data.

## Scope & Impact
- Define official preset names and educational descriptions in `strings.xml`.
- Update `OnboardingViewModel` and `OnboardingScreen` to use the new preset names and display contextual hints.
- Implement `BudgetSettingsViewModel` to manage existing budget updates.
- Create `BudgetSettingsScreen` to allow editing current month's income and percentages.
- Update `AppNavigation` and `DashboardScreen` to include a settings entry point.

## Implementation Steps

### 1. Update Resources (`strings.xml`)
- Add strings for:
    - "Raul Sena Method (50/30/20)" + Description.
    - "Bruno Perini Method (50/25/25)" + Description.
    - Educational hints for each budget group (Essentials, Lifestyle, Investments).

### 2. Refine Onboarding (`ui/features/onboarding/`)
- Update `PercentagesStep` to show the influencer names on the preset buttons.
- Display a "Hint Card" that changes based on the selected preset, explaining the philosophy behind it.

### 3. Implement Budget Settings (`ui/features/settings/`)
- Create `BudgetSettingsViewModel`:
    - Load current month's `MonthlyBudget`.
    - Handle updates to income and percentages.
    - Save changes back to `BudgetRepository`.
- Create `BudgetSettingsScreen.kt`:
    - Reuse logic/components from `OnboardingScreen` (Income and Percentage fields).
    - Add a "Save" button.

### 4. Navigation & UI Integration
- Add `Screen.Settings` route.
- Add a settings icon (Icons.Default.Settings) to `DashboardScreen` top bar.
- Link the icon to the new `BudgetSettingsScreen`.

## Verification & Testing
- Manual check: Verify that updating the budget in Settings immediately reflects on the Dashboard (e.g., limits and progress bars change).
- Data Integrity: Ensure transactions remain linked to the correct month after a budget update.
- Run `./gradlew build` to verify new dependencies and UI wiring.
