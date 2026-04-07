# Spec: Cash Flow Realignment

## 1. Objective
Refine how the app calculates and displays the "Available Balance" to reflect real cash flow (money in/money out) while maintaining budget goals based on monthly planning. This supports users with multiple income dates (bi-weekly) or variable income.

## 2. Functional Requirements
- **Real Balance Logic:** 
    - The "Available Balance" displayed on the Dashboard must be calculated as: `(Total INCOME Transactions) - (Total EXPENSE Transactions)` for the selected month.
    - This replaces the current logic which uses the static income from settings.
- **Budget Goal Persistence:**
    - The limits for the 50/30/20 groups (Needs, Wants, Savings) will **remain** based on the planned income defined in Settings (`baseIncome + extraIncome`).
    - This allows users to plan their whole month even before all money is received.
- **Educational Hints:**
    - Add a specific hint explaining the difference between "Cash Flow" (money you have now) and "Budget" (your spending plan).
    - Pattern: "Definition + Practical Tip".

## 3. Technical Requirements
- **Domain Layer:**
    - Update `GetDashboardSummaryUseCase` to calculate `totalIncome` from transactions instead of settings.
    - Keep `plannedIncome` from settings to calculate group limits.
- **Data Layer:**
    - Ensure `TransactionRepository` correctly sums all income transactions for a specific month.
- **UI Layer:**
    - Update Dashboard cards to reflect these distinct values.
    - Add an information icon or hint text near the balance to educate the user.

## 4. UI/UX Considerations
- Clearly distinguish (visually or via hints) that the Balance is what the user *has*, while the bars are what the user *can* spend.
- The transition should be seamless for existing users.

## 5. Acceptance Criteria
- [ ] Available balance matches the sum of income transactions minus expenses.
- [ ] Group limits (50/30/20) remain stable based on settings.
- [ ] A new educational hint about Cash Flow is implemented.
- [ ] All unit tests pass (Golden Rule applies).
- [ ] `./gradlew build` passes.
