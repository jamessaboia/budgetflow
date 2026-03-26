package com.jamessaboia.budgetflow.ui.features.settings

data class BudgetSettingsUiState(
    val baseIncome: String = "",
    val extraIncome: String = "",
    val needsPercent: Int = 50,
    val wantsPercent: Int = 30,
    val savingsPercent: Int = 20,
    val isLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val error: String? = null
)
