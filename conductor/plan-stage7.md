# Plan - Stage 7: Final Refinements

## Objective
Polishing the BudgetFlow app by addressing UI/UX details, fixing all compiler warnings related to deprecated APIs, and ensuring the codebase strictly follows the latest Android 2026 standards. This stage turns a functional MVP into a high-quality, professional product.

## Scope & Impact
- Fix all `hiltViewModel` deprecated imports across `OnboardingScreen`, `DashboardScreen`, `AddTransactionScreen`, and `TransactionsScreen`.
- Update icons like `ArrowBack` and `List` to their `AutoMirrored` counterparts for better accessibility and RTL support.
- Refine `AddTransactionScreen` to use the non-deprecated `menuAnchor` overload and improve error feedback (Snackbars).
- Ensure consistent spacing and typography throughout the app.
- Final code cleanup and verification against `GEMINI.md` mandates.

## Implementation Steps

### 1. Fix Deprecations & Warnings
- **Icons:** In `DashboardScreen`, `AddTransactionScreen`, and `TransactionsScreen`, replace `Icons.Default.ArrowBack` with `Icons.AutoMirrored.Filled.ArrowBack` and `Icons.Default.List` with `Icons.AutoMirrored.Filled.List`.
- **Hilt ViewModel:** Update imports to the non-deprecated package `androidx.hilt.navigation.compose.hiltViewModel` or the one recommended in the latest build logs.
- **Exposed Dropdown Menu:** Update the `menuAnchor` implementation in `AddTransactionScreen` to the modern overload with `ExposedDropdownMenuAnchorType`.

### 2. UI/UX Refinements
- **Error Feedback:** Implement `SnackbarHost` in `Scaffold` for `AddTransactionScreen` to show errors instead of just a static text, providing better visual feedback.
- **Empty States:** Ensure `TransactionsScreen` has a more visually appealing "empty state" illustration or icon when no transactions exist.
- **Dashboard Polish:** Adjust the color of the "Remaining Balance" to be more prominent and improve the contrast of progress indicators.

### 3. Final Verification
- Run `./gradlew lint` (if applicable) and `./gradlew build` to ensure 0 warnings and 0 errors.
- Manual verification of the onboarding-to-dashboard flow one last time.

## Verification & Testing
- Final Build validation: `./gradlew build`.
- Verify that all screens are responsive and accessible.
- Ensure that the Hilt dependency graph remains clean and efficient.
