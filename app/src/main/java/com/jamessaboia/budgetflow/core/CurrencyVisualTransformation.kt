package com.jamessaboia.budgetflow.core

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * A VisualTransformation that applies thousands separators to a decimal input.
 * E.g., "1234.56" becomes "1.234,56" in PT-BR or "1,234.56" in EN.
 */
class CurrencyVisualTransformation : VisualTransformation {
    
    private val symbols = DecimalFormatSymbols(Locale.getDefault())
    private val thousandSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Format the text using a simple numeric parser
        val formattedText = formatWithSeparators(originalText)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val originalSub = originalText.substring(0, offset.coerceAtMost(originalText.length))
                return formatWithSeparators(originalSub).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val transformedSub = formattedText.substring(0, offset.coerceAtMost(formattedText.length))
                // Count how many non-separator characters are in the transformed substring
                return transformedSub.count { it != thousandSeparator }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }

    private fun formatWithSeparators(unformatted: String): String {
        // Find if there's a decimal part (we use '.' as the raw internal separator)
        val parts = unformatted.split(".")
        val integerPart = parts[0]
        val decimalPart = if (parts.size > 1) parts[1] else null

        // Format integer part with thousands separators
        val formattedInteger = if (integerPart.isEmpty()) {
            ""
        } else {
            integerPart.reversed()
                .chunked(3)
                .joinToString(thousandSeparator.toString())
                .reversed()
        }

        return if (decimalPart != null) {
            "$formattedInteger$decimalSeparator$decimalPart"
        } else {
            formattedInteger
        }
    }
}
