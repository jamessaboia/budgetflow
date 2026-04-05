package com.jamessaboia.budgetflow.ui.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.CurrencyVisualTransformation
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.StatusBarSpacer
import com.jamessaboia.budgetflow.ui.features.onboarding.PercentageSlider
import com.jamessaboia.budgetflow.ui.features.onboarding.PresetOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSettingsScreen(
    onBack: () -> Unit,
    onNavigateToCategories: () -> Unit,
    viewModel: BudgetSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    val successMessage = stringResource(R.string.update_success)
    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            snackbarHostState.showSnackbar(successMessage)
            viewModel.resetUpdateSuccess()
        }
    }

    Scaffold(
        topBar = {
            Column {
                StatusBarSpacer()
                TopAppBar(
                    title = { Text(stringResource(R.string.budget_settings)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBarSpacer()
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.baseIncome.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category Management Section
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToCategories() },
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = stringResource(R.string.manage_categories),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = stringResource(R.string.income_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = uiState.baseIncome,
                    onValueChange = { input ->
                        val normalized = input.replace(",", ".")
                        val filtered = normalized.filterIndexed { index, char ->
                            char.isDigit() || (char == '.' && normalized.indexOf('.') == index)
                        }
                        viewModel.onBaseIncomeChange(filtered)
                    },
                    label = { Text(stringResource(R.string.income_main_label)) },
                    prefix = { Text(stringResource(R.string.currency_prefix)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    visualTransformation = CurrencyVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.extraIncome,
                    onValueChange = { input ->
                        val normalized = input.replace(",", ".")
                        val filtered = normalized.filterIndexed { index, char ->
                            char.isDigit() || (char == '.' && normalized.indexOf('.') == index)
                        }
                        viewModel.onExtraIncomeChange(filtered)
                    },
                    label = { Text(stringResource(R.string.income_extra_label)) },
                    prefix = { Text(stringResource(R.string.currency_prefix)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    visualTransformation = CurrencyVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = stringResource(R.string.percentages_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PresetOption(
                        selected = uiState.needsPercent == 50 && uiState.wantsPercent == 30 && uiState.savingsPercent == 20,
                        title = stringResource(R.string.preset_balanced),
                        description = stringResource(R.string.preset_balanced_desc),
                        onClick = { viewModel.onPercentagesChange(50, 30, 20) }
                    )
                    PresetOption(
                        selected = uiState.needsPercent == 50 && uiState.wantsPercent == 25 && uiState.savingsPercent == 25,
                        title = stringResource(R.string.preset_accelerated),
                        description = stringResource(R.string.preset_accelerated_desc),
                        onClick = { viewModel.onPercentagesChange(50, 25, 25) }
                    )
                }

                PercentageSlider(
                    label = stringResource(R.string.group_needs),
                    value = uiState.needsPercent,
                    hint = stringResource(R.string.hint_group_needs),
                    color = MaterialTheme.colorScheme.primary,
                    onValueChange = { viewModel.onPercentagesChange(it, uiState.wantsPercent, uiState.savingsPercent) }
                )
                PercentageSlider(
                    label = stringResource(R.string.group_wants),
                    value = uiState.wantsPercent,
                    hint = stringResource(R.string.hint_group_lifestyle),
                    color = MaterialTheme.colorScheme.primary,
                    onValueChange = { viewModel.onPercentagesChange(uiState.needsPercent, it, uiState.savingsPercent) }
                )
                PercentageSlider(
                    label = stringResource(R.string.group_savings),
                    value = uiState.savingsPercent,
                    hint = stringResource(R.string.hint_group_savings),
                    color = MaterialTheme.colorScheme.primary,
                    onValueChange = { viewModel.onPercentagesChange(uiState.needsPercent, uiState.wantsPercent, it) }
                )

                val total = uiState.needsPercent + uiState.wantsPercent + uiState.savingsPercent
                Text(
                    text = stringResource(R.string.total_label, total),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (total == 100) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Button(
                    onClick = viewModel::saveChanges,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = total == 100 && uiState.isDirty && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(stringResource(R.string.save_changes))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
