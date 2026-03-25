package com.jamessaboia.budgetflow.domain.model

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val description: String = "",
    val date: Date = Date(),
    val monthYear: String // Format: "yyyy-MM" to easy link with MonthlyBudget
)
