# Plan - Stage 13: Monetary Input Auto-Formatting (Visual Mask)

## Objective
Implement an automatic monetary formatting mask (currency mask) for all income and transaction amount fields. As the user types, the field should automatically add thousands separators (e.g., "5000" becomes "5.000") to improve readability and follow financial app standards.

## Scope & Impact
- Create a `CurrencyVisualTransformation` class in the `core/` package.
- Apply the transformation to `AddTransactionScreen`, `OnboardingScreen` (IncomeStep), and `BudgetSettingsScreen`.
- Ensure the input remains easy to edit (cursor position handling).
- Maintain compatibility with the existing parsing logic in ViewModels.

## Implementation Steps

### 1. Create Currency Transformation (`core/`)
- Implement `CurrencyVisualTransformation`:
    - It will take the raw input string.
    - Format it using `NumberFormat` for the current locale (supporting dots for thousands and commas for decimals in PT-BR).
    - Implement the `OffsetMapping` to ensure the cursor moves correctly when separators are added.

### 2. Update AddTransactionScreen
- Add `visualTransformation = CurrencyVisualTransformation()` to the "Valor" `OutlinedTextField`.
- Ensure `onValueChange` only accepts digits and a single decimal separator.

### 3. Update OnboardingScreen & BudgetSettingsScreen
- Apply the same `visualTransformation` to the "Renda Principal" and "Renda Extra" fields.

### 4. Verification
- Manual check: Type "1000000" and ensure it displays as "1.000.000" (or "1,000,000" depending on locale).
- Manual check: Verify that deleting characters works as expected and doesn't "jump" the cursor.
- Verify that saving still works (parsing logic should ignore the visual separators).

## Alternative Approach
If `VisualTransformation` becomes too complex for the current stage, use a simpler "State-based" formatting in the ViewModel where we format the string immediately on input. However, `VisualTransformation` is the preferred "Modern Android" way.
