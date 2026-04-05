package com.jamessaboia.budgetflow.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jamessaboia.budgetflow.R

@Composable
fun getCategoryDisplayName(name: String): String {
    val resId = when (name) {
        "Moradia", "cat_housing" -> R.string.cat_housing
        "Alimentação", "cat_food" -> R.string.cat_food
        "Transporte", "cat_transport" -> R.string.cat_transport
        "Saúde", "cat_health" -> R.string.cat_health
        "Educação", "cat_education" -> R.string.cat_education
        "Lazer", "cat_leisure" -> R.string.cat_leisure
        "Estilo de Vida", "cat_lifestyle" -> R.string.cat_lifestyle
        "Compras", "cat_shopping" -> R.string.cat_shopping
        "Assinaturas", "cat_subscriptions" -> R.string.cat_subscriptions
        "Reserva de Emergência", "cat_emergency" -> R.string.cat_emergency
        "Investimentos", "cat_investments" -> R.string.cat_investments
        "Objetivos", "cat_goals" -> R.string.cat_goals
        else -> null
    }
    
    return if (resId != null) stringResource(resId) else name
}

@Composable
fun getCategoryDescription(description: String?): String? {
    if (description == null) return null
    
    val resId = when (description) {
        "hint_housing" -> R.string.hint_housing
        "hint_food" -> R.string.hint_food
        "hint_transport" -> R.string.hint_transport
        "hint_health" -> R.string.hint_health
        "hint_education" -> R.string.hint_education
        "hint_leisure" -> R.string.hint_leisure
        "hint_lifestyle" -> R.string.hint_lifestyle
        "hint_shopping" -> R.string.hint_shopping
        "hint_subscriptions" -> R.string.hint_subscriptions
        "hint_emergency" -> R.string.hint_emergency
        "hint_investments" -> R.string.hint_investments
        "hint_goals" -> R.string.hint_goals
        else -> null
    }
    
    return if (resId != null) stringResource(resId) else description
}
