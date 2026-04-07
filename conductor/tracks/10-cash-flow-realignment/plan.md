# Plan: Cash Flow Realignment (Track 10)

## Step 1: Model & Domain Update
- [ ] **DashboardModels:** Update `DashboardSummary` to include:
    - `actualIncome: Double` (Sum of income transactions).
    - `plannedIncome: Double` (Base + Extra from settings).
- [ ] **GetDashboardSummaryUseCase:** 
    - Update logic: Use `transactionRepository.getTotalIncomeByMonth(monthYear)` for `actualIncome`.
    - Use `budget.baseIncome + budget.extraIncome` for `plannedIncome`.
    - `remainingBalance` = `actualIncome - totalSpent`.
    - Group limits = `plannedIncome * (percentage / 100)`.

## Step 2: Mandatory Unit Testing
- [ ] **GetDashboardSummaryUseCaseTest:** Create/Update tests to verify:
    - Balance calculation uses actual transactions.
    - Group limits use planned income from settings.
- [ ] **TransactionRepositoryTest:** Ensure `getTotalIncomeByMonth` is fully covered.

## Step 3: UI Implementation
- [ ] **Strings:** Add new strings for the Cash Flow educational hint.
    - `hint_cash_flow_title`: "Fluxo de Caixa vs. Orçamento"
    - `hint_cash_flow_desc`: "O saldo mostra o dinheiro que você realmente tem. As metas são baseadas no seu planejamento mensal. Dica: Monitore suas entradas para não gastar o que ainda não recebeu!"
- [ ] **DashboardScreen:**
    - Update `BalanceHeader` to display the new `actualIncome` and calculated balance.
    - Integrate the new info icon/hint near the Available Balance title.
    - Ensure 50/30/20 cards still use planned limits.

## Step 4: Verification
- [ ] Run all unit tests (`./gradlew test`).
- [ ] Run `./gradlew build`.
- [ ] Manual verification:
    - Add an income transaction and see the balance increase.
    - Check if the 50/30/20 bar limits remain unchanged (matching settings).
