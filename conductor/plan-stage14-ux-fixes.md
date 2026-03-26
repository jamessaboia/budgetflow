# Plan - Stage 14: Final UX Fixes

## Objective
Address specific user feedback to polish the final MVP experience. This includes fixing the comma input issue for monetary values, renaming the savings group for clarity, and adding contextual educational hints to the Dashboard's group cards.

## Scope & Impact
- **Monetary Input:** Fix the input filter in `AddTransactionScreen`, `OnboardingScreen`, and `BudgetSettingsScreen` to correctly process commas entered from the keyboard.
- **Strings Update:** Change "Reserva" to "Reserva / Investimentos" in `strings.xml`.
- **Dashboard Hints:** Update `DashboardScreen.kt` to pass hint strings to `GroupCard`. Add an interactive Info icon to the cards that toggles the display of the educational hint.

## Implementation Steps

### 1. Fix Decimal Input Filter
- In all monetary `OutlinedTextField`s, update the `onValueChange` lambda:
  ```kotlin
  val normalized = input.replace(",", ".")
  val filtered = normalized.filterIndexed { index, char ->
      char.isDigit() || (char == '.' && normalized.indexOf('.') == index)
  }
  ```

### 2. Rename Savings Group
- In `res/values/strings.xml`, update `group_savings` to "Reserva / Investimentos".
- Update `group_savings_summary` to "Reserva / Investimentos (%1$d%%)".
- (Optional) Update English and Spanish files for consistency if needed, though the request specifically addresses the PT-BR interface.

### 3. Add Hints to Dashboard Cards
- Modify `GroupCard` signature in `DashboardScreen.kt` to accept a `hint: String` parameter.
- Add `var showHint by remember { mutableStateOf(false) }`.
- Add an `IconButton` with `Icons.Default.Info` next to the card title.
- Show the `hint` text conditionally when `showHint` is true.
- Pass the appropriate hints (`R.string.hint_group_needs`, etc.) from the `DashboardContent` composable.

## Verification
- Test inputting "26,90" and verify it is retained.
- Verify the Dashboard shows the info icons and displays the text properly.
- Run `./gradlew build` to ensure no syntax errors.
