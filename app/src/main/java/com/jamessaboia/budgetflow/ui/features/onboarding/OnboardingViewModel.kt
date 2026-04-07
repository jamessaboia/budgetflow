package com.jamessaboia.budgetflow.ui.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onIncomeChange(income: String) {
        _uiState.update { it.copy(baseIncome = income) }
    }

    fun onPercentagesChange(needs: Int, wants: Int, savings: Int) {
        _uiState.update { 
            it.copy(
                needsPercent = needs,
                wantsPercent = wants,
                savingsPercent = savings
            )
        }
    }

    fun nextStep() {
        _uiState.update { 
            val nextStep = when (it.step) {
                OnboardingStep.INTRO_SLIDER -> OnboardingStep.INCOME
                OnboardingStep.INCOME -> OnboardingStep.PERCENTAGES
                OnboardingStep.PERCENTAGES -> OnboardingStep.SUMMARY
                OnboardingStep.SUMMARY -> OnboardingStep.SUMMARY
                else -> OnboardingStep.INCOME
            }
            it.copy(step = nextStep)
        }
    }

    fun previousStep() {
        _uiState.update { 
            val prevStep = when (it.step) {
                OnboardingStep.INTRO_SLIDER -> OnboardingStep.INTRO_SLIDER
                OnboardingStep.INCOME -> OnboardingStep.INTRO_SLIDER
                OnboardingStep.PERCENTAGES -> OnboardingStep.INCOME
                OnboardingStep.SUMMARY -> OnboardingStep.PERCENTAGES
                else -> OnboardingStep.INTRO_SLIDER
            }
            it.copy(step = prevStep)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val incomeValue = _uiState.value.baseIncome.replace(",", ".").toDoubleOrNull() ?: 0.0
                val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)

                val initialBudget = MonthlyBudget(
                    monthYear = currentMonth,
                    baseIncome = incomeValue,
                    extraIncome = 0.0,
                    needsPercentage = _uiState.value.needsPercent,
                    wantsPercentage = _uiState.value.wantsPercent,
                    savingsPercentage = _uiState.value.savingsPercent
                )
                budgetRepository.saveBudget(initialBudget)
                
                saveDefaultCategories()
                budgetRepository.completeOnboarding()

                _uiState.update { it.copy(isLoading = false, isComplete = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private suspend fun saveDefaultCategories() {
        val defaults = listOf(
            Category(name = "cat_housing", description = "hint_housing", groupType = BudgetGroup.NEEDS, isDefault = true),
            Category(name = "cat_food", description = "hint_food", groupType = BudgetGroup.NEEDS, isDefault = true),
            Category(name = "cat_transport", description = "hint_transport", groupType = BudgetGroup.NEEDS, isDefault = true),
            Category(name = "cat_health", description = "hint_health", groupType = BudgetGroup.NEEDS, isDefault = true),
            Category(name = "cat_education", description = "hint_education", groupType = BudgetGroup.NEEDS, isDefault = true),
            Category(name = "cat_leisure", description = "hint_leisure", groupType = BudgetGroup.WANTS, isDefault = true),
            Category(name = "cat_lifestyle", description = "hint_lifestyle", groupType = BudgetGroup.WANTS, isDefault = true),
            Category(name = "cat_shopping", description = "hint_shopping", groupType = BudgetGroup.WANTS, isDefault = true),
            Category(name = "cat_subscriptions", description = "hint_subscriptions", groupType = BudgetGroup.WANTS, isDefault = true),
            Category(name = "cat_emergency", description = "hint_emergency", groupType = BudgetGroup.SAVINGS, isDefault = true),
            Category(name = "cat_investments", description = "hint_investments", groupType = BudgetGroup.SAVINGS, isDefault = true),
            Category(name = "cat_goals", description = "hint_goals", groupType = BudgetGroup.SAVINGS, isDefault = true)
        )
        defaults.forEach { budgetRepository.saveCategory(it) }
    }
}
