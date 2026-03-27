package com.jamessaboia.budgetflow.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
    val id: Long = 0,
    val name: String,
    val groupType: BudgetGroup,
    val budgetLimit: Double? = null,
    val isDefault: Boolean = false
)
