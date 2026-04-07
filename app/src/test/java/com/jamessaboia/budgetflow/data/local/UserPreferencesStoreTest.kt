package com.jamessaboia.budgetflow.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class UserPreferencesStoreTest {

    private lateinit var context: Context
    private lateinit var store: UserPreferencesStore

    @Before
    fun setup() = runTest {
        context = ApplicationProvider.getApplicationContext()
        // Clear any previous datastore preferences
        context.dataStore.edit { it.clear() }
        store = UserPreferencesStore(context)
    }

    @After
    fun teardown() = runTest {
        context.dataStore.edit { it.clear() }
    }

    @Test
    fun defaultPreferences_areReturnedCorrectly() = runTest {
        store.userPreferencesFlow.test {
            val prefs = awaitItem()
            assertFalse(prefs.isOnboardingComplete)
            assertTrue(prefs.isBalanceVisible)
            assertEquals(50, prefs.defaultNeedsPercent)
            assertEquals(30, prefs.defaultWantsPercent)
            assertEquals(20, prefs.defaultSavingsPercent)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateOnboardingComplete_updatesValue() = runTest {
        store.updateOnboardingComplete(true)
        
        store.userPreferencesFlow.test {
            val prefs = awaitItem()
            assertTrue(prefs.isOnboardingComplete)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBalanceVisible_updatesValue() = runTest {
        store.updateBalanceVisible(false)
        
        store.userPreferencesFlow.test {
            val prefs = awaitItem()
            assertFalse(prefs.isBalanceVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateDefaultPercentages_updatesValues() = runTest {
        store.updateDefaultPercentages(60, 20, 20)
        
        store.userPreferencesFlow.test {
            val prefs = awaitItem()
            assertEquals(60, prefs.defaultNeedsPercent)
            assertEquals(20, prefs.defaultWantsPercent)
            assertEquals(20, prefs.defaultSavingsPercent)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
