# Product Definition - BudgetFlow

## Overview
BudgetFlow is a personal finance app aimed at helping users manage their monthly income through a structured budget allocation (50/30/20 rule as a starting point). It provides a simple, local-first way to track income and expenses.

## Target Audience
Individuals looking for a pragmatic, structured way to allocate their income and track spending without complex financial tools.

## MVP Requirements (Stage 1-3)

### Functional Requirements
1. **Monthly Income Registration:**
    - Main salary (liquid).
    - Optional extra income.
    - Total monthly available calculation.
2. **Budget Configuration:**
    - Initial preset: 50% (Needs), 30% (Wants), 20% (Savings).
    - Editable percentages.
    - Other presets or customization.
3. **Budget Categories:**
    - **Needs:** Housing, Food, Transport, Bills, Health.
    - **Wants:** Leisure, Delivery, Subscriptions, Shopping.
    - **Savings:** Emergency fund, Investments, Goals.
4. **Manual Transaction Entry:**
    - Amount, Type (Income/Expense), Category, Date, Description (optional).
5. **Monthly Dashboard:**
    - Total income, total spent, remaining balance.
    - Spending per group (Needs, Wants, Savings).
    - Progress bars and remaining limits per group.
    - Visual alerts for limit exceedance.
6. **Simple Monthly History:**
    - View data for previous months.

### Non-Functional Requirements
- **Local-first:** Data stored in Room/DataStore.
- **Modern Android Stack:** Kotlin, Compose, MVVM + Clean, Hilt, Navigation.
- **UI/UX:** Clean, educational, and intuitive (Material 3).
- **Architecture:** Scalable for future backend sync.

## Future Scope (Post-MVP)
- Backend synchronization (Cloud backup).
- Shared accounts/budgets.
- More complex reporting and charts.
- Dark/Light mode customization.
