package com.jamessaboia.budgetflow.ui.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.model.TransactionWithCategory
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
    )
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    val uiState: StateFlow<TransactionsUiState> = _selectedMonth
        .flatMapLatest { monthYear ->
            transactionRepository.getTransactionsByMonth(monthYear)
        }
        .map { transactions ->
            TransactionsUiState(isLoading = false, transactions = transactions)
        }
        .catch { e ->
            emit(TransactionsUiState(isLoading = false, error = e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionsUiState(isLoading = true)
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

    private val _lastDeletedTransaction = MutableStateFlow<Transaction?>(null)

    private val _showUndoSnackbar = MutableSharedFlow<Unit>()
    val showUndoSnackbar: SharedFlow<Unit> = _showUndoSnackbar.asSharedFlow()

    fun deleteTransaction(transactionWithCategory: TransactionWithCategory) {
        viewModelScope.launch {
            _lastDeletedTransaction.value = transactionWithCategory.transaction
            transactionRepository.deleteTransaction(transactionWithCategory.transaction)
            _showUndoSnackbar.emit(Unit)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            _lastDeletedTransaction.value?.let { transaction ->
                transactionRepository.addTransaction(transaction)
                _lastDeletedTransaction.value = null
            }
        }
    }
}
