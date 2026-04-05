package com.jamessaboia.budgetflow.domain.model

data class CategorySpent(
    val categoryName: String,
    val amount: Double
)

data class GroupSummary(
    val limit: Double,
    val spent: Double,
    val percentageSpent: Float, 
    val remaining: Double,
    val categorySpending: List<CategorySpent> = emptyList()
)

data class DashboardSummary(
    val totalIncome: Double,
    val totalSpent: Double,
    val remainingBalance: Double,
    val needsSummary: GroupSummary,
    val wantsSummary: GroupSummary,
    val savingsSummary: GroupSummary
)
