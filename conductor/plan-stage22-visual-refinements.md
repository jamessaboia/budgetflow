# Plan - Stage 22: Visual Consistency and Animated Branding

## Objective
Standardize the global Status Bar color, refine Dashboard scrolling behavior, ensure high-contrast navigation bar icons, and add animated app branding to the onboarding flow.

## Scope & Impact
- **Status Bar:** Ensure it remains `Primary` green globally, but make `TopAppBar` transparent on all screens except the Dashboard.
- **Dashboard Scrolling:** Refactor `DashboardContent` so only the `BalanceHeader` is fixed; "Meus Grupos" and cards should scroll together.
- **Navigation Bar:** Force dark icons on the bottom navigation bar for better visibility.
- **App Icon Integration:** Add the `ic_launcher_foreground` icon to the "Welcome" and "All set!" onboarding steps with a subtle scale animation.

## Implementation Steps

### 1. Global Status Bar & Navigation Bar Style
- In `MainActivity.kt`:
    - Set `statusBarStyle` to `SystemBarStyle.dark(0xFF3C6939.toInt())` (using the actual hex for `primaryLight`). This ensures the Status Bar is always Green with White icons.
    - Set `navigationBarStyle` to `SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)` to force dark icons on the bottom bar.

### 2. Transparent TopAppBars
- In `OnboardingScreen.kt`, `AddTransactionScreen.kt`, `TransactionsScreen.kt`, and `BudgetSettingsScreen.kt`:
    - Update `TopAppBar` colors: set `containerColor` to `Color.Transparent` (or `MaterialTheme.colorScheme.background`).
    - Only `DashboardScreen.kt` will keep the `Primary` container color for its `LargeTopAppBar`.

### 3. Dashboard Scroll Refinement
- In `DashboardScreen.kt` (DashboardContent):
    - Move the "Meus Grupos" `Text` and `Spacer` inside the `LazyColumn` as an `item`.
    - Only the `BalanceHeader` remains outside the `LazyColumn` (or as a fixed element above it).

### 4. Animated App Icon in Onboarding
- In `OnboardingScreen.kt`:
    - Create an `AnimatedAppIcon` composable using `rememberInfiniteTransition` for a subtle pulse (scale) effect.
    - Insert this composable above the titles in `WelcomeStep` and `SummaryStep`.

## Verification
- Verify the Status Bar is Green on all screens.
- Verify Onboarding/Settings toolbars are transparent.
- Verify "Meus Grupos" scrolls with the cards in the Dashboard.
- Verify bottom nav buttons are dark and visible.
- Verify the app icon pulses nicely on the first and last onboarding steps.
