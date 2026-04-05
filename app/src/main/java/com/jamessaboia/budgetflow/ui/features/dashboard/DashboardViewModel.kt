package com.jamessaboia.budgetflow.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.usecase.EnsureBudgetForMonthUseCase
import com.jamessaboia.budgetflow.domain.usecase.GetDashboardSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val ensureBudgetForMonthUseCase: EnsureBudgetForMonthUseCase,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
    )
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    private val _summaryFlow = _selectedMonth
        .onEach { monthYear ->
            ensureBudgetForMonthUseCase(monthYear)
        }
        .flatMapLatest { monthYear ->
            getDashboardSummaryUseCase(monthYear)
        }

    private val _userPreferencesFlow = budgetRepository.getUserPreferences()

    val uiState: StateFlow<DashboardUiState> = combine(
        _summaryFlow,
        _userPreferencesFlow
    ) { summary, preferences ->
        DashboardUiState(
            isLoading = false,
            summary = summary,
            isBalanceVisible = preferences.isBalanceVisible
        )
    }
    .catch { e ->
        emit(DashboardUiState(isLoading = false, error = e.message))
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun onPreviousMonth() {
        navigateMonth(-1)
    }

    fun onNextMonth() {
        navigateMonth(1)
    }

    fun toggleBalanceVisibility() {
        viewModelScope.launch {
            budgetRepository.toggleBalanceVisibility(!uiState.value.isBalanceVisible)
        }
    }

    private fun navigateMonth(delta: Int) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                val current = _selectedMonth.value.split("-")
                set(Calendar.YEAR, current[0].toInt())
                set(Calendar.MONTH, current[1].toInt() - 1)
                add(Calendar.MONTH, delta)
            }
            _selectedMonth.value = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
        }
    }
}
