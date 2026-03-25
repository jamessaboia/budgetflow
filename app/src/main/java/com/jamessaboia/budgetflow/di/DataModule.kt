package com.jamessaboia.budgetflow.di

import android.content.Context
import androidx.room.Room
import com.jamessaboia.budgetflow.data.local.BudgetFlowDatabase
import com.jamessaboia.budgetflow.data.local.UserPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BudgetFlowDatabase {
        return Room.databaseBuilder(
            context,
            BudgetFlowDatabase::class.java,
            "budget_flow_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesStore(@ApplicationContext context: Context): UserPreferencesStore {
        return UserPreferencesStore(context)
    }
}
