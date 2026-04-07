package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class EnsureBudgetForMonthUseCaseTest {

    private lateinit var useCase: EnsureBudgetForMonthUseCase
    private val budgetRepository: BudgetRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = EnsureBudgetForMonthUseCase(budgetRepository)
    }

    @Test
    fun `when budget exists, does nothing`() = runTest {
        val monthYear = "2026-04"
        val existingBudget = MonthlyBudget(
            monthYear = monthYear, baseIncome = 1000.0, extraIncome = 0.0,
            needsPercentage = 50, wantsPercentage = 30, savingsPercentage = 20
        )
        every { budgetRepository.getBudgetByMonth(monthYear) } returns flowOf(existingBudget)

        useCase(monthYear)

        coVerify(exactly = 0) { budgetRepository.getLatestBudgetBefore(any()) }
        coVerify(exactly = 0) { budgetRepository.saveBudget(any()) }
    }

    @Test
    fun `when budget missing but previous exists, clones previous budget`() = runTest {
        val monthYear = "2026-04"
        val previousBudget = MonthlyBudget(
            monthYear = "2026-03", baseIncome = 4000.0, extraIncome = 500.0,
            needsPercentage = 60, wantsPercentage = 20, savingsPercentage = 20
        )
        
        every { budgetRepository.getBudgetByMonth(monthYear) } returns flowOf(null)
        coEvery { budgetRepository.getLatestBudgetBefore(monthYear) } returns previousBudget

        useCase(monthYear)

        coVerify { budgetRepository.getLatestBudgetBefore(monthYear) }
        coVerify { 
            budgetRepository.saveBudget(match { 
                it.monthYear == monthYear &&
                it.baseIncome == 4000.0 &&
                it.extraIncome == 500.0 &&
                it.needsPercentage == 60 &&
                it.wantsPercentage == 20 &&
                it.savingsPercentage == 20
            }) 
        }
    }

    @Test
    fun `when budget missing and no previous exists, creates default budget`() = runTest {
        val monthYear = "2026-04"
        
        every { budgetRepository.getBudgetByMonth(monthYear) } returns flowOf(null)
        coEvery { budgetRepository.getLatestBudgetBefore(monthYear) } returns null

        useCase(monthYear)

        coVerify { budgetRepository.getLatestBudgetBefore(monthYear) }
        coVerify { 
            budgetRepository.saveBudget(match { 
                it.monthYear == monthYear &&
                it.baseIncome == 0.0 &&
                it.extraIncome == 0.0 &&
                it.needsPercentage == 50 &&
                it.wantsPercentage == 30 &&
                it.savingsPercentage == 20
            }) 
        }
    }
}
