# Spec: UX Polish & Bug Fixes

## 1. Objective
Refine the user experience by implementing visual masks for currency inputs, improving navigation visibility, and ensuring robust feedback through reactive UI states. This track also focuses on complete internationalization and consistency.

## 2. Functional Requirements
- **Currency Mask:**
    - Standardize `CurrencyVisualTransformation` for all monetary fields.
    - Implement real-time masking in `AddTransactionScreen`, `BudgetSettingsScreen`, and `OnboardingScreen`.
- **Reactive "Save" Logic:**
    - In `AddTransactionScreen`, the "Save" button must be **disabled** until all required fields are filled:
        - `amount > 0`
        - `category != null`
    - This replaces the error snackbar for missing fields, providing proactive guidance.
- **System UI Visibility:**
    - Fix the bottom navigation bar visibility. Set the background to the app's primary green color (`0xFF3C6939` or theme primary) and ensure icons/text are clearly visible.
- **Feedback & Snackbars:**
    - Implement a Snackbar confirmation with an "Undo" option after deleting a transaction.
    - Ensure all user-facing feedback messages are persistent and correctly displayed on every trigger.
- **Full Internationalization (I18n):**
    - Audit all strings in the project.
    - Ensure 100% coverage for Portuguese (PT), English (EN), and Spanish (ES).
    - Specifically fix the "Value must be greater than zero" (and similar) translations.

## 3. Technical Requirements
- **UI State:** Add a `canSave` property or logic to `AddTransactionUiState` (calculated in the ViewModel or Screen).
- **Theme:** Adjust `enableEdgeToEdge` and `Scaffold` container colors to ensure the bottom bar matches the primary brand color.
- **Resources:** Update `strings.xml` in `values`, `values-en`, and `values-es`.

## 4. UI/UX Considerations
- Proactive validation (disabling buttons) is preferred over reactive validation (showing errors after clicks).
- Color contrast for the bottom navigation must meet accessibility standards.

## 5. Acceptance Criteria
- [ ] Save button in Add Transaction is disabled if fields are incomplete.
- [ ] Bottom navigation bar is visible with a solid primary color background.
- [ ] All monetary inputs show the currency mask correctly.
- [ ] Deleting a transaction shows a confirmed Snackbar.
- [ ] No hardcoded strings; all translations present for PT, EN, and ES.
- [ ] `./gradlew build` passes.
