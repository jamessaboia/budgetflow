package com.jamessaboia.budgetflow.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class MonthlyBudget(
    val id: Long = 0,
    val monthYear: String, 
    val baseIncome: Double,
    val extraIncome: Double = 0.0,
    val needsPercentage: Int = 50,
    val wantsPercentage: Int = 30,
    val savingsPercentage: Int = 20
) {
    val totalIncome: Double get() = baseIncome + extraIncome
}
