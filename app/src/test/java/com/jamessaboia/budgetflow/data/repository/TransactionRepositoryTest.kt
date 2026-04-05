package com.jamessaboia.budgetflow.data.repository

import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.dao.TransactionDao
import com.jamessaboia.budgetflow.domain.model.Transaction
import com.jamessaboia.budgetflow.domain.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class TransactionRepositoryTest {

    private lateinit var repository: TransactionRepositoryImpl
    private val database: BudgetFlowDatabase = mockk()
    private val transactionDao: TransactionDao = mockk()
    private val categoryDao: com.jamessaboia.budgetflow.data.local.dao.CategoryDao = mockk()

    @Before
    fun setup() {
        every { database.transactionDao() } returns transactionDao
        every { database.categoryDao() } returns categoryDao
        repository = TransactionRepositoryImpl(database)
    }

    @Test
    fun getTransactionCountByCategory_callsDao() = runTest {
        coEvery { transactionDao.getTransactionCountByCategory(1L) } returns 5

        val count = repository.getTransactionCountByCategory(1L)

        assertEquals(5, count)
        coVerify { transactionDao.getTransactionCountByCategory(1L) }
    }

    @Test
    fun addTransaction_callsDao() = runTest {
        val transaction = Transaction(
            id = 1,
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = 2,
            description = "Test",
            date = Date(),
            monthYear = "2026-04"
        )
        coEvery { transactionDao.insertTransaction(any()) } returns Unit

        repository.addTransaction(transaction)

        coVerify { transactionDao.insertTransaction(match {
            it.id == 1L && it.amount == 100.0 && it.categoryId == 2L
        }) }
    }
}
