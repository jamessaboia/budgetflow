package com.jamessaboia.budgetflow.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object Transactions : Screen("transactions")
    object Settings : Screen("settings")
    object CategoryManagement : Screen("category_management")
    object GroupCategoryList : Screen("group_category_list/{groupName}") {
        fun createRoute(groupName: String) = "group_category_list/$groupName"
    }
}
