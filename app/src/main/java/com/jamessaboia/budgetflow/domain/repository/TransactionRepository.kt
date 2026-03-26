package com.jamessaboia.budgetflow.domain.repository

import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsByMonth(monthYear: String): Flow<List<TransactionWithCategory>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTotalExpensesByMonth(monthYear: String): Flow<Double>
    fun getTotalIncomeByMonth(monthYear: String): Flow<Double>
}
