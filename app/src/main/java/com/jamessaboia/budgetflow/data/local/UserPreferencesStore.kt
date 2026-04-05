package com.jamessaboia.budgetflow.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesStore(private val context: Context) {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("is_onboarding_complete")
        val DEFAULT_NEEDS_PERCENT = intPreferencesKey("default_needs_percent")
        val DEFAULT_WANTS_PERCENT = intPreferencesKey("default_wants_percent")
        val DEFAULT_SAVINGS_PERCENT = intPreferencesKey("default_savings_percent")
        val IS_BALANCE_VISIBLE = booleanPreferencesKey("is_balance_visible")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                isOnboardingComplete = preferences[PreferencesKeys.IS_ONBOARDING_COMPLETE] ?: false,
                defaultNeedsPercent = preferences[PreferencesKeys.DEFAULT_NEEDS_PERCENT] ?: 50,
                defaultWantsPercent = preferences[PreferencesKeys.DEFAULT_WANTS_PERCENT] ?: 30,
                defaultSavingsPercent = preferences[PreferencesKeys.DEFAULT_SAVINGS_PERCENT] ?: 20,
                isBalanceVisible = preferences[PreferencesKeys.IS_BALANCE_VISIBLE] ?: true
            )
        }

    suspend fun updateOnboardingComplete(complete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETE] = complete
        }
    }

    suspend fun updateBalanceVisible(visible: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_BALANCE_VISIBLE] = visible
        }
    }

    suspend fun updateDefaultPercentages(needs: Int, wants: Int, savings: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_NEEDS_PERCENT] = needs
            preferences[PreferencesKeys.DEFAULT_WANTS_PERCENT] = wants
            preferences[PreferencesKeys.DEFAULT_SAVINGS_PERCENT] = savings
        }
    }
}

data class UserPreferences(
    val isOnboardingComplete: Boolean,
    val defaultNeedsPercent: Int,
    val defaultWantsPercent: Int,
    val defaultSavingsPercent: Int,
    val isBalanceVisible: Boolean
)
