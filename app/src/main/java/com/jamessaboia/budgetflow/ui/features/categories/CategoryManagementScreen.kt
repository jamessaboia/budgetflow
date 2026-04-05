package com.jamessaboia.budgetflow.ui.features.categories

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.StatusBarSpacer
import com.jamessaboia.budgetflow.core.getCategoryDescription
import com.jamessaboia.budgetflow.core.getCategoryDisplayName
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    onBack: () -> Unit,
    viewModel: CategoryManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddEditDialog by remember { mutableStateOf<Category?>(null) }
    var isAdding by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val successMessage = stringResource(R.string.category_saved)
    val deletedMessage = stringResource(R.string.category_deleted)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                CategoryUiEffect.ShowSuccessMessage -> snackbarHostState.showSnackbar(successMessage)
                CategoryUiEffect.ShowDeletedMessage -> snackbarHostState.showSnackbar(deletedMessage)
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                StatusBarSpacer()
                TopAppBar(
                    title = { Text(stringResource(R.string.manage_categories)) },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAdding = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_category))
            }
        },
        bottomBar = {
            NavigationBarSpacer()
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val groupedCategories = uiState.categories.groupBy { it.groupType }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BudgetGroup.entries.forEach { group ->
                        val categoriesInGroup = groupedCategories[group] ?: emptyList()
                        
                        item(key = group.name) {
                            Text(
                                text = when (group) {
                                    BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
                                    BudgetGroup.WANTS -> stringResource(R.string.group_wants)
                                    BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(categoriesInGroup, key = { it.id }) { category ->
                            CategoryItem(
                                category = category,
                                onEdit = { showAddEditDialog = category },
                                onDelete = { viewModel.onDeleteClick(category) },
                                modifier = Modifier.animateItem()
                            )
                        }
                        
                        if (categoriesInGroup.isEmpty()) {
                            item(key = "${group.name}_empty") {
                                Text(
                                    text = "Nenhuma categoria neste grupo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    
    if (isAdding || showAddEditDialog != null) {
        AddEditCategoryDialog(
            category = showAddEditDialog,
            onDismiss = {
                isAdding = false
                showAddEditDialog = null
            },
            onConfirm = { name, description, group ->
                if (showAddEditDialog != null) {
                    viewModel.onEditCategory(showAddEditDialog!!, name, description, group)
                } else {
                    viewModel.onAddCategory(name, description, group)
                }
                isAdding = false
                showAddEditDialog = null
            }
        )
    }

    
    uiState.showDeleteWarning?.let { category ->
        AlertDialog(
            onDismissRequest = viewModel::dismissDeleteWarning,
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.delete_category_warning)) },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDelete(category) }) {
                    Text(stringResource(R.string.delete_confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteWarning) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getCategoryDisplayName(category.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_category), tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete), tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            val displayDescription = getCategoryDescription(category.description)
            if (!displayDescription.isNullOrBlank()) {
                Text(
                    text = displayDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AddEditCategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String, String?, BudgetGroup) -> Unit
) {
    val initialName = category?.name?.let { if (it.startsWith("cat_")) "" else it } ?: ""
    val initialDescription = getCategoryDescription(category?.description) ?: ""
    
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedGroup by remember { mutableStateOf(category?.groupType ?: BudgetGroup.NEEDS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) stringResource(R.string.add_category) else stringResource(R.string.edit_category)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.category_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.category_description_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3
                )
                
                Text(stringResource(R.string.select_group), style = MaterialTheme.typography.labelMedium)
                
                BudgetGroup.entries.forEach { group ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedGroup = group }
                    ) {
                        RadioButton(selected = selectedGroup == group, onClick = { selectedGroup = group })
                        Text(
                            text = when (group) {
                                BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
                                BudgetGroup.WANTS -> stringResource(R.string.group_wants)
                                BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description.ifBlank { null }, selectedGroup) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
