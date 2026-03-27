package com.jamessaboia.budgetflow.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BudgetSettingsViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetSettingsUiState())
    val uiState: StateFlow<BudgetSettingsUiState> = _uiState.asStateFlow()

    private val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
    private var currentBudgetId: Long = 0
    private var initialSavedState: BudgetSettingsUiState? = null

    init {
        loadCurrentBudget()
    }

    private fun loadCurrentBudget() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            budgetRepository.getBudgetByMonth(currentMonth).first()?.let { budget ->
                currentBudgetId = budget.id
                val loadedState = BudgetSettingsUiState(
                    baseIncome = budget.baseIncome.toString(),
                    extraIncome = budget.extraIncome.toString(),
                    needsPercent = budget.needsPercentage,
                    wantsPercent = budget.wantsPercentage,
                    savingsPercent = budget.savingsPercentage,
                    isLoading = false
                )
                initialSavedState = loadedState
                _uiState.value = loadedState
            } ?: run {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onBaseIncomeChange(value: String) {
        _uiState.update { 
            it.copy(baseIncome = value).validateDirty(initialSavedState)
        }
    }

    fun onExtraIncomeChange(value: String) {
        _uiState.update { 
            it.copy(extraIncome = value).validateDirty(initialSavedState)
        }
    }

    fun onPercentagesChange(needs: Int, wants: Int, savings: Int) {
        _uiState.update {
            it.copy(
                needsPercent = needs,
                wantsPercent = wants,
                savingsPercent = savings
            ).validateDirty(initialSavedState)
        }
    }

    fun resetUpdateSuccess() {
        _uiState.update { it.copy(isUpdateSuccess = false) }
    }

    private fun BudgetSettingsUiState.validateDirty(initial: BudgetSettingsUiState?): BudgetSettingsUiState {
        if (initial == null) return this.copy(isDirty = true)
        val hasChanges = baseIncome != initial.baseIncome ||
                extraIncome != initial.extraIncome ||
                needsPercent != initial.needsPercent ||
                wantsPercent != initial.wantsPercent ||
                savingsPercent != initial.savingsPercent
        return this.copy(isDirty = hasChanges)
    }

    fun saveChanges() {
        val incomeValue = _uiState.value.baseIncome.replace(",", ".").toDoubleOrNull() ?: 0.0
        val extraIncomeValue = _uiState.value.extraIncome.replace(",", ".").toDoubleOrNull() ?: 0.0
        
        if (incomeValue <= 0) {
            _uiState.update { it.copy(error = "Renda deve ser maior que zero") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val updatedBudget = MonthlyBudget(
                    id = currentBudgetId,
                    monthYear = currentMonth,
                    baseIncome = incomeValue,
                    extraIncome = extraIncomeValue,
                    needsPercentage = _uiState.value.needsPercent,
                    wantsPercentage = _uiState.value.wantsPercent,
                    savingsPercentage = _uiState.value.savingsPercent
                )
                budgetRepository.saveBudget(updatedBudget)
                
                // Update initial state to the new saved state
                val newState = _uiState.value.copy(isLoading = false, isUpdateSuccess = true, isDirty = false)
                initialSavedState = newState.copy(isUpdateSuccess = false)
                _uiState.value = newState
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
