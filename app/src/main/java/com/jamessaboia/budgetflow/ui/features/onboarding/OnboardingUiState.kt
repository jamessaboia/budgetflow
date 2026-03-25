package com.jamessaboia.budgetflow.ui.features.onboarding

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.WELCOME,
    val baseIncome: String = "",
    val needsPercent: Int = 50,
    val wantsPercent: Int = 30,
    val savingsPercent: Int = 20,
    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val error: String? = null
)

enum class OnboardingStep {
    WELCOME,
    INCOME,
    PERCENTAGES,
    SUMMARY
}
