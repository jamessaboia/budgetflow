package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import javax.inject.Inject

class CheckTransactionsBeforeDeleteUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    /**
     * Returns true if the category has transactions linked to it.
     */
    suspend operator fun invoke(categoryId: Long): Boolean {
        return transactionRepository.getTransactionCountByCategory(categoryId) > 0
    }
}
