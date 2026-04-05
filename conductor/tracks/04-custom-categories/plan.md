# Plan: Custom Category Management (Track 04)

## Step 1: Data Layer & Mandatory Unit Testing
- [ ] **Repository Update:** Ensure `BudgetRepository` and its implementation support `deleteCategory` and `updateCategory`.
- [ ] **CategoryDao Tests:** Create `CategoryDaoTest.kt` in `androidTest` (or `test` with Robolectric) to verify CRUD operations:
    - Insert and Retrieve categories.
    - Update category name and group.
    - Delete category.
- [ ] **Repository Tests:** Create `BudgetRepositoryTest.kt` to verify the logic of category management, mocking the DAO.

## Step 2: Safety & Business Logic (Domain)
- [ ] **CheckTransactionsBeforeDeleteUseCase:** Create a UseCase to check if a category is being used by any transaction before allowing deletion.
- [ ] **Update Category UseCases:** Ensure we have clean UseCases for adding and editing categories if they don't exist yet.

## Step 3: UI Implementation (Management Screen)
- [ ] **ViewModel:** Create `CategoryManagementViewModel` to manage the list of categories and handling CRUD events.
- [ ] **Screen:** Create `CategoryManagementScreen` using:
    - `LazyColumn` with headers for each `BudgetGroup` (Needs, Wants, Savings).
    - FloatingActionButton to add a new category.
    - Swipe-to-delete or action buttons for edit/delete.
- [ ] **Dialogs:** Implement "Add/Edit Category" dialogs with validation (name cannot be empty).

## Step 4: Navigation & Integration
- [ ] **Routes:** Register the new category management route in `NavRoutes.kt` and `AppNavigation.kt`.
- [ ] **Settings Integration:** Add a button in `BudgetSettingsScreen` to navigate to the Category Management screen.

## Step 5: Final Verification
- [ ] **Run Unit Tests:** Execute all new and existing tests (`./gradlew test`).
- [ ] **Run Build:** Execute `./gradlew build`.
- [ ] **Manual Test:** Create a custom category, use it in a transaction, and try to delete it (verify safety check).
