package com.jamessaboia.budgetflow.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.entities.CategoryEntity
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
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
class CategoryDaoTest {

    private lateinit var categoryDao: CategoryDao
    private lateinit var db: BudgetFlowDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BudgetFlowDatabase::class.java
        ).allowMainThreadQueries().build()
        categoryDao = db.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetCategory() = runTest {
        val category = CategoryEntity(
            id = 1,
            name = "Test Category",
            groupType = BudgetGroup.NEEDS,
            budgetLimit = 500.0,
            isDefault = false
        )
        categoryDao.insertCategory(category)
        
        val retrieved = categoryDao.getCategoryById(1)
        assertNotNull(retrieved)
        assertEquals(category.name, retrieved?.name)
        assertEquals(category.groupType, retrieved?.groupType)
    }

    @Test
    fun getAllCategories() = runTest {
        val category1 = CategoryEntity(id = 1, name = "Cat 1", groupType = BudgetGroup.NEEDS, budgetLimit = 100.0, isDefault = false)
        val category2 = CategoryEntity(id = 2, name = "Cat 2", groupType = BudgetGroup.WANTS, budgetLimit = 200.0, isDefault = false)
        
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        categoryDao.getAllCategories().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("Cat 1", list[0].name)
            assertEquals("Cat 2", list[1].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateCategory() = runTest {
        val category = CategoryEntity(id = 1, name = "Old Name", groupType = BudgetGroup.NEEDS, budgetLimit = 100.0, isDefault = false)
        categoryDao.insertCategory(category)
        
        val updatedCategory = category.copy(name = "New Name")
        categoryDao.insertCategory(updatedCategory)
        
        val retrieved = categoryDao.getCategoryById(1)
        assertEquals("New Name", retrieved?.name)
    }

    @Test
    fun deleteCategory() = runTest {
        val category = CategoryEntity(id = 1, name = "To Delete", groupType = BudgetGroup.NEEDS, budgetLimit = 100.0, isDefault = false)
        categoryDao.insertCategory(category)
        
        categoryDao.deleteCategory(category)
        
        val retrieved = categoryDao.getCategoryById(1)
        assertNull(retrieved)
    }
}
