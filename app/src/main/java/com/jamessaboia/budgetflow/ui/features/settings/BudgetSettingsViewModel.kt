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

    init {
        loadCurrentBudget()
    }

    private fun loadCurrentBudget() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            budgetRepository.getBudgetByMonth(currentMonth).first()?.let { budget ->
                currentBudgetId = budget.id
                _uiState.update {
                    it.copy(
                        baseIncome = budget.baseIncome.toString(),
                        extraIncome = budget.extraIncome.toString(),
                        needsPercent = budget.needsPercentage,
                        wantsPercent = budget.wantsPercentage,
                        savingsPercent = budget.savingsPercentage,
                        isLoading = false
                    )
                }
            } ?: run {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onBaseIncomeChange(value: String) {
        _uiState.update { it.copy(baseIncome = value) }
    }

    fun onExtraIncomeChange(value: String) {
        _uiState.update { it.copy(extraIncome = value) }
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

    fun saveChanges() {
        val incomeValue = _uiState.value.baseIncome.toDoubleOrNull() ?: 0.0
        val extraIncomeValue = _uiState.value.extraIncome.toDoubleOrNull() ?: 0.0
        
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
                _uiState.update { it.copy(isLoading = false, isUpdateSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
