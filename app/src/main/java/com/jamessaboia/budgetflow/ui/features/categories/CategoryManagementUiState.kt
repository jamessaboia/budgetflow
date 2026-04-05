package com.jamessaboia.budgetflow.ui.features.categories

import com.jamessaboia.budgetflow.domain.model.Category

data class CategoryManagementUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteWarning: Category? = null
)
