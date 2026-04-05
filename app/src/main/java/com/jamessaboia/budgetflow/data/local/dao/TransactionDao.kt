package com.jamessaboia.budgetflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamessaboia.budgetflow.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE monthYear = :monthYear ORDER BY date DESC")
    fun getTransactionsByMonth(monthYear: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT SUM(amount) FROM transactions WHERE monthYear = :monthYear AND type = 'EXPENSE'")
    fun getTotalExpensesByMonth(monthYear: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE monthYear = :monthYear AND type = 'INCOME'")
    fun getTotalIncomeByMonth(monthYear: String): Flow<Double?>

    @Query("SELECT COUNT(*) FROM transactions WHERE categoryId = :categoryId")
    suspend fun getTransactionCountByCategory(categoryId: Long): Int
}
