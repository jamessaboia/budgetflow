# Plan - Stage 19: System UI and Visual Polish

## Objective
Address final visual inconsistencies: make system navigation bar buttons visible against the light background and ensure Dashboard card shadows are fully rendered without being cut off by the list boundaries.

## Scope & Impact
- **System Navigation Bar:** Update `MainActivity.kt` to explicitly configure the system bars for light mode, ensuring icons (Back, Home, Recents) are dark and visible.
- **Card Shadows:** Refactor `DashboardScreen.kt` and `TransactionsScreen.kt` to use `contentPadding` and `clipToPadding = false` in `LazyColumn`s. This prevents the `Modifier.padding` from clipping the elevation shadows of the cards.

## Implementation Steps

### 1. Fix System Navigation Bar Visibility
- In `MainActivity.kt`, update `enableEdgeToEdge()`:
    - Import `androidx.activity.SystemBarStyle`.
    - Call `enableEdgeToEdge` with `statusBarStyle` and `navigationBarStyle` set to `SystemBarStyle.light`. This forces the system to use high-contrast icons for our Light theme.

### 2. Fix Cut-off Shadows in Dashboard
- In `DashboardScreen.kt`:
    - Locate `DashboardContent` and `DashboardSkeleton`.
    - Remove `.padding(16.dp)` from the `LazyColumn` modifier.
    - Add `contentPadding = PaddingValues(16.dp)` to the `LazyColumn` parameters.
    - Add `clipToPadding = false` to the `LazyColumn` parameters.

### 3. Fix Cut-off Shadows in Transactions
- In `TransactionsScreen.kt`:
    - Locate `TransactionsSkeleton`.
    - Remove `.padding(16.dp)` (if present) or ensure it uses the same `contentPadding` + `clipToPadding = false` pattern.
    - Note: The real `TransactionsScreen` list already uses `contentPadding`, but might need `clipToPadding = false`.

## Verification
- **System UI:** Verify the bottom navigation buttons are now dark/visible.
- **Shadows:** Inspect the `GroupCard`s in the Dashboard; the bottom shadow should now be soft and fully visible.
- Run `./gradlew build` to verify.
