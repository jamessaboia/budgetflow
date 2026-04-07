package com.jamessaboia.budgetflow.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.entities.MonthlyBudgetEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class BudgetDaoTest {

    private lateinit var budgetDao: BudgetDao
    private lateinit var db: BudgetFlowDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BudgetFlowDatabase::class.java
        ).allowMainThreadQueries().build()
        budgetDao = db.budgetDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetBudgetByMonth() = runTest {
        val budget = MonthlyBudgetEntity(
            id = 1,
            monthYear = "2026-04",
            baseIncome = 5000.0,
            extraIncome = 1000.0,
            needsPercentage = 50,
            wantsPercentage = 30,
            savingsPercentage = 20
        )
        budgetDao.insertBudget(budget)

        budgetDao.getBudgetByMonth("2026-04").test {
            val retrieved = awaitItem()
            assertNotNull(retrieved)
            assertEquals("2026-04", retrieved?.monthYear)
            assertEquals(5000.0, retrieved?.baseIncome)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getLatestBudgetBefore_returnsCorrectBudget() = runTest {
        val budget1 = MonthlyBudgetEntity(
            id = 1, monthYear = "2026-02", baseIncome = 4000.0, extraIncome = 0.0,
            needsPercentage = 50, wantsPercentage = 30, savingsPercentage = 20
        )
        val budget2 = MonthlyBudgetEntity(
            id = 2, monthYear = "2026-03", baseIncome = 5000.0, extraIncome = 1000.0,
            needsPercentage = 50, wantsPercentage = 30, savingsPercentage = 20
        )

        budgetDao.insertBudget(budget1)
        budgetDao.insertBudget(budget2)

        // Requesting for 2026-04, should get 2026-03
        val retrieved = budgetDao.getLatestBudgetBefore("2026-04")
        assertNotNull(retrieved)
        assertEquals("2026-03", retrieved?.monthYear)
        
        // Requesting for 2026-03, should get 2026-02
        val retrievedBeforeMarch = budgetDao.getLatestBudgetBefore("2026-03")
        assertNotNull(retrievedBeforeMarch)
        assertEquals("2026-02", retrievedBeforeMarch?.monthYear)

        // Requesting for 2026-01, should get null
        val retrievedBeforeJanuary = budgetDao.getLatestBudgetBefore("2026-01")
        assertNull(retrievedBeforeJanuary)
    }
}
