# Spec: Dashboard Visual Refinement & Privacy

## 1. Objective
Enhance the Dashboard's visual hierarchy, color consistency, and user privacy. This involves refining the header layout, standardizing financial colors, and implementing a visibility toggle for sensitive data.

## 2. Functional Requirements
- **Data Visibility Toggle:**
    - Add an "eye" icon (open/closed) to the Dashboard.
    - When closed: Available balance, Total Income, and Total Spent values are masked (e.g., "R$ ••••").
    - Persistent state: The app should remember if the user prefers values hidden or visible (using DataStore).
    - Use smooth animations when switching states.
- **Financial Color Standardization:**
    - All "Spent" values in Group Cards (Needs, Wants, Savings) must use the **Red** color from the app's palette.
- **Header Layout Refactoring:**
    - Within the primary green Balance Header card:
        - Place Income and Expenses into sub-cards.
        - Sub-cards background must match the app's general background color.
        - **Income sub-card:** Green text/icons representing gains (e.g., `ArrowUpward`).
        - **Expenses sub-card:** Red text/icons representing costs (e.g., `ArrowDownward`).

## 3. Technical Requirements
- **Persistence:** Update `UserPreferencesStore` to include a `isBalanceVisible` boolean.
- **UI State:** Include `isBalanceVisible` in `DashboardUiState`.
- **Theme:** Strictly use `MaterialTheme.colorScheme.primary` (Green) and `MaterialTheme.colorScheme.error` (Red).
- **Animations:** Use `AnimatedVisibility` or `Crossfade` for value transitions.

## 4. UI/UX Considerations
- Ensure sub-cards inside the green header have enough contrast and padding to look harmonious.
- The visibility toggle should be easily accessible, likely near the "Available Balance" title.

## 5. Acceptance Criteria
- [ ] User can toggle visibility of all financial values on the Dashboard.
- [ ] Spending values in group cards are red.
- [ ] Income and Expenses are displayed in distinct background-colored sub-cards within the green header.
- [ ] Visual icons clearly distinguish income from expenses.
- [ ] `./gradlew build` passes.
