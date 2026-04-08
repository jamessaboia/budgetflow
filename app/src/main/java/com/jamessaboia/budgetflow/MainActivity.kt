package com.jamessaboia.budgetflow

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0xFF3C6939.toInt()),
            navigationBarStyle = SystemBarStyle.dark(0xFF3C6939.toInt())
        )

        // Premium "Subtle Growth" Reveal Animation
        splashScreen.setOnExitAnimationListener { splashScreenProvider ->
            val iconView = splashScreenProvider.iconView
            val view = splashScreenProvider.view

            // 1. Icon Animation: Subtle scale up (breath effect) + smooth fade
            iconView.animate()
                .scaleX(2.5f) // Much more elegant and sharp
                .scaleY(2.5f)
                .alpha(0f)
                .setDuration(500L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

            // 2. Background Animation: Smooth slide reveal upward
            view.animate()
                .translationY(-view.height.toFloat())
                .setDuration(700L)
                .setStartDelay(100L)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    splashScreenProvider.remove()
                }
                .start()
        }

        setContent {
            BudgetFlowTheme {
                val userPrefs by budgetRepository.getUserPreferences().collectAsState(initial = null)
                val navController = rememberNavController()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    val prefs = userPrefs
                    if (prefs == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
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
