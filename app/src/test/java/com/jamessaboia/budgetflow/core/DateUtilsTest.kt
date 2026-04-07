package com.jamessaboia.budgetflow.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateUtilsTest {

    @Test
    fun `formatMonthYearForDisplay returns correctly formatted string for valid input`() {
        val input = "2026-04"
        val expectedDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(input)!!
        val expectedOutput = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(expectedDate).replaceFirstChar { it.uppercase() }
        
        val actualOutput = DateUtils.formatMonthYearForDisplay(input)
        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun `formatMonthYearForDisplay returns input string for invalid input`() {
        val input = "invalid-date"
        val actualOutput = DateUtils.formatMonthYearForDisplay(input)
        assertEquals(input, actualOutput)
    }

    @Test
    fun `getCurrentMonthYear returns correctly formatted current date`() {
        val expected = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
        val actual = DateUtils.getCurrentMonthYear()
        assertEquals(expected, actual)
    }
}
