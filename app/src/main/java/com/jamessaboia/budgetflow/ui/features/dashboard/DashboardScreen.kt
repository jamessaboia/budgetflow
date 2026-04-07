package com.jamessaboia.budgetflow.ui.features.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.MonthPicker
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.getCategoryDisplayName
import com.jamessaboia.budgetflow.domain.model.DashboardSummary
import com.jamessaboia.budgetflow.domain.model.GroupSummary
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val listState = rememberLazyListState()
    
    var previousIndex by remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
    var previousScrollOffset by remember { mutableIntStateOf(listState.firstVisibleItemScrollOffset) }
    
    val isScrollingUp = remember {
        derivedStateOf {
            if (previousIndex != listState.firstVisibleItemIndex) {
                previousIndex > listState.firstVisibleItemIndex
            } else {
                previousScrollOffset >= listState.firstVisibleItemScrollOffset
            }.also {
                previousIndex = listState.firstVisibleItemIndex
                previousScrollOffset = listState.firstVisibleItemScrollOffset
            }
        }
    }.value

    val isAtTop = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }.value

    val isFabVisible = isAtTop || isScrollingUp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = (-0.5).sp
                    ) 
                },
                actions = {
                    IconButton(onClick = onNavigateToTransactions) {
                        Icon(
                            Icons.AutoMirrored.Filled.List, 
                            contentDescription = stringResource(R.string.history),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings, 
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToAddTransaction,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    expanded = isAtTop,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text(stringResource(R.string.add_transaction_fab), fontWeight = FontWeight.Bold) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = {
            NavigationBarSpacer()
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.isLoading) {
                DashboardSkeleton(
                    selectedMonth = selectedMonth,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth
                )
            } else if (uiState.error != null) {
                Text(
                    text = stringResource(R.string.error_generic, uiState.error!!),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.summary != null) {
                DashboardContent(
                    summary = uiState.summary!!,
                    selectedMonth = selectedMonth,
                    isBalanceVisible = uiState.isBalanceVisible,
                    listState = listState,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth,
                    onToggleBalanceVisibility = viewModel::toggleBalanceVisibility
                )
            }
        }
    }
}

@Composable
fun DashboardSkeleton(
    selectedMonth: String,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            shadowElevation = 4.dp
        ) {
            Column {
                MonthPicker(
                    selectedMonth = selectedMonth,
                    onPreviousMonth = onPreviousMonth,
                    onNextMonth = onNextMonth,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .shimmer()
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(24.dp),
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {}
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            repeat(3) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(100.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {}
            }
        }
    }
}

@Composable
fun DashboardContent(
    summary: DashboardSummary,
    selectedMonth: String,
    isBalanceVisible: Boolean,
    listState: LazyListState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToggleBalanceVisibility: () -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                shadowElevation = 4.dp
            ) {
                Column {
                    MonthPicker(
                        selectedMonth = selectedMonth,
                        onPreviousMonth = onPreviousMonth,
                        onNextMonth = onNextMonth,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                    BalanceHeader(
                        summary = summary,
                        isVisible = isBalanceVisible,
                        onToggleVisibility = onToggleBalanceVisibility
                    )
                }
            }
        }

        item {
            var showOverviewHint by remember { mutableStateOf(false) }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.my_groups),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { showOverviewHint = !showOverviewHint }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Overview Info",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                }
                
                AnimatedVisibility(visible = showOverviewHint) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.hint_groups_overview),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                GroupCard(
                    title = stringResource(R.string.group_needs), 
                    summary = summary.needsSummary, 
                    hint = stringResource(R.string.hint_group_needs),
                    isBalanceVisible = isBalanceVisible
                )
                Spacer(modifier = Modifier.height(12.dp))
                GroupCard(
                    title = stringResource(R.string.group_wants), 
                    summary = summary.wantsSummary, 
                    hint = stringResource(R.string.hint_group_lifestyle),
                    isBalanceVisible = isBalanceVisible
                )
                Spacer(modifier = Modifier.height(12.dp))
                GroupCard(
                    title = stringResource(R.string.group_savings), 
                    summary = summary.savingsSummary, 
                    hint = stringResource(R.string.hint_group_savings),
                    isBalanceVisible = isBalanceVisible
                )
            }
        }
    }
}

@Composable
fun BalanceHeader(
    summary: DashboardSummary,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onToggleVisibility, 
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle Visibility",
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = stringResource(R.string.available_balance),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
        
        AnimatedContent(
            targetState = if (isVisible) summary.remainingBalance else null,
            transitionSpec = {
                fadeIn(animationSpec = tween(1000, easing = FastOutSlowInEasing))
                    .togetherWith(fadeOut(animationSpec = tween(800)))
            }, label = "BalanceAnimation"
        ) { balance ->
            Text(
                text = if (balance != null) stringResource(R.string.currency_format, balance) else "R$ •••••",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary,
                letterSpacing = (-1).sp
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(
                label = stringResource(R.string.income_label),
                value = if (isVisible) stringResource(R.string.currency_format, summary.actualIncome) else "••••",
                icon = Icons.Default.ArrowUpward,
                iconColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            
            SummaryCard(
                label = stringResource(R.string.spent_label),
                value = if (isVisible) stringResource(R.string.currency_format, summary.totalSpent) else "••••",
                icon = Icons.Default.ArrowDownward,
                iconColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(iconColor.copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    color = iconColor,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun GroupCard(
    title: String, 
    summary: GroupSummary, 
    hint: String,
    isBalanceVisible: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    val isExceeded = summary.percentageSpent > 1f
    val mainColor = if (isExceeded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (summary.categorySpending.isNotEmpty()) expanded = !expanded },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title, 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { showHint = !showHint }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                    if (summary.categorySpending.isNotEmpty()) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.percentage_value, (summary.percentageSpent * 100).toInt()),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = mainColor
                )
            }
            
            AnimatedVisibility(visible = showHint) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            val progress = summary.percentageSpent.coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = mainColor,
                trackColor = mainColor.copy(alpha = 0.1f),
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val spentValue = if (isBalanceVisible) stringResource(R.string.currency_format, summary.spent) else "••••"
                val limitValue = if (isBalanceVisible) stringResource(R.string.currency_format, summary.limit) else "••••"
                
                Column {
                    Text(
                        text = stringResource(R.string.spent_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = spentValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Black
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.limit_value, ""),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = limitValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            val remainingValue = if (isBalanceVisible) stringResource(R.string.currency_format, if (isExceeded) -summary.remaining else summary.remaining) else "••••"
            
            Surface(
                color = if (isExceeded) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else mainColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = if (isExceeded) stringResource(R.string.exceeded_by, remainingValue) else stringResource(R.string.remains_value, remainingValue),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isExceeded) MaterialTheme.colorScheme.error else mainColor,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    summary.categorySpending.forEach { categorySpent ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = com.jamessaboia.budgetflow.core.getCategoryDisplayName(categorySpent.categoryName),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isBalanceVisible) stringResource(R.string.currency_format, categorySpent.amount) else "••••",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatCurrency(value: Double): String {
    return "R$ %.2f".format(value)
}
