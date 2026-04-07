package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteCategoryUseCaseTest {

    private lateinit var useCase: DeleteCategoryUseCase
    private val repository: BudgetRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = DeleteCategoryUseCase(repository)
    }

    @Test
    fun `deleteCategory delegates to repository`() = runTest {
        val category = Category(id = 1, name = "Test", groupType = BudgetGroup.NEEDS)
        
        useCase(category)
        
        coVerify { repository.deleteCategory(category) }
    }
}
