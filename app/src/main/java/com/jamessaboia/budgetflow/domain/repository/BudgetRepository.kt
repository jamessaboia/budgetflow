package com.jamessaboia.budgetflow.domain.repository

import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetByMonth(monthYear: String): Flow<MonthlyBudget?>
    suspend fun getLatestBudgetBefore(monthYear: String): MonthlyBudget?
    suspend fun saveBudget(budget: MonthlyBudget)
    
    fun getAllCategories(): Flow<List<Category>>
    suspend fun saveCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun completeOnboarding()
    suspend fun toggleBalanceVisibility(visible: Boolean)
}
