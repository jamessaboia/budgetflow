package com.jamessaboia.budgetflow.data.repository

import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.toDomain
import com.jamessaboia.budgetflow.data.local.toEntity
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.model.TransactionWithCategory
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val database: BudgetFlowDatabase
) : TransactionRepository {

    private val transactionDao = database.transactionDao()
    private val categoryDao = database.categoryDao()

    override fun getTransactionsByMonth(monthYear: String): Flow<List<TransactionWithCategory>> {
        return combine(
            transactionDao.getTransactionsByMonth(monthYear),
            categoryDao.getAllCategories()
        ) { transactionEntities, categoryEntities ->
            val categoryMap = categoryEntities.associateBy({ it.id }, { it.name })
            transactionEntities.map { entity ->
                TransactionWithCategory(
                    transaction = entity.toDomain(),
                    categoryName = categoryMap[entity.categoryId] ?: "Unknown"
                )
            }
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
