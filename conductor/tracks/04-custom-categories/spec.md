# Spec: Custom Category Management

## 1. Objective
Allow the user to fully customize their budget by creating, editing, or deleting their own categories. This replaces the rigid predefined category structure and allows users to adapt the app to their specific financial reality.

## 2. Functional Requirements
- **Category List Screen:** A new screen in "Settings" to view all current categories grouped by "Needs", "Wants", and "Savings".
- **Add Category:**
    - User provides a name.
    - User selects one of the 3 budget groups.
    - User can optionally set a specific budget limit for the category (future scope refinement).
- **Edit Category:**
    - User can rename any category.
    - User can move a category between budget groups.
- **Delete Category:**
    - User can delete custom categories.
    - **Safety Rule:** If a category has transactions linked to it, the user must be warned or prevented from deleting (or the transactions must be moved to an "Uncategorized" state).
- **Default Categories:**
    - System default categories should be editable/deletable or at least hideable to ensure full customization.

## 3. Technical Requirements
- **Data Layer:** 
    - Ensure `CategoryDao` and `BudgetRepository` support all CRUD operations.
    - **Unit Testing:** Implement mandatory tests for `CategoryDao` and `BudgetRepositoryImpl` regarding category management.
- **UI Layer:**
    - New feature: `ui/features/categories/CategoryManagementScreen`.
    - Use `UiState` and `UiEvent` for the management flow.
- **Architecture:** 
    - Maintain Clean Architecture and UDF.
    - No business logic in the UI.

## 4. UI/UX Considerations
- Group categories visually by their `BudgetGroup`.
- Use consistent colors for each group (Needs/Wants/Savings).
- Provide immediate feedback (Snackbar) for creation/deletion.

## 5. Acceptance Criteria
- [ ] User can view a organized list of all categories.
- [ ] User can add a new category to a specific group.
- [ ] User can edit an existing category name and group.
- [ ] User can delete a category.
- [ ] **Unit tests** for Category DAO and Repository must pass with 100% success.
- [ ] `./gradlew build` passes.
