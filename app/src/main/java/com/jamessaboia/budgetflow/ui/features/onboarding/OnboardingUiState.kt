package com.jamessaboia.budgetflow.ui.features.onboarding

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.INTRO_SLIDER,
    val baseIncome: String = "",
    val extraIncome: String = "",
    val needsPercent: Int = 50,
    val wantsPercent: Int = 30,
    val savingsPercent: Int = 20,
    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val error: String? = null
)

enum class OnboardingStep {
    INTRO_SLIDER,
    INCOME,
    PERCENTAGES,
    SUMMARY
}
