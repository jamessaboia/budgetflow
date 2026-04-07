package com.jamessaboia.budgetflow.domain.usecase

import app.cash.turbine.test
import com.jamessaboia.budgetflow.domain.model.*
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.*

class GetDashboardSummaryUseCaseTest {

    private lateinit var useCase: GetDashboardSummaryUseCase
    private val budgetRepository: BudgetRepository = mockk()
    private val transactionRepository: TransactionRepository = mockk()

    @Before
    fun setup() {
        useCase = GetDashboardSummaryUseCase(budgetRepository, transactionRepository)
    }

    @Test
    fun `when budget exists, calculates summary with actual and planned income`() = runTest {
        val monthYear = "2026-04"
        val budget = MonthlyBudget(
            monthYear = monthYear,
            baseIncome = 5000.0,
            extraIncome = 1300.0, // Total planned = 6300
            needsPercentage = 50,
            wantsPercentage = 30,
            savingsPercentage = 20
        )
        
        val categories = listOf(
            Category(id = 1, name = "Food", groupType = BudgetGroup.NEEDS),
            Category(id = 2, name = "Rent", groupType = BudgetGroup.NEEDS),
            Category(id = 3, name = "Salary", groupType = BudgetGroup.NEEDS) // Type doesn't matter for income calc
        )

        val transactions = listOf(
            TransactionWithCategory(
                transaction = Transaction(amount = 2500.0, type = TransactionType.INCOME, categoryId = 3, date = Date(), monthYear = monthYear),
                categoryName = "Salary"
            ),
            TransactionWithCategory(
                transaction = Transaction(amount = 1000.0, type = TransactionType.EXPENSE, categoryId = 1, date = Date(), monthYear = monthYear),
                categoryName = "Food"
            )
        )

        every { budgetRepository.getBudgetByMonth(monthYear) } returns flowOf(budget)
        every { transactionRepository.getTransactionsByMonth(monthYear) } returns flowOf(transactions)
        every { budgetRepository.getAllCategories() } returns flowOf(categories)

        useCase(monthYear).test {
            val summary = awaitItem()
            assertNotNull(summary)
            assertEquals(2500.0, summary!!.actualIncome, 0.0)
            assertEquals(6300.0, summary.plannedIncome, 0.0)
            assertEquals(1000.0, summary.totalSpent, 0.0)
            assertEquals(1500.0, summary.remainingBalance, 0.0) // 2500 - 1000
            
            // Needs limit should be 50% of 6300 = 3150
            assertEquals(3150.0, summary.needsSummary.limit, 0.0)
            assertEquals(1000.0, summary.needsSummary.spent, 0.0)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when budget is null, returns null`() = runTest {
        val monthYear = "2026-04"
        every { budgetRepository.getBudgetByMonth(monthYear) } returns flowOf(null)
        every { transactionRepository.getTransactionsByMonth(monthYear) } returns flowOf(emptyList())
        every { budgetRepository.getAllCategories() } returns flowOf(emptyList())

        useCase(monthYear).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
