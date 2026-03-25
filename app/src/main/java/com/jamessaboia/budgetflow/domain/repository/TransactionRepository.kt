package com.jamessaboia.budgetflow.domain.repository

import com.jamessaboia.budgetflow.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsByMonth(monthYear: String): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTotalExpensesByMonth(monthYear: String): Flow<Double>
    fun getTotalIncomeByMonth(monthYear: String): Flow<Double>
}
