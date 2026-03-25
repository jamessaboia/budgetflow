package com.jamessaboia.budgetflow.data.local

import com.jamessaboia.budgetflow.data.local.entities.CategoryEntity
import com.jamessaboia.budgetflow.data.local.entities.MonthlyBudgetEntity
import com.jamessaboia.budgetflow.data.local.entities.TransactionEntity
import com.jamessaboia.budgetflow.domain.model.Category
import com.jamessaboia.budgetflow.domain.model.MonthlyBudget
import com.jamessaboia.budgetflow.domain.model.Transaction

fun MonthlyBudgetEntity.toDomain() = MonthlyBudget(
    id = id,
    monthYear = monthYear,
    baseIncome = baseIncome,
    extraIncome = extraIncome,
    needsPercentage = needsPercentage,
    wantsPercentage = wantsPercentage,
    savingsPercentage = savingsPercentage
)

fun MonthlyBudget.toEntity() = MonthlyBudgetEntity(
    id = id,
    monthYear = monthYear,
    baseIncome = baseIncome,
    extraIncome = extraIncome,
    needsPercentage = needsPercentage,
    wantsPercentage = wantsPercentage,
    savingsPercentage = savingsPercentage
)

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    groupType = groupType,
    budgetLimit = budgetLimit,
    isDefault = isDefault
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    groupType = groupType,
    budgetLimit = budgetLimit,
    isDefault = isDefault
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    description = description,
    date = date,
    monthYear = monthYear
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    description = description,
    date = date,
    monthYear = monthYear
)
