package com.jamessaboia.budgetflow.ui.features.transactions

import com.jamessaboia.budgetflow.domain.model.TransactionWithCategory

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val transactions: List<TransactionWithCategory> = emptyList(),
    val error: String? = null
)
