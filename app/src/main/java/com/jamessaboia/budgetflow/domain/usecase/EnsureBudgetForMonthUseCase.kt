package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EnsureBudgetForMonthUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    




    suspend operator fun invoke(monthYear: String) {
        val existingBudget = budgetRepository.getBudgetByMonth(monthYear).first()
        
        if (existingBudget == null) {
            val latestBudget = budgetRepository.getLatestBudgetBefore(monthYear)
            
            val newBudget = if (latestBudget != null) {
                
                MonthlyBudget(
                    monthYear = monthYear,
                    baseIncome = latestBudget.baseIncome,
                    extraIncome = latestBudget.extraIncome,
                    needsPercentage = latestBudget.needsPercentage,
                    wantsPercentage = latestBudget.wantsPercentage,
                    savingsPercentage = latestBudget.savingsPercentage
                )
            } else {
                
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
