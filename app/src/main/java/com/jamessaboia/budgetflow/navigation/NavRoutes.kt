package com.jamessaboia.budgetflow.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object Transactions : Screen("transactions")
    object Settings : Screen("settings")
}
