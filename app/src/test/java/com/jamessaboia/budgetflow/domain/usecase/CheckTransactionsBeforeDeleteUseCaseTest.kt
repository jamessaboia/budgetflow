package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckTransactionsBeforeDeleteUseCaseTest {

    private lateinit var useCase: CheckTransactionsBeforeDeleteUseCase
    private val repository: TransactionRepository = mockk()

    @Before
    fun setup() {
        useCase = CheckTransactionsBeforeDeleteUseCase(repository)
    }

    @Test
    fun `returns true when category has transactions`() = runTest {
        coEvery { repository.getTransactionCountByCategory(1L) } returns 5
        
        val result = useCase(1L)
        
        assertTrue(result)
    }

    @Test
    fun `returns false when category has no transactions`() = runTest {
        coEvery { repository.getTransactionCountByCategory(1L) } returns 0
        
        val result = useCase(1L)
        
        assertFalse(result)
    }
}
