package com.jamessaboia.budgetflow.ui.features.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R

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
                .padding(24.dp),
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
            onValueChange = onIncomeChange,
            label = { Text(stringResource(R.string.income_main_label)) },
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = extraIncome,
            onValueChange = onExtraIncomeChange,
            label = { Text(stringResource(R.string.income_extra_label)) },
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                enabled = income.toDoubleOrNull() != null && income.toDouble() > 0
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

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
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = needs == 50 && wants == 30 && savings == 20,
                onClick = { onPercentagesChange(50, 30, 20) },
                label = { Text("50/30/20") }
            )
            FilterChip(
                selected = needs == 60 && wants == 20 && savings == 20,
                onClick = { onPercentagesChange(60, 20, 20) },
                label = { Text("60/20/20") }
            )
            FilterChip(
                selected = needs == 50 && wants == 20 && savings == 30,
                onClick = { onPercentagesChange(50, 20, 30) },
                label = { Text("50/20/30") }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PercentageSlider(stringResource(R.string.group_needs), needs) { onPercentagesChange(it, wants, savings) }
        PercentageSlider(stringResource(R.string.group_wants), wants) { onPercentagesChange(needs, it, savings) }
        PercentageSlider(stringResource(R.string.group_savings), savings) { onPercentagesChange(needs, wants, it) }
        
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
fun PercentageSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.percentage_value, value), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..100f,
            steps = 100
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
    val incomeValue = income.toDoubleOrNull() ?: 0.0
    val extraIncomeValue = extraIncome.toDoubleOrNull() ?: 0.0
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
