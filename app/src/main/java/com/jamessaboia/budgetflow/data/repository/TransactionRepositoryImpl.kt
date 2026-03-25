package com.jamessaboia.budgetflow.data.repository

import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.toDomain
import com.jamessaboia.budgetflow.data.local.toEntity
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val database: BudgetFlowDatabase
) : TransactionRepository {

    private val transactionDao = database.transactionDao()

    override fun getTransactionsByMonth(monthYear: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByMonth(monthYear).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    override fun getTotalExpensesByMonth(monthYear: String): Flow<Double> {
        return transactionDao.getTotalExpensesByMonth(monthYear).map { it ?: 0.0 }
    }

    override fun getTotalIncomeByMonth(monthYear: String): Flow<Double> {
        return transactionDao.getTotalIncomeByMonth(monthYear).map { it ?: 0.0 }
    }
}
