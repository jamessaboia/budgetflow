# Plan - Stage 4: Feature Onboarding (Marco 3)

## Objective
Implement the initial onboarding flow of the BudgetFlow app. This stage focuses on capturing the user's base income, allowing customization of the 50/30/20 budget division, and saving these configurations locally (both in Room and DataStore).

## Scope & Impact
- Create `OnboardingViewModel` to manage state (UDF) and persistence logic.
- Implement the `OnboardingScreen` using Jetpack Compose and Material 3 components.
- Setup basic navigation to handle the transition from Onboarding to Dashboard.
- Provide default categories during the onboarding completion.
- Integrate with `BudgetRepository` to save the initial state.

## Implementation Steps

### 1. ViewModel & State (`ui/features/onboarding/`)
- Define `OnboardingUiState` (loading, income, percentages, success).
- Implement `OnboardingViewModel` with StateFlow:
    - Handle user input for base income and custom percentages.
    - Logic to complete onboarding: save percentages to DataStore and create the first `MonthlyBudget` in Room.
    - Logic to pre-populate default categories (Rent, Food, Transport, etc.).

### 2. UI Components (`ui/features/onboarding/`)
- Create `OnboardingScreen.kt`:
    - Screen 1: Welcome & Base Income input.
    - Screen 2: Budget Division adjustment (Visual sliders/inputs).
    - Screen 3: Summary and Confirmation.
- Use Material 3 `OutlinedTextField`, `Button`, and `Slider` (if applicable).

### 3. Navigation Setup (`navigation/`)
- Define `NavRoutes` and `Onboarding` destination.
- Implement `AppNavigation.kt` to orchestrate routes.
- Create a `MainActivity` that decides whether to show Onboarding or Dashboard based on DataStore.

### 4. Use Case (Optional/Pragmatic)
- If the logic for "Complete Onboarding" grows too large (categories + budget + preference), create a `CompleteOnboardingUseCase` in the Domain layer.

## Verification & Testing
- Unit Test for `OnboardingViewModel` to verify state transitions and persistence calls.
- Manual UI check for input validation (income must be positive, percentages must sum to 100%).
- Run `./gradlew build` to ensure the new UI and Hilt dependencies are correctly wired.
