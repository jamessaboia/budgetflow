# Plan - Stage 21: Performance Snappiness and Global System UI

## Objective
Remove perceived lag when saving transactions and ensure consistent global styling for the System Status Bar and Navigation Bar icons.

## Scope & Impact
- **AddTransactionScreen:** Remove blocking snackbar call to allow instant navigation back to the Dashboard.
- **TopAppBar Colors:** Update `AddTransactionScreen`, `TransactionsScreen`, and `BudgetSettingsScreen` to use `Primary` container colors, matching the Dashboard and ensuring the Status Bar background is consistent globally.
- **MainActivity:** Refine `enableEdgeToEdge` to ensure light icons on the Status Bar (since background will be dark green) and dark icons on the Navigation Bar.

## Implementation Steps

### 1. Instant Navigation in AddTransaction
- In `AddTransactionScreen.kt`, update the `isSuccess` LaunchedEffect:
    - Remove `snackbarHostState.showSnackbar`.
    - Call `onBack()` immediately without any delay or blocking call.

### 2. Global TopAppBar Standardization
- In `AddTransactionScreen.kt`, `TransactionsScreen.kt`, and `BudgetSettingsScreen.kt`:
    - Add `colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)` to the `TopAppBar`.

### 3. System Bar Icon Contrast
- In `MainActivity.kt`, update `enableEdgeToEdge`:
    - Use `SystemBarStyle.dark` for the Status Bar (to force light icons on our green background).
    - Use `SystemBarStyle.light` for the Navigation Bar (to force dark icons on the white background).

## Verification
- **Speed:** Verify that clicking "Save Transaction" returns to the Dashboard instantly.
- **Visuals:** Verify the Status Bar is Green on ALL screens.
- **Contrast:** Verify that system icons (WiFi, Battery, Back/Home buttons) are clearly visible on both bars.
