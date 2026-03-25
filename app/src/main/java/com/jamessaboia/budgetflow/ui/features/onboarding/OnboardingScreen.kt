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
import androidx.hilt.navigation.compose.hiltViewModel

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
                        onIncomeChange = viewModel::onIncomeChange,
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
    onIncomeChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Qual seu salário líquido?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Informe quanto você recebe mensalmente após os descontos.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = income,
            onValueChange = onIncomeChange,
            label = { Text("Renda Mensal") },
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
        Spacer(modifier = Modifier.height(24.dp))
        
        PercentageItem("Essenciais (Necessidades)", needs)
        PercentageItem("Estilo de Vida (Desejos)", wants)
        PercentageItem("Reserva / Investimentos", savings)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sugerimos 50/30/20, mas você pode personalizar depois.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Voltar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Próximo")
            }
        }
    }
}

@Composable
fun PercentageItem(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "$value%",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SummaryStep(
    income: String,
    needs: Int,
    wants: Int,
    savings: Int,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val incomeValue = income.toDoubleOrNull() ?: 0.0
    
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
                SummaryRow("Renda Mensal", "R$ %.2f".format(incomeValue))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SummaryRow("Essenciais ($needs%)", "R$ %.2f".format(incomeValue * needs / 100))
                SummaryRow("Estilo de Vida ($wants%)", "R$ %.2f".format(incomeValue * wants / 100))
                SummaryRow("Reserva ($savings%)", "R$ %.2f".format(incomeValue * savings / 100))
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
