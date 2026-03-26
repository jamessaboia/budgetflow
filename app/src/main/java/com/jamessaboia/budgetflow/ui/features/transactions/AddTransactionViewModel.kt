package com.jamessaboia.budgetflow.ui.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.model.TransactionType
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        budgetRepository.getAllCategories()
            .onEach { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
            .launchIn(viewModelScope)
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(type = type) }
    }

    fun onCategoryChange(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onDateChange(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun saveTransaction() {
        val amountValue = _uiState.value.amount.replace(",", ".").toDoubleOrNull() ?: 0.0
        val categoryId = _uiState.value.selectedCategory?.id
        
        if (amountValue <= 0) {
            _uiState.update { it.copy(error = "Valor deve ser maior que zero") }
            return
        }
        
        if (categoryId == null) {
            _uiState.update { it.copy(error = "Selecione uma categoria") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val monthYear = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(_uiState.value.date)
                
                val transaction = Transaction(
                    amount = amountValue,
                    type = _uiState.value.type,
                    categoryId = categoryId,
                    description = _uiState.value.description,
                    date = _uiState.value.date,
                    monthYear = monthYear
                )
                
                transactionRepository.addTransaction(transaction)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
