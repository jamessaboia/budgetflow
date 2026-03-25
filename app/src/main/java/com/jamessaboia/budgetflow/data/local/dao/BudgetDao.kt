package com.jamessaboia.budgetflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamessaboia.budgetflow.data.local.entities.MonthlyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM monthly_budgets WHERE monthYear = :monthYear LIMIT 1")
    fun getBudgetByMonth(monthYear: String): Flow<MonthlyBudgetEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: MonthlyBudgetEntity)

    @Query("DELETE FROM monthly_budgets WHERE monthYear = :monthYear")
    suspend fun deleteBudgetByMonth(monthYear: String)
}
