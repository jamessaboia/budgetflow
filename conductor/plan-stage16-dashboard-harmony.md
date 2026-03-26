# Plan - Stage 16: Dashboard Color Harmony

## Objective
Update the colors of the `BalanceHeader` and the `TopAppBar` in `DashboardScreen.kt` to match the `FloatingActionButton` (Primary color) to create a more harmonious UI.

## Implementation Steps
1. Replace `MaterialTheme.colorScheme.secondary` with `MaterialTheme.colorScheme.primary` for the `TopAppBar` container color.
2. Replace `MaterialTheme.colorScheme.onSecondary` with `MaterialTheme.colorScheme.onPrimary` for the `TopAppBar` content color.
3. Replace `MaterialTheme.colorScheme.secondary` with `MaterialTheme.colorScheme.primary` for the `BalanceHeader` Card container color.
4. Replace `MaterialTheme.colorScheme.onSecondary` with `MaterialTheme.colorScheme.onPrimary` for the `BalanceHeader` content color.
5. Update `SummaryInfoItem` to use `onPrimary` instead of `onSecondary`.

## Verification
- Build and run the app to verify the color changes.
