# Plan - Stage 9: Branding & Multi-language Support (Marco 6)

## Objective
Update the visual identity of BudgetFlow with a modern "Digital Trust" color palette and implement full internationalization support for Portuguese (Brazil), English, and Spanish. This stage ensures the app looks professional and is accessible to a global audience.

## Scope & Impact
- Update `ui/theme/Color.kt` with the new hex codes.
- Refine `ui/theme/Theme.kt` to apply the new palette to Material 3 Light/Dark schemes.
- Extract all hardcoded strings from UI screens to `strings.xml`.
- Create `values-en/strings.xml` (English) and `values-es/strings.xml` (Spanish).
- Update the "Welcome" string to be more inclusive of different budget methods.

## Implementation Steps

### 1. Visual Refinement (`ui/theme/`)
- Define "Digital Trust" colors in `Color.kt`:
    - `MidnightNavy`: `#101828` (Primary Container/Background Dark)
    - `DigitalBlue`: `#2E90FA` (Primary)
    - `GrowthMint`: `#66C285` (Secondary/Success)
    - `IceGray`: `#F9FAFB` (Background Light)
- Update `Theme.kt` to use these colors in `lightColorScheme` and `darkColorScheme`.

### 2. String Extraction & Translation (`res/values/`)
- Identify all hardcoded strings in `OnboardingScreen`, `DashboardScreen`, `AddTransactionScreen`, and `TransactionsScreen`.
- Populate `res/values/strings.xml` (Default/PT-BR).
- Create `res/values-en/strings.xml` and translate.
- Create `res/values-es/strings.xml` and translate.
- **Specific Change:** Update welcome message to: *"Organize sua renda com métodos comprovados pelos maiores educadores financeiros."*

### 3. UI Refactoring
- Replace all hardcoded `Text("...")` with `stringResource(R.string....)`.
- Ensure layouts are flexible enough for different text lengths (especially in Spanish).

## Verification & Testing
- Manual visual check: Ensure the new colors have high contrast and look professional.
- Language test: Switch system language to English/Spanish and verify that all UI elements update correctly.
- Run `./gradlew build` to ensure no resource references are broken.
