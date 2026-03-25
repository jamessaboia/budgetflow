package com.jamessaboia.budgetflow.domain.usecase

import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.DashboardSummary
import com.jamessaboia.budgetflow.domain.model.GroupSummary
import com.jamessaboia.budgetflow.domain.model.TransactionType
import com.jamessaboia.budgetflow.domain.repository.BudgetRepository
import com.jamessaboia.budgetflow.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardSummary?> {
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
        
        return combine(
            budgetRepository.getBudgetByMonth(currentMonth),
            transactionRepository.getTransactionsByMonth(currentMonth),
            budgetRepository.getAllCategories()
        ) { budget, transactions, categories ->
            if (budget == null) return@combine null

            val categoryGroupMap = categories.associateBy({ it.id }, { it.groupType })
            
            val expensesByGroup = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { categoryGroupMap[it.categoryId] ?: BudgetGroup.NEEDS }
                .mapValues { entry -> entry.value.sumOf { it.amount } }

            val totalIncome = budget.totalIncome
            
            fun createGroupSummary(group: BudgetGroup, percentage: Int): GroupSummary {
                val limit = totalIncome * percentage / 100.0
                val spent = expensesByGroup[group] ?: 0.0
                val percentageSpent = if (limit > 0) (spent / limit).toFloat() else 0f
                val remaining = limit - spent
                return GroupSummary(limit, spent, percentageSpent, remaining)
            }

            val needsSummary = createGroupSummary(BudgetGroup.NEEDS, budget.needsPercentage)
            val wantsSummary = createGroupSummary(BudgetGroup.WANTS, budget.wantsPercentage)
            val savingsSummary = createGroupSummary(BudgetGroup.SAVINGS, budget.savingsPercentage)

            val totalSpent = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val remainingBalance = totalIncome - totalSpent

            DashboardSummary(
                totalIncome = totalIncome,
                totalSpent = totalSpent,
                remainingBalance = remainingBalance,
                needsSummary = needsSummary,
                wantsSummary = wantsSummary,
                savingsSummary = savingsSummary
            )
        }
    }
}
