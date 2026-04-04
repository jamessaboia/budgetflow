package com.jamessaboia.budgetflow.core

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dbFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val displayFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    fun formatMonthYearForDisplay(monthYear: String): String {
        return try {
            val date = dbFormat.parse(monthYear) ?: return monthYear
            displayFormat.format(date).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            monthYear
        }
    }

    fun getCurrentMonthYear(): String {
        return dbFormat.format(Calendar.getInstance().time)
    }
}
