package com.jamessaboia.budgetflow.ui.features.categories

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jamessaboia.budgetflow.R
import com.jamessaboia.budgetflow.core.NavigationBarSpacer
import com.jamessaboia.budgetflow.core.StatusBarSpacer
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.Category
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCategoryListScreen(
    groupName: String,
    onBack: () -> Unit,
    viewModel: CategoryManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val group = remember { BudgetGroup.valueOf(groupName) }
    var showAddEditDialog by remember { mutableStateOf<Category?>(null) }
    var isAdding by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val groupDisplayName = when (group) {
        BudgetGroup.NEEDS -> stringResource(R.string.group_needs)
        BudgetGroup.WANTS -> stringResource(R.string.group_wants)
        BudgetGroup.SAVINGS -> stringResource(R.string.group_savings)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                CategoryUiEffect.ShowSuccessMessage -> snackbarHostState.showSnackbar(groupDisplayName + ": Categoria salva")
                CategoryUiEffect.ShowDeletedMessage -> snackbarHostState.showSnackbar(groupDisplayName + ": Categoria excluída")
            }
        }
    }

    // FAB Behavior Logic
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

    Scaffold(
        topBar = {
            Column {
                StatusBarSpacer()
                TopAppBar(
                    title = { Text(stringResource(R.string.all_categories_in, groupDisplayName), fontWeight = FontWeight.Black) },
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
            val categoriesInGroup = uiState.categories.filter { it.groupType == group }
            
            if (categoriesInGroup.isEmpty()) {
                EmptyGroupPlaceholder()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val rows = categoriesInGroup.chunked(2)
                    items(rows) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { category ->
                                CategoryTile(
                                    category = category,
                                    onEdit = { showAddEditDialog = category },
                                    onDelete = { viewModel.onDeleteClick(category) },
                                    modifier = Modifier.weight(1f).animateItem()
                                )
                            }
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
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
            lockedGroup = group, // LOCK the group here
            onDismiss = {
                isAdding = false
                showAddEditDialog = null
            },
            onConfirm = { name, description, selectedGroup ->
                if (showAddEditDialog != null) {
                    viewModel.onEditCategory(showAddEditDialog!!, name, description, selectedGroup)
                } else {
                    viewModel.onAddCategory(name, description, selectedGroup)
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
