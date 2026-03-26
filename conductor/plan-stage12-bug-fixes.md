# Plan - Stage 12: Bug Fixes and UX Polish

## Objective
Fix user-reported bugs and UX issues before moving to the next major phase. This includes improving the monetary input field to support commas and auto-formatting, fixing the double percentage `%%` displaying in text, and standardizing the slider colors across budget groups.

## Scope & Impact
- **Monetary Input:** Modify `AddTransactionScreen` (and potentially `OnboardingScreen`/`BudgetSettingsScreen` if applicable) to better handle monetary input, replacing commas with dots for parsing, or implementing a visual currency mask.
- **String Resources:** Update `strings.xml` (all languages) to remove `%%` and replace with `%`. For strings that are not actually format strings but triggered lint warnings (like `hint_group_needs`), add `formatted="false"` to the XML tag to satisfy lint without displaying double percentages.
- **Slider Colors:** Update `PercentageSlider` in `OnboardingScreen` and `BudgetSettingsScreen` to use `MaterialTheme.colorScheme.primary` for the thumb and active track for all groups, instead of using secondary and tertiary colors.

## Implementation Steps

### 1. Fix Monetary Input Parsing
- In `AddTransactionViewModel`, `OnboardingViewModel`, and `BudgetSettingsViewModel`, update the parsing logic: `amount.replace(",", ".").toDoubleOrNull()`.
- (Optional but recommended) Implement a visual currency visual transformation or simply allow comma input. Replacing `,` with `.` on the ViewModel side is the most robust immediate fix for the parsing error.

### 2. Fix Double Percentages in Strings
- Search for all occurrences of `%%` in `res/values/strings.xml`, `res/values-en/strings.xml`, and `res/values-es/strings.xml`.
- For strings like `total_label` that use `String.format` (e.g., `%1$d%%`), keep `%%` because `String.format` consumes one `%`.
- For strings like `hint_group_needs` that do NOT use `String.format`, change `%%` back to `%` and add `formatted="false"` to the XML tag (e.g., `<string name="hint_group_needs" formatted="false">... 50% ...</string>`).

### 3. Standardize Slider Colors
- In `OnboardingScreen.kt` and `BudgetSettingsScreen.kt`, the `PercentageSlider` component accepts a `color` parameter.
- Pass `MaterialTheme.colorScheme.primary` for all three sliders (Needs, Wants, Savings) instead of primary, secondary, and tertiary.

## Verification & Testing
- Manual check: Type "15,50" in the transaction amount field and verify it saves correctly as 15.50.
- Manual check: Open the hint for "Essenciais" and ensure it says "50%" and not "50%%".
- Manual check: Look at the sliders in Settings and Onboarding and ensure all slider tracks and thumbs are the same green color.
- Run `./gradlew build` and `./gradlew lintDebug` to ensure no lint regressions.
