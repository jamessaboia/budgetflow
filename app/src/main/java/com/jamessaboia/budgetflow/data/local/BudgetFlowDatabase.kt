package com.jamessaboia.budgetflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jamessaboia.budgetflow.data.local.dao.BudgetDao
import com.jamessaboia.budgetflow.data.local.dao.CategoryDao
import com.jamessaboia.budgetflow.data.local.dao.TransactionDao
import com.jamessaboia.budgetflow.data.local.entities.CategoryEntity
import com.jamessaboia.budgetflow.data.local.entities.MonthlyBudgetEntity
import com.jamessaboia.budgetflow.data.local.entities.TransactionEntity

@Database(
    entities = [
        MonthlyBudgetEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BudgetFlowDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
