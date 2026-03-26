package com.jamessaboia.budgetflow.ui.features.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.CurrencyVisualTransformation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isComplete) {
        onOnboardingComplete()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(targetState = uiState.step, label = "OnboardingTransition") { step ->
                when (step) {
                    OnboardingStep.WELCOME -> WelcomeStep(onNext = { viewModel.nextStep() })
                    OnboardingStep.INCOME -> IncomeStep(
                        income = uiState.baseIncome,
                        extraIncome = uiState.extraIncome,
                        onIncomeChange = viewModel::onIncomeChange,
                        onExtraIncomeChange = viewModel::onExtraIncomeChange,
                        onNext = { viewModel.nextStep() },
                        onBack = { viewModel.previousStep() }
                    )
                    OnboardingStep.PERCENTAGES -> PercentagesStep(
                        needs = uiState.needsPercent,
                        wants = uiState.wantsPercent,
                        savings = uiState.savingsPercent,
                        onPercentagesChange = viewModel::onPercentagesChange,
                        onNext = { viewModel.nextStep() },
                        onBack = { viewModel.previousStep() }
                    )
                    OnboardingStep.SUMMARY -> SummaryStep(
                        income = uiState.baseIncome,
                        extraIncome = uiState.extraIncome,
                        needs = uiState.needsPercent,
                        wants = uiState.wantsPercent,
                        savings = uiState.savingsPercent,
                        isLoading = uiState.isLoading,
                        onConfirm = { viewModel.completeOnboarding() },
                        onBack = { viewModel.previousStep() }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeStep(onNext: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.start_now))
        }
    }
}

@Composable
fun IncomeStep(
    income: String,
    extraIncome: String,
    onIncomeChange: (String) -> Unit,
    onExtraIncomeChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.income_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.income_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = income,
            onValueChange = { input ->
                val filtered = input.replace(",", ".").filterIndexed { index, char ->
                    char.isDigit() || (char == '.' && input.indexOf('.') == index)
                }
                onIncomeChange(filtered)
            },
            label = { Text(stringResource(R.string.income_main_label)) },
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            visualTransformation = CurrencyVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = extraIncome,
            onValueChange = { input ->
                val normalized = input.replace(",", ".")
                val filtered = normalized.filterIndexed { index, char ->
                    char.isDigit() || (char == '.' && normalized.indexOf('.') == index)
                }
                onExtraIncomeChange(filtered)
            },
            label = { Text(stringResource(R.string.income_extra_label)) },
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            visualTransformation = CurrencyVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.back))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = income.replace(",", ".").toDoubleOrNull() != null && income.replace(",", ".").toDouble() > 0
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentagesStep(
    needs: Int,
    wants: Int,
    savings: Int,
    onPercentagesChange: (Int, Int, Int) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.percentages_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetOption(
                selected = needs == 50 && wants == 30 && savings == 20,
                title = stringResource(R.string.preset_balanced),
                description = stringResource(R.string.preset_balanced_desc),
                onClick = { onPercentagesChange(50, 30, 20) }
            )
            PresetOption(
                selected = needs == 50 && wants == 25 && savings == 25,
                title = stringResource(R.string.preset_accelerated),
                description = stringResource(R.string.preset_accelerated_desc),
                onClick = { onPercentagesChange(50, 25, 25) }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        PercentageSlider(
            label = stringResource(R.string.group_needs),
            value = needs,
            hint = stringResource(R.string.hint_group_needs),
            color = MaterialTheme.colorScheme.primary,
            onValueChange = { onPercentagesChange(it, wants, savings) }
        )
        PercentageSlider(
            label = stringResource(R.string.group_wants),
            value = wants,
            hint = stringResource(R.string.hint_group_lifestyle),
            color = MaterialTheme.colorScheme.primary,
            onValueChange = { onPercentagesChange(needs, it, savings) }
        )
        PercentageSlider(
            label = stringResource(R.string.group_savings),
            value = savings,
            hint = stringResource(R.string.hint_group_savings),
            color = MaterialTheme.colorScheme.primary,
            onValueChange = { onPercentagesChange(needs, wants, it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        val total = needs + wants + savings
        Text(
            text = stringResource(R.string.total_label, total),
            style = MaterialTheme.typography.titleMedium,
            color = if (total == 100) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        if (total != 100) {
            Text(
                text = stringResource(R.string.total_error),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.back))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext, 
                modifier = Modifier.weight(1f),
                enabled = total == 100
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Composable
fun PresetOption(
    selected: Boolean,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = if (selected) null else CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PercentageSlider(
    label: String,
    value: Int,
    hint: String,
    color: Color,
    onValueChange: (Int) -> Unit
) {
    var showHint by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { showHint = !showHint }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Info, 
                        contentDescription = "Hint", 
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }
            Text(
                text = stringResource(R.string.percentage_value, value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        
        if (showHint) {
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..100f,
            steps = 100,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}

@Composable
fun SummaryStep(
    income: String,
    extraIncome: String,
    needs: Int,
    wants: Int,
    savings: Int,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val incomeValue = income.replace(",", ".").toDoubleOrNull() ?: 0.0
    val extraIncomeValue = extraIncome.replace(",", ".").toDoubleOrNull() ?: 0.0
    val totalIncome = incomeValue + extraIncomeValue
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.summary_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SummaryRow(stringResource(R.string.summary_income_total), stringResource(R.string.currency_format, totalIncome))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SummaryRow(stringResource(R.string.group_needs_summary, needs), stringResource(R.string.currency_format, totalIncome * needs / 100))
                SummaryRow(stringResource(R.string.group_wants_summary, wants), stringResource(R.string.currency_format, totalIncome * wants / 100))
                SummaryRow(stringResource(R.string.group_savings_summary, savings), stringResource(R.string.currency_format, totalIncome * savings / 100))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.back))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.finish))
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
