package com.jamessaboia.budgetflow.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.entities.CategoryEntity
import com.jamessaboia.budgetflow.data.local.entities.TransactionEntity
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.TransactionType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class TransactionDaoTest {

    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var db: BudgetFlowDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BudgetFlowDatabase::class.java
        ).allowMainThreadQueries().build()
        transactionDao = db.transactionDao()
        categoryDao = db.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTransactionsByMonth() = runTest {
        val category = CategoryEntity(id = 1, name = "Food", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true)
        categoryDao.insertCategory(category)

        val transaction1 = TransactionEntity(
            id = 1, amount = 100.0, type = TransactionType.EXPENSE, categoryId = 1,
            description = "Lunch", date = Date(), monthYear = "2026-04"
        )
        val transaction2 = TransactionEntity(
            id = 2, amount = 200.0, type = TransactionType.EXPENSE, categoryId = 1,
            description = "Dinner", date = Date(), monthYear = "2026-04"
        )
        val transaction3 = TransactionEntity(
            id = 3, amount = 50.0, type = TransactionType.EXPENSE, categoryId = 1,
            description = "Old", date = Date(), monthYear = "2026-03"
        )
        
        transactionDao.insertTransaction(transaction1)
        transactionDao.insertTransaction(transaction2)
        transactionDao.insertTransaction(transaction3)

        transactionDao.getTransactionsByMonth("2026-04").test {
            val list = awaitItem()
            assertEquals(2, list.size)
            
            // Should be ordered by date DESC, assuming date is same, ID determines order
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteTransaction() = runTest {
        val category = CategoryEntity(id = 1, name = "Food", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true)
        categoryDao.insertCategory(category)

        val transaction = TransactionEntity(
            id = 1, amount = 100.0, type = TransactionType.EXPENSE, categoryId = 1,
            description = "Lunch", date = Date(), monthYear = "2026-04"
        )
        transactionDao.insertTransaction(transaction)
        
        transactionDao.deleteTransaction(transaction)

        transactionDao.getTransactionsByMonth("2026-04").test {
            val list = awaitItem()
            assertEquals(0, list.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTotalExpensesAndIncomeByMonth() = runTest {
        categoryDao.insertCategory(CategoryEntity(id = 1, name = "Food", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true))
        categoryDao.insertCategory(CategoryEntity(id = 2, name = "Salary", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true))

        val expense1 = TransactionEntity(id = 1, amount = 100.0, type = TransactionType.EXPENSE, categoryId = 1, description = "", date = Date(), monthYear = "2026-04")
        val expense2 = TransactionEntity(id = 2, amount = 50.0, type = TransactionType.EXPENSE, categoryId = 1, description = "", date = Date(), monthYear = "2026-04")
        val income1 = TransactionEntity(id = 3, amount = 2000.0, type = TransactionType.INCOME, categoryId = 2, description = "", date = Date(), monthYear = "2026-04")

        transactionDao.insertTransaction(expense1)
        transactionDao.insertTransaction(expense2)
        transactionDao.insertTransaction(income1)

        transactionDao.getTotalExpensesByMonth("2026-04").test {
            assertEquals(150.0, awaitItem() ?: 0.0, 0.0)
            cancelAndIgnoreRemainingEvents()
        }

        transactionDao.getTotalIncomeByMonth("2026-04").test {
            assertEquals(2000.0, awaitItem() ?: 0.0, 0.0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTransactionCountByCategory() = runTest {
        categoryDao.insertCategory(CategoryEntity(id = 1, name = "Cat1", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true))
        categoryDao.insertCategory(CategoryEntity(id = 2, name = "Cat2", groupType = BudgetGroup.NEEDS, budgetLimit = null, isDefault = true))

        val transaction1 = TransactionEntity(id = 1, amount = 100.0, type = TransactionType.EXPENSE, categoryId = 1, description = "", date = Date(), monthYear = "2026-04")
        val transaction2 = TransactionEntity(id = 2, amount = 50.0, type = TransactionType.EXPENSE, categoryId = 1, description = "", date = Date(), monthYear = "2026-04")
        val transaction3 = TransactionEntity(id = 3, amount = 50.0, type = TransactionType.EXPENSE, categoryId = 2, description = "", date = Date(), monthYear = "2026-04")

        transactionDao.insertTransaction(transaction1)
        transactionDao.insertTransaction(transaction2)
        transactionDao.insertTransaction(transaction3)

        val countCat1 = transactionDao.getTransactionCountByCategory(1)
        val countCat2 = transactionDao.getTransactionCountByCategory(2)
        val countCat3 = transactionDao.getTransactionCountByCategory(3)

        assertEquals(2, countCat1)
        assertEquals(1, countCat2)
        assertEquals(0, countCat3)
    }
}
