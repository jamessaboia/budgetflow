# Plan: UX Polish & Bug Fixes (Track 03)

## Step 1: Internationalization (I18n)
- [ ] **Audit Strings:** Check all `strings.xml` files for completeness.
- [ ] **Translate:** Add missing keys to `values/strings.xml`, `values-en/strings.xml`, and `values-es/strings.xml`. 
    - Keys: `error_amount_zero`, `error_select_category`, `transaction_deleted`, `undo`, `currency_prefix`.
- [ ] **Verify:** Ensure no hardcoded strings remain in Composable files.

## Step 2: System UI & Navigation
- [ ] **MainActivity Adjustment:** Update `enableEdgeToEdge` to use the primary green color for both status and navigation bars.
- [ ] **Theme Polish:** Ensure `BudgetFlowTheme` sets the `navigationBarColor` correctly if necessary.
- [ ] **Check Layouts:** Verify if any `Scaffold` or `Surface` is overriding the navigation bar color.

## Step 3: Proactive Validation (Add Transaction)
- [ ] **AddTransactionViewModel:** Add a logic to determine if the transaction can be saved (amount > 0 and category selected).
- [ ] **AddTransactionScreen:** Update the "Save" button `enabled` property to use this logic.
- [ ] **Cleanup:** Remove the error snackbar logic for empty fields, as the button will be disabled.

## Step 4: Currency Mask (All Inputs)
- [ ] **CurrencyVisualTransformation:** Ensure the transformation is robust for BRL and other locales.
- [ ] **Implementation:** 
    - Apply to `AddTransactionScreen`'s amount field.
    - Apply to `BudgetSettingsScreen`'s income fields.
    - Apply to `OnboardingScreen`'s income fields.
- [ ] **Data Parsing:** Verify that all ViewModels correctly parse the masked input back to `Double`.

## Step 5: Deletion Feedback
- [ ] **TransactionsViewModel:** Add a `deleteTransactionWithUndo` logic or a state/effect to notify the UI.
- [ ] **TransactionsScreen:** Observe the effect and show a Snackbar with an "Undo" action.

## Step 6: Final Verification
- [ ] Run `./gradlew build`.
- [ ] Manual test: Verify the disabled button state.
- [ ] Manual test: Verify navigation bar visibility.
- [ ] Manual test: Check all translations by switching system language.
