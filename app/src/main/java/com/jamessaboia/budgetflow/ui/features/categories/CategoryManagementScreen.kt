package com.jamessaboia.budgetflow.ui.features.categories

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.StatusBarSpacer
import com.jamessaboia.budgetflow.core.getCategoryDescription
import com.jamessaboia.budgetflow.core.getCategoryDisplayName
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    onBack: () -> Unit,
    onNavigateToGroup: (String) -> Unit,
    viewModel: CategoryManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddEditDialog by remember { mutableStateOf<Category?>(null) }
    var isAdding by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val successMessage = stringResource(R.string.category_saved)
    val deletedMessage = stringResource(R.string.category_deleted)

    // Scroll Logic for FAB
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
        derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
    }.value
    val isFabVisible = isAtTop || isScrollingUp

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
                    title = { Text(stringResource(R.string.manage_categories), fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { isAdding = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    expanded = isAtTop,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text(stringResource(R.string.add_category), fontWeight = FontWeight.Bold) }
                )
            }
        },
        bottomBar = { NavigationBarSpacer() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else {
                val groupedCategories = uiState.categories.groupBy { it.groupType }
                
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BudgetGroup.entries.forEach { group ->
                        val allCategories = groupedCategories[group] ?: emptyList()
                        val visibleCategories = allCategories.take(3)
                        val hasMore = allCategories.size > 3
                        
                        item(key = group.name) {
                            CategoryGroupHeader(
                                title = when (group) {
                                    BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
                                    BudgetGroup.WANTS -> stringResource(R.string.group_wants)
                                    BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
                                }
                            )
                        }
                        
                        item(key = "${group.name}_grid") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                // First Row (Items 1 & 2)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    if (visibleCategories.isNotEmpty()) {
                                        CategoryTile(
                                            category = visibleCategories[0],
                                            onEdit = { showAddEditDialog = visibleCategories[0] },
                                            onDelete = { viewModel.onDeleteClick(visibleCategories[0]) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    
                                    if (visibleCategories.size > 1) {
                                        CategoryTile(
                                            category = visibleCategories[1],
                                            onEdit = { showAddEditDialog = visibleCategories[1] },
                                            onDelete = { viewModel.onDeleteClick(visibleCategories[1]) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                
                                // Second Row (Item 3 & "See More" or Spacer)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    if (visibleCategories.size > 2) {
                                        CategoryTile(
                                            category = visibleCategories[2],
                                            onEdit = { showAddEditDialog = visibleCategories[2] },
                                            onDelete = { viewModel.onDeleteClick(visibleCategories[2]) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else if (visibleCategories.isNotEmpty()) {
                                        // Just a spacer if we have some categories but not 3
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    
                                    if (hasMore) {
                                        SeeMoreTile(
                                            onClick = { onNavigateToGroup(group.name) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else if (visibleCategories.size > 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        
                        if (visibleCategories.isEmpty()) {
                            item(key = "${group.name}_empty") {
                                EmptyGroupPlaceholder()
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
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
            shape = RoundedCornerShape(28.dp),
            title = { Text(stringResource(R.string.delete), fontWeight = FontWeight.Black) },
            text = { Text(stringResource(R.string.delete_category_warning), lineHeight = 22.sp) },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDelete(category) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.delete_confirm), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteWarning) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
fun SeeMoreTile(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.see_all_categories),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CategoryGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp),
        letterSpacing = 0.5.sp
    )
}

@Composable
fun EmptyGroupPlaceholder() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Nenhuma categoria personalizada para este grupo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryTile(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onEdit() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = getCategoryDisplayName(category.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val displayDescription = getCategoryDescription(category.description)
                if (!displayDescription.isNullOrBlank()) {
                    Text(
                        text = displayDescription,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete, 
                        contentDescription = stringResource(R.string.delete), 
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Edit, 
                            contentDescription = stringResource(R.string.edit_category), 
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditCategoryDialog(
    category: Category?,
    lockedGroup: BudgetGroup? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String?, BudgetGroup) -> Unit
) {
    val initialName = category?.let { getCategoryDisplayName(it.name) } ?: ""
    val initialDescription = getCategoryDescription(category?.description) ?: ""
    
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedGroup by remember { mutableStateOf(lockedGroup ?: category?.groupType ?: BudgetGroup.NEEDS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = { 
            Text(
                if (category == null) stringResource(R.string.add_category) else stringResource(R.string.edit_category),
                fontWeight = FontWeight.Black
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.category_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.category_description_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
                
                if (lockedGroup == null) {
                    Text(
                        stringResource(R.string.select_group), 
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        BudgetGroup.entries.forEach { group ->
                            val isSelected = selectedGroup == group
                            Surface(
                                onClick = { selectedGroup = group },
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    RadioButton(selected = isSelected, onClick = null)
                                    Text(
                                        text = when (group) {
                                            BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
                                            BudgetGroup.WANTS -> stringResource(R.string.group_wants)
                                            BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Show locked group info
                    val groupName = when (lockedGroup) {
                        BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
                        BudgetGroup.WANTS -> stringResource(R.string.group_wants)
                        BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Grupo: $groupName",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description.ifBlank { null }, selectedGroup) },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(stringResource(R.string.confirm), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
