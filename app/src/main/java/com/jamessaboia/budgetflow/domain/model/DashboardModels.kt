package com.jamessaboia.budgetflow.domain.model

data class GroupSummary(
    val limit: Double,
    val spent: Double,
    val percentageSpent: Float, // 0..1
    val remaining: Double
)

data class DashboardSummary(
    val totalIncome: Double,
    val totalSpent: Double,
    val remainingBalance: Double,
    val needsSummary: GroupSummary,
    val wantsSummary: GroupSummary,
    val savingsSummary: GroupSummary
)
