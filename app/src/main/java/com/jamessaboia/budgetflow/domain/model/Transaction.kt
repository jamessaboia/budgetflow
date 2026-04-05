package com.jamessaboia.budgetflow.domain.model

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val description: String = "",
    val date: Date = Date(),
    val monthYear: String 
)
