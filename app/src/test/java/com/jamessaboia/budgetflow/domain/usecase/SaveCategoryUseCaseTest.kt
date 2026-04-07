package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveCategoryUseCaseTest {

    private lateinit var useCase: SaveCategoryUseCase
    private val repository: BudgetRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = SaveCategoryUseCase(repository)
    }

    @Test
    fun `saveCategory delegates to repository`() = runTest {
        val category = Category(id = 1, name = "Test", groupType = BudgetGroup.NEEDS)
        
        useCase(category)
        
        coVerify { repository.saveCategory(category) }
    }
}
