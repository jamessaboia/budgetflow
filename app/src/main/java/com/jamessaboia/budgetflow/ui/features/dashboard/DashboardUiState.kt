package com.jamessaboia.budgetflow.ui.features.dashboard

import com.jamessaboia.budgetflow.domain.model.DashboardSummary

data class DashboardUiState(
    val isLoading: Boolean = true,
    val summary: DashboardSummary? = null,
    val error: String? = null
)
