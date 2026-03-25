package com.jamessaboia.budgetflow.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val groupType: BudgetGroup,
    val budgetLimit: Double? = null,
    val isDefault: Boolean = false
)
