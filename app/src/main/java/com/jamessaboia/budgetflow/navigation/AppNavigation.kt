package com.jamessaboia.budgetflow.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jamessaboia.budgetflow.ui.features.categories.CategoryManagementScreen
import com.jamessaboia.budgetflow.ui.features.categories.GroupCategoryListScreen
import com.jamessaboia.budgetflow.ui.features.dashboard.DashboardScreen
import com.jamessaboia.budgetflow.ui.features.onboarding.OnboardingScreen
import com.jamessaboia.budgetflow.ui.features.settings.BudgetSettingsScreen
import com.jamessaboia.budgetflow.ui.features.transactions.AddTransactionScreen
import com.jamessaboia.budgetflow.ui.features.transactions.TransactionsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onNavigateToTransactions = {
                    navController.navigate(Screen.Transactions.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            BudgetSettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCategories = { navController.navigate(Screen.CategoryManagement.route) }
            )
        }
        composable(Screen.CategoryManagement.route) {
            CategoryManagementScreen(
                onBack = { navController.popBackStack() },
                onNavigateToGroup = { groupName ->
                    navController.navigate(Screen.GroupCategoryList.createRoute(groupName))
                }
            )
        }
        composable(Screen.GroupCategoryList.route) { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: return@composable
            GroupCategoryListScreen(
                groupName = groupName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
