package com.jamessaboia.budgetflow.ui.features.transactions

import com.jamessaboia.budgetflow.domain.model.Transaction

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null
)
