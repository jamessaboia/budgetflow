package com.jamessaboia.budgetflow.ui.features.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.getCategoryDisplayName
import com.jamessaboia.budgetflow.domain.model.TransactionType
import com.jamessaboia.budgetflow.domain.model.TransactionWithCategory
import com.valentinilk.shimmer.shimmer
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onBack: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.isLoading) {
                TransactionsSkeleton()
            } else if (uiState.transactions.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_transactions),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.transactions) { transactionWithCategory ->
                        TransactionItem(
                            transactionWithCategory = transactionWithCategory,
                            onDelete = { viewModel.deleteTransaction(transactionWithCategory) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionsSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(6) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {}
        }
    }
}

@Composable
fun TransactionItem(
    transactionWithCategory: TransactionWithCategory,
    onDelete: () -> Unit
) {
    val transaction = transactionWithCategory.transaction
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (transaction.description.isNotBlank()) transaction.description else stringResource(R.string.no_description),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getCategoryDisplayName(transactionWithCategory.categoryName),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaction.date),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (transaction.type == TransactionType.INCOME) {
                        stringResource(R.string.transaction_income_format, transaction.amount)
                    } else {
                        stringResource(R.string.transaction_expense_format, transaction.amount)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
