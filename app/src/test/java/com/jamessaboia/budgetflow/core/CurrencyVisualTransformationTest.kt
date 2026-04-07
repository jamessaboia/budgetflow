package com.jamessaboia.budgetflow.core

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DecimalFormatSymbols
import java.util.Locale

class CurrencyVisualTransformationTest {

    @Test
    fun `filter formats integer correctly`() {
        val transformation = CurrencyVisualTransformation()
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        val separator = symbols.groupingSeparator
        
        val input = AnnotatedString("1000")
        val result = transformation.filter(input)
        
        assertEquals("1${separator}000", result.text.text)
    }

    @Test
    fun `filter formats decimal correctly`() {
        val transformation = CurrencyVisualTransformation()
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        val groupSeparator = symbols.groupingSeparator
        val decSeparator = symbols.decimalSeparator
        
        val input = AnnotatedString("1000.50")
        val result = transformation.filter(input)
        
        assertEquals("1${groupSeparator}000${decSeparator}50", result.text.text)
    }

    @Test
    fun `filter handles empty string`() {
        val transformation = CurrencyVisualTransformation()
        
        val input = AnnotatedString("")
        val result = transformation.filter(input)
        
        assertEquals("", result.text.text)
    }

    @Test
    fun `offset mapping translates offsets correctly`() {
        val transformation = CurrencyVisualTransformation()
        val input = AnnotatedString("1000")
        val result = transformation.filter(input)
        
        // original "1000"
        // mapped "1,000" (if grouping separator is comma)
        // Offset 0 -> 0
        // Offset 1 ('1') -> 1
        // Offset 2 ('0') -> 3 (after '1,0')
        assertEquals(0, result.offsetMapping.originalToTransformed(0))
        assertEquals(1, result.offsetMapping.originalToTransformed(1))
        assertEquals(2, result.offsetMapping.originalToTransformed(2))
        assertEquals(3, result.offsetMapping.originalToTransformed(3))
        assertEquals(5, result.offsetMapping.originalToTransformed(4))

        assertEquals(0, result.offsetMapping.transformedToOriginal(0))
        assertEquals(1, result.offsetMapping.transformedToOriginal(1))
        assertEquals(1, result.offsetMapping.transformedToOriginal(2)) // skips separator
        assertEquals(2, result.offsetMapping.transformedToOriginal(3))
        assertEquals(3, result.offsetMapping.transformedToOriginal(4))
        assertEquals(4, result.offsetMapping.transformedToOriginal(5))
    }
}
