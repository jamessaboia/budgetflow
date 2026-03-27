# Plan - Stage 17: Skeleton Loading Implementation

## Objective
Implement professional skeleton loading (shimmer effect) in high-traffic data reading screens: `DashboardScreen` and `TransactionsScreen`. The skeleton should follow the app's custom color palette, providing a smoother transition from loading to data presentation than a standard circular progress indicator.

## Scope & Impact
- Add the `com.valentinilk.shimmer:compose-shimmer` dependency (industry standard for 2026 Jetpack Compose apps).
- Create a reusable `SkeletonBox` composable or modifiers aligned with the app's `surfaceVariant` or `surfaceContainerHigh` colors for a cohesive look.
- Update `DashboardScreen` to display a skeleton version of the Balance Header and Group Cards while `uiState.isLoading` is true.
- Update `TransactionsScreen` to display a list of skeleton transaction items while `uiState.isLoading` is true.

## Implementation Steps

### 1. Add Dependencies
- Update `gradle/libs.versions.toml` to include `compose-shimmer` by Valentinilk (v1.3.3).
- Apply the dependency in `app/build.gradle.kts`.

### 2. Implement Skeleton UI in Dashboard
- In `DashboardScreen.kt`, when `isLoading` is true, wrap the content in a `shimmer()` modifier block (provided by the library).
- Create skeleton placeholders for:
  - `BalanceHeader` (a card with a gray box for the total and smaller boxes for the sub-values).
  - `GroupCard`s (3 placeholder cards with boxes where the text and progress bars would be).

### 3. Implement Skeleton UI in Transactions
- In `TransactionsScreen.kt`, when `isLoading` is true, display a `LazyColumn` containing 5-6 `TransactionItemSkeleton` components wrapped in a `shimmer()` modifier.
- Ensure the skeletons mimic the shape of the real `TransactionItem` (date, category, amount boxes).

## Verification
- Temporarily add a delay (e.g., `delay(2000)`) in the ViewModels to manually observe the shimmer effect.
- Ensure the shimmer colors do not conflict with the dark/light mode palette.
- Run `./gradlew build` to verify dependencies.
