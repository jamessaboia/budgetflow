package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EnsureBudgetForMonthUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    /**
     * Ensures a budget exists for the given [monthYear].
     * If it doesn't exist, it clones the latest available budget before that month.
     * If no previous budget exists, it creates one with default percentages (50/30/20).
     */
    suspend operator fun invoke(monthYear: String) {
        val existingBudget = budgetRepository.getBudgetByMonth(monthYear).first()
        
        if (existingBudget == null) {
            val latestBudget = budgetRepository.getLatestBudgetBefore(monthYear)
            
            val newBudget = if (latestBudget != null) {
                // Clone the latest budget
                MonthlyBudget(
                    monthYear = monthYear,
                    baseIncome = latestBudget.baseIncome,
                    extraIncome = latestBudget.extraIncome,
                    needsPercentage = latestBudget.needsPercentage,
                    wantsPercentage = latestBudget.wantsPercentage,
                    savingsPercentage = latestBudget.savingsPercentage
                )
            } else {
                // Create a default budget
                MonthlyBudget(
                    monthYear = monthYear,
                    baseIncome = 0.0,
                    extraIncome = 0.0,
                    needsPercentage = 50,
                    wantsPercentage = 30,
                    savingsPercentage = 20
                )
            }
            budgetRepository.saveBudget(newBudget)
        }
    }
}
