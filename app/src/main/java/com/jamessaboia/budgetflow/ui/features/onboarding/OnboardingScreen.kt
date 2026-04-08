package com.jamessaboia.budgetflow.ui.features.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.CurrencyVisualTransformation
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.StatusBarSpacer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isComplete) {
        LaunchedEffect(Unit) {
            onOnboardingComplete()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                if (uiState.step != OnboardingStep.INTRO_SLIDER) {
                    Column {
                        StatusBarSpacer()
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = { viewModel.previousStep() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    }
                }
            },
            bottomBar = {
                NavigationBarSpacer()
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (uiState.step == OnboardingStep.INTRO_SLIDER) PaddingValues(0.dp) else innerPadding)
            ) {
                AnimatedContent(
                    targetState = uiState.step, 
                    label = "OnboardingTransition",
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    }
                ) { step ->
                    when (step) {
                        OnboardingStep.INTRO_SLIDER -> IntroSlider(onFinish = { viewModel.nextStep() })
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
}

@Composable
fun IntroSlider(onFinish: () -> Unit) {
    val pages = listOf(
        IntroPageData(
            titleRes = R.string.intro_title,
            descRes = R.string.intro_desc,
            icon = Icons.Default.SelfImprovement
        ),
        IntroPageData(
            titleRes = R.string.planning_title,
            descRes = R.string.planning_desc,
            icon = Icons.Default.   Assignment
        ),
        IntroPageData(
            titleRes = R.string.cashflow_title,
            descRes = R.string.cashflow_desc,
            icon = Icons.Default.AccountBalanceWallet
        ),
        IntroPageData(
            titleRes = R.string.hints_title,
            descRes = R.string.hints_desc,
            icon = Icons.Default.Lightbulb
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) { pageIndex ->
            IntroPage(data = pages[pageIndex])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width by animateDpAsState(targetValue = if (isSelected) 24.dp else 8.dp, label = "dotWidth")
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                AnimatedContent(
                    targetState = pagerState.currentPage == pages.size - 1,
                    label = "ButtonTextTransition"
                ) { isLastPage ->
                    if (isLastPage) {
                        Text(stringResource(R.string.get_started), fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    }
}

data class IntroPageData(
    val titleRes: Int,
    val descRes: Int,
    val icon: ImageVector
)

@Composable
fun IntroPage(data: IntroPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = stringResource(data.titleRes),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 34.sp
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = stringResource(data.descRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 26.sp
        )
    }
}

@Composable
fun IncomeStep(
    income: String,
    onIncomeChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.income_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.income_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Importante",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.hint_income_planning),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        OutlinedTextField(
            value = income,
            onValueChange = { input ->
                val normalized = input.replace(",", ".")
                val filtered = normalized.filterIndexed { index, char ->
                    char.isDigit() || (char == '.' && normalized.indexOf('.') == index)
                }
                onIncomeChange(filtered)
            },
            label = { Text(stringResource(R.string.income_main_label)) },
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            visualTransformation = CurrencyVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            enabled = income.replace(",", ".").toDoubleOrNull() != null && income.replace(",", ".").toDouble() > 0
        ) {
            Text(stringResource(R.string.next), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(32.dp))
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.percentages_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.percentages_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
        
        Spacer(modifier = Modifier.height(32.dp))
        
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
            color = MaterialTheme.colorScheme.secondary,
            onValueChange = { onPercentagesChange(needs, it, savings) }
        )
        PercentageSlider(
            label = stringResource(R.string.group_savings),
            value = savings,
            hint = stringResource(R.string.hint_group_savings),
            color = MaterialTheme.colorScheme.tertiary,
            onValueChange = { onPercentagesChange(needs, wants, it) }
        )
        
        val totalPercent = needs + wants + savings
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            color = if (totalPercent == 100) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
            shape = CircleShape
        ) {
            Text(
                text = stringResource(R.string.total_label, totalPercent),
                style = MaterialTheme.typography.titleMedium,
                color = if (totalPercent == 100) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
        
        if (totalPercent != 100) {
            Text(
                text = stringResource(R.string.total_error),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Button(
            onClick = onNext, 
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            enabled = totalPercent == 100
        ) {
            Text(stringResource(R.string.next), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(32.dp))
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
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected, onClick = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description, 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 32.dp)
            )
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

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                IconButton(onClick = { showHint = !showHint }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Info, 
                        contentDescription = "Hint", 
                        modifier = Modifier.size(16.dp),
                        tint = color.copy(alpha = 0.6f)
                    )
                }
            }
            Text(
                text = stringResource(R.string.percentage_value, value),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        
        AnimatedVisibility(visible = showHint) {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                activeTrackColor = color,
                inactiveTrackColor = color.copy(alpha = 0.1f)
            )
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
    val totalIncome = income.replace(",", ".").toDoubleOrNull() ?: 0.0
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.summary_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryRow(stringResource(R.string.summary_income_total), stringResource(R.string.currency_format, totalIncome), isTotal = true)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SummaryRow(stringResource(R.string.group_needs_summary, needs), stringResource(R.string.currency_format, totalIncome * needs / 100))
                SummaryRow(stringResource(R.string.group_wants_summary, wants), stringResource(R.string.currency_format, totalIncome * wants / 100))
                SummaryRow(stringResource(R.string.group_savings_summary, savings), stringResource(R.string.currency_format, totalIncome * savings / 100))
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            Button(
                onClick = onConfirm, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(stringResource(R.string.finish), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label, 
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value, 
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge, 
            fontWeight = FontWeight.Black,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
