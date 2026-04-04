package com.jamessaboia.budgetflow.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val ensureBudgetForMonthUseCase: EnsureBudgetForMonthUseCase
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
    )
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    val uiState: StateFlow<DashboardUiState> = _selectedMonth
        .onEach { monthYear ->
            ensureBudgetForMonthUseCase(monthYear)
        }
        .flatMapLatest { monthYear ->
            getDashboardSummaryUseCase(monthYear)
        }
        .map { summary ->
            DashboardUiState(isLoading = false, summary = summary)
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
