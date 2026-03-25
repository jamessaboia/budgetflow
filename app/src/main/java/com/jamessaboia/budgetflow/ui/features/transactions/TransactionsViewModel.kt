package com.jamessaboia.budgetflow.ui.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)

    val uiState: StateFlow<TransactionsUiState> = transactionRepository.getTransactionsByMonth(currentMonth)
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

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }
}
