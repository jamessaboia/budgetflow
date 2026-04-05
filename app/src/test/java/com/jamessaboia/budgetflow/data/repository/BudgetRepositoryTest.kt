package com.jamessaboia.budgetflow.data.repository

import app.cash.turbine.test
import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.UserPreferencesStore
import com.jamessaboia.budgetflow.data.local.dao.BudgetDao
import com.jamessaboia.budgetflow.data.local.dao.CategoryDao
import com.jamessaboia.budgetflow.data.local.entities.CategoryEntity
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BudgetRepositoryTest {

    private lateinit var repository: BudgetRepositoryImpl
    private val database: BudgetFlowDatabase = mockk()
    private val budgetDao: BudgetDao = mockk()
    private val categoryDao: CategoryDao = mockk()
    private val preferencesStore: UserPreferencesStore = mockk()

    @Before
    fun setup() {
        every { database.budgetDao() } returns budgetDao
        every { database.categoryDao() } returns categoryDao
        repository = BudgetRepositoryImpl(database, preferencesStore)
    }

    @Test
    fun getAllCategories_mapsCorrectly() = runTest {
        val entities = listOf(
            CategoryEntity(id = 1, name = "Cat 1", groupType = BudgetGroup.NEEDS, budgetLimit = 100.0, isDefault = true),
            CategoryEntity(id = 2, name = "Cat 2", groupType = BudgetGroup.WANTS, budgetLimit = 200.0, isDefault = false)
        )
        every { categoryDao.getAllCategories() } returns flowOf(entities)

        repository.getAllCategories().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("Cat 1", list[0].name)
            assertEquals(BudgetGroup.NEEDS, list[0].groupType)
            assertEquals(true, list[0].isDefault)
            
            assertEquals("Cat 2", list[1].name)
            assertEquals(BudgetGroup.WANTS, list[1].groupType)
            assertEquals(false, list[1].isDefault)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveCategory_callsDao() = runTest {
        val category = Category(id = 1, name = "New Cat", groupType = BudgetGroup.NEEDS, isDefault = false)
        coEvery { categoryDao.insertCategory(any()) } returns Unit

        repository.saveCategory(category)

        coVerify { categoryDao.insertCategory(match { 
            it.id == 1L && it.name == "New Cat" && it.groupType == BudgetGroup.NEEDS 
        }) }
    }

    @Test
    fun deleteCategory_callsDao() = runTest {
        val category = Category(id = 1, name = "To Delete", groupType = BudgetGroup.NEEDS, isDefault = false)
        coEvery { categoryDao.deleteCategory(any()) } returns Unit

        repository.deleteCategory(category)

        coVerify { categoryDao.deleteCategory(match { 
            it.id == 1L && it.name == "To Delete" 
        }) }
    }
}
