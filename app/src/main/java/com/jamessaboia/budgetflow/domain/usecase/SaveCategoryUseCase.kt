package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(category: Category) {
        budgetRepository.saveCategory(category)
    }
}
