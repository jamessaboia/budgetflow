package com.jamessaboia.budgetflow.domain.model

data class TransactionWithCategory(
    val transaction: Transaction,
    val categoryName: String
)
