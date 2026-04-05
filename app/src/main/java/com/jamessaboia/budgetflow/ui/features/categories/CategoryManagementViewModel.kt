package com.jamessaboia.budgetflow.ui.features.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.usecase.CheckTransactionsBeforeDeleteUseCase
import com.jamessaboia.budgetflow.domain.usecase.DeleteCategoryUseCase
import com.jamessaboia.budgetflow.domain.usecase.SaveCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManagementViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val saveCategoryUseCase: SaveCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val checkTransactionsBeforeDeleteUseCase: CheckTransactionsBeforeDeleteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryManagementUiState())
    val uiState: StateFlow<CategoryManagementUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CategoryUiEffect>()
    val uiEffect: SharedFlow<CategoryUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        budgetRepository.getAllCategories()
            .onEach { categories ->
                _uiState.update { it.copy(categories = categories, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onAddCategory(name: String, description: String?, group: BudgetGroup) {
        viewModelScope.launch {
            val newCategory = Category(
                name = name,
                description = description,
                groupType = group,
                isDefault = false
            )
            saveCategoryUseCase(newCategory)
            _uiEffect.emit(CategoryUiEffect.ShowSuccessMessage)
        }
    }

    fun onEditCategory(category: Category, newName: String, newDescription: String?, newGroup: BudgetGroup) {
        viewModelScope.launch {
            val updatedCategory = category.copy(name = newName, description = newDescription, groupType = newGroup)
            saveCategoryUseCase(updatedCategory)
            _uiEffect.emit(CategoryUiEffect.ShowSuccessMessage)
        }
    }

    fun onDeleteClick(category: Category) {
        viewModelScope.launch {
            val hasTransactions = checkTransactionsBeforeDeleteUseCase(category.id)
            if (hasTransactions) {
                _uiState.update { it.copy(showDeleteWarning = category) }
            } else {
                deleteCategoryUseCase(category)
                _uiEffect.emit(CategoryUiEffect.ShowDeletedMessage)
            }
        }
    }

    fun confirmDelete(category: Category) {
        viewModelScope.launch {
            deleteCategoryUseCase(category)
            _uiState.update { it.copy(showDeleteWarning = null) }
            _uiEffect.emit(CategoryUiEffect.ShowDeletedMessage)
        }
    }

    fun dismissDeleteWarning() {
        _uiState.update { it.copy(showDeleteWarning = null) }
    }
}

sealed class CategoryUiEffect {
    object ShowSuccessMessage : CategoryUiEffect()
    object ShowDeletedMessage : CategoryUiEffect()
}
