package com.jamessaboia.budgetflow.di

import com.jamessaboia.budgetflow.data.repository.BudgetRepositoryImpl
import com.jamessaboia.budgetflow.data.repository.TransactionRepositoryImpl
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}
