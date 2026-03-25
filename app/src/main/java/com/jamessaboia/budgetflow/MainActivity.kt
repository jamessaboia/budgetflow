package com.jamessaboia.budgetflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.navigation.AppNavigation
import com.jamessaboia.budgetflow.navigation.Screen
import com.jamessaboia.budgetflow.ui.theme.BudgetFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var budgetRepository: BudgetRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetFlowTheme {
                val userPrefs by budgetRepository.getUserPreferences().collectAsState(initial = null)
                val navController = rememberNavController()

                Box(modifier = Modifier.fillMaxSize()) {
                    val prefs = userPrefs
                    if (prefs == null) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        val startDestination = if (prefs.isOnboardingComplete) {
                            Screen.Dashboard.route
                        } else {
                            Screen.Onboarding.route
                        }
                        AppNavigation(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}
