package com.jamessaboia.budgetflow.ui.features.transactions

import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.model.TransactionType
import java.util.Date

data class AddTransactionUiState(
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category? = null,
    val date: Date = Date(),
    val description: String = "",
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    val canSave: Boolean
        get() {
            val amountValue = amount.replace(",", ".").toDoubleOrNull() ?: 0.0
            return amountValue > 0 && selectedCategory != null && !isLoading
        }
}
