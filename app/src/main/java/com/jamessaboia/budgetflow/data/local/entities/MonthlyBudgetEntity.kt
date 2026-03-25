package com.jamessaboia.budgetflow.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budgets")
data class MonthlyBudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthYear: String,
    val baseIncome: Double,
    val extraIncome: Double,
    val needsPercentage: Int,
    val wantsPercentage: Int,
    val savingsPercentage: Int
)
