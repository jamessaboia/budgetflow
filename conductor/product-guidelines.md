# BudgetFlow - Product Guidelines

## Core Product Vision
BudgetFlow is a didactic, robust, and scalable MVP. It should empower users to understand and manage their finances using the 50/30/20 rule without overwhelming them with unnecessary complexity.

## UI/UX Principles
1. **Material Design 3 (M3):** Use standard M3 components (Cards, FABs, TopAppBar) provided by Jetpack Compose.
2. **Clarity over Clutter:** Keep screens focused. A screen should have a primary action and a clear hierarchy of information.
3. **Didactic Hints:** Every major feature should include educational hints. 
   - **Pattern:** "Definition + Practical Tip". 
   - *Example:* "Needs (50%): Essential expenses. Tip: Try negotiating fixed bills like internet or phone plans."
4. **Visual Feedback:** Use standard Compose animations and state changes to provide feedback for user actions.
5. **Consistency:** Standardize paddings, corner radiuses, and typography using the Compose Theme (`Theme.kt`, `Type.kt`).
6. **Error States:** Gracefully handle errors and empty states (e.g., "No transactions yet. Tap + to add one.").

## Engineering Conventions
- **Naming:** Use clear, intention-revealing names.
- **Components:** Create stateless, reusable UI components for common elements (e.g., standard buttons, input fields).
- **Validation:** Validate inputs early (e.g., in the ViewModel) and provide clear error messages to the user.
