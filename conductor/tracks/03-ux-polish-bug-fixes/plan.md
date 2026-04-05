# Plan: UX Polish & Bug Fixes (Track 03)

## Step 1: Internationalization (I18n)
- [x] **Audit Strings:** Checked `strings.xml` for core keys.
- [x] **Translate:** Added `transaction_deleted`, `undo`, and navigation strings to PT, EN, and ES.
- [ ] **Audit Remaining:** Check if any toast or validation message in ViewModels is still hardcoded.

## Step 2: System UI & Navigation
- [x] **MainActivity Adjustment:** Updated `enableEdgeToEdge` with primary color.
- [x] **NavigationBarSpacer:** Integrated as a custom component in all main screens via Scaffold.
- [x] **Theme Polish:** Updated `BudgetFlowTheme` with `SideEffect` for window colors.

## Step 3: Proactive Validation (Add Transaction)
- [x] **AddTransactionViewModel:** Implemented `canSave` logic.
- [x] **AddTransactionScreen:** Disabled "Save" button based on state.
- [x] **Cleanup:** Removed redundant error snackbars for basic validation.

## Step 4: Currency Mask (All Inputs)
- [x] **CurrencyVisualTransformation:** Integrated into AddTransaction, Settings, and Onboarding screens.
- [ ] **Data Parsing Audit:** Double-check `OnboardingViewModel` and `BudgetSettingsViewModel` for robust `Double` parsing (handling dots and commas consistently).

## Step 5: Deletion Feedback
- [x] **TransactionsViewModel:** Implemented undo logic and `SharedFlow` for effects.
- [x] **TransactionsScreen:** Implemented Snackbar with Undo action.

## Step 6: Final Verification
- [ ] Run `./gradlew build`.
- [ ] Manual verification of all UI polish items.
