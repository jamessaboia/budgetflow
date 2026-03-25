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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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
            text = "Bem-vindo ao BudgetFlow",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Vamos organizar suas finanças usando a regra 50/30/20 de forma simples e prática.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Começar Agora")
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
            text = "Qual sua renda?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Informe seu salário principal e, se houver, uma renda extra mensal.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = income,
            onValueChange = onIncomeChange,
            label = { Text("Renda Principal") },
            prefix = { Text("R$ ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = extraIncome,
            onValueChange = onExtraIncomeChange,
            label = { Text("Renda Extra (Opcional)") },
            prefix = { Text("R$ ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = income.toDoubleOrNull() != null && income.toDouble() > 0
            ) {
                Text("Próximo")
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
            text = "Como quer dividir sua renda?",
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
        
        PercentageSlider("Essenciais", needs) { onPercentagesChange(it, wants, savings) }
        PercentageSlider("Estilo de Vida", wants) { onPercentagesChange(needs, it, savings) }
        PercentageSlider("Reserva", savings) { onPercentagesChange(needs, wants, it) }
        
        Spacer(modifier = Modifier.height(16.dp))
        val total = needs + wants + savings
        Text(
            text = "Total: $total%",
            style = MaterialTheme.typography.titleMedium,
            color = if (total == 100) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        if (total != 100) {
            Text(
                text = "A soma deve ser exatamente 100%.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext, 
                modifier = Modifier.weight(1f),
                enabled = total == 100
            ) {
                Text("Próximo")
            }
        }
    }
}

@Composable
fun PercentageSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$value%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
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
            text = "Tudo pronto!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SummaryRow("Renda Total", "R$ %.2f".format(totalIncome))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SummaryRow("Essenciais ($needs%)", "R$ %.2f".format(totalIncome * needs / 100))
                SummaryRow("Estilo de Vida ($wants%)", "R$ %.2f".format(totalIncome * wants / 100))
                SummaryRow("Reserva ($savings%)", "R$ %.2f".format(totalIncome * savings / 100))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Voltar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                    Text("Finalizar")
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
