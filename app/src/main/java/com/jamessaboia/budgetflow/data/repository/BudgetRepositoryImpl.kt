package com.jamessaboia.budgetflow.data.repository

import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.UserPreferences
import com.jamessaboia.budgetflow.data.local.UserPreferencesStore
import com.jamessaboia.budgetflow.data.local.toDomain
import com.jamessaboia.budgetflow.data.local.toEntity
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val database: BudgetFlowDatabase,
    private val preferencesStore: UserPreferencesStore
) : BudgetRepository {

    private val budgetDao = database.budgetDao()
    private val categoryDao = database.categoryDao()

    override fun getBudgetByMonth(monthYear: String): Flow<MonthlyBudget?> {
        return budgetDao.getBudgetByMonth(monthYear).map { it?.toDomain() }
    }

    override suspend fun getLatestBudgetBefore(monthYear: String): MonthlyBudget? {
        return budgetDao.getLatestBudgetBefore(monthYear)?.toDomain()
    }

    override suspend fun saveBudget(budget: MonthlyBudget) {
        budgetDao.insertBudget(budget.toEntity())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    override fun getUserPreferences(): Flow<UserPreferences> {
        return preferencesStore.userPreferencesFlow
    }

    override suspend fun completeOnboarding() {
        preferencesStore.updateOnboardingComplete(true)
    }

    override suspend fun toggleBalanceVisibility(visible: Boolean) {
        preferencesStore.updateBalanceVisible(visible)
    }
}
