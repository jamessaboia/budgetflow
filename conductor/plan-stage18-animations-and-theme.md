# Plan - Stage 18: Animated Splash, Theme Locking, and Premium Animations

## Objective
Enhance the app's initial impression and visual continuity by implementing an animated splash screen, locking the theme to a high-quality Light Mode (as per user preference), and introducing premium UI animations (animated numbers and screen transitions) using modern Jetpack Compose 2026 standards.

## Scope & Impact
- **Splash Screen:** Integrate `androidx.core:core-splashscreen` and add a scale/fade animation to the app icon.
- **Theme Locking:** Modify `Theme.kt` to permanently use the `lightScheme` and ignore system dark mode settings.
- **UI Animations:** 
    - Implement a "rolling number" animation for the available balance in `DashboardScreen`.
    - Implement standard fade/slide transitions for all screen changes in `AppNavigation`.
    - Apply `@Immutable` annotations to domain models to optimize animation performance.

## Implementation Steps

### 1. Animated Splash Screen
- Add `androidx.core:core-splashscreen` (v1.2.0-alpha01 or latest stable) to `libs.versions.toml` and `app/build.gradle.kts`.
- Update `res/values/themes.xml` to define `Theme.App.Starting` extending `Theme.SplashScreen`.
- Update `AndroidManifest.xml` to use the splash theme as the launcher activity theme.
- In `MainActivity`, call `installSplashScreen()` and use the `setOnExitAnimationListener` to animate the icon scaling and fading.

### 2. Lock to Light Mode
- In `app/src/main/java/com/jamessaboia/budgetflow/ui/theme/Theme.kt`:
    - Remove the `darkScheme` definition.
    - Update `BudgetFlowTheme` to always pass `lightScheme` to `MaterialTheme`.
    - Ensure `isSystemInDarkTheme()` is ignored.

### 3. Premium UI Animations
- **Animated Balance:**
    - In `DashboardScreen.kt`, wrap the `remainingBalance` text in an `AnimatedContent` or use `animateFloatAsState` to create a smooth transition when the balance updates.
- **Navigation Transitions:**
    - In `AppNavigation.kt`, add `enterTransition`, `exitTransition`, `popEnterTransition`, and `popExitTransition` to the `NavHost` to provide a cohesive slide/fade feel.
- **Performance Optimization:**
    - Add `androidx.compose.runtime.Immutable` annotation to `MonthlyBudget`, `Category`, and `Transaction` in the `domain/model/` package.

## Verification
- **Splash:** Cold start the app and verify the icon animates before the Dashboard/Onboarding appears.
- **Theme:** Switch the device to Dark Mode and verify the app remains in the "Premium Light" theme.
- **Animations:** Add/Delete a transaction and verify the balance numbers animate smoothly. Verify screen changes feel "fluid" rather than instant.
- Run `./gradlew build` to ensure no regressions.
