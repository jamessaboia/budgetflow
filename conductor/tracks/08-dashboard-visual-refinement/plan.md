# Plan: Dashboard Visual Refinement & Privacy (Track 08)

## Step 1: Persistence & Data Layer
- [ ] **UserPreferencesStore:**
    - Add `balanceVisible` key to DataStore.
    - Update `UserPreferences` data class.
    - Add function to toggle balance visibility.
- [ ] **BudgetRepository:**
    - Expose `balanceVisible` preference.
    - Add method `toggleBalanceVisibility(visible: Boolean)`.

## Step 2: ViewModel Integration
- [ ] **DashboardUiState:** Add `isBalanceVisible: Boolean`.
- [ ] **DashboardViewModel:**
    - Observe the visibility preference from the repository.
    - Combine it into the `uiState`.
    - Add `toggleBalanceVisibility()` method.

## Step 3: UI Implementation - Balance Header
- [ ] **Visibility Masking:** Create a helper to mask currency strings (e.g., "R$ ••••").
- [ ] **BalanceHeader Refactor:**
    - Add the eye icon (Icons.Default.Visibility / VisibilityOff) next to the "Available Balance" label.
    - Update the main balance text to show masked values if `!isBalanceVisible`.
    - Create a Row containing two `Card`s (one for Income, one for Expenses).
    - **Sub-cards style:** `containerColor = MaterialTheme.colorScheme.background`.
    - **Income Card:** Green arrow icon + Green "Income" text.
    - **Expenses Card:** Red arrow icon + Red "Spent" text.

## Step 4: UI Implementation - Group Cards
- [ ] **GroupCard Update:**
    - Change the text color of the `spent` value to `MaterialTheme.colorScheme.error` (Red).
    - Ensure it also respects the `isBalanceVisible` state (masking the value if hidden).

## Step 5: Animations & Polish
- [ ] Use `AnimatedContent` for the value transitions (masked vs unmasked).
- [ ] Ensure all colors align with the existing `Theme.kt` definitions.

## Step 6: Final Verification
- [ ] Run `./gradlew build`.
- [ ] Manual test: Toggle visibility and verify if it persists after app restart.
- [ ] Manual test: Verify colors and icons in the new header layout.
