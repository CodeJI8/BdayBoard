package com.wishINC.bdayboard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.wishINC.bdayboard.data.Birthday
import com.wishINC.bdayboard.ui.components.AddEditBirthdayDialog
import com.wishINC.bdayboard.ui.components.BirthdayCard
import com.wishINC.bdayboard.ui.components.DeleteConfirmDialog
import com.wishINC.bdayboard.ui.components.EmptyState
import com.wishINC.bdayboard.ui.components.NextBirthdayCard
import com.wishINC.bdayboard.viewmodel.BirthdayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BirthdayViewModel
) {
    val displayList by viewModel.displayList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingBirthday by remember { mutableStateOf<Birthday?>(null) }
    var deletingBirthday by remember { mutableStateOf<Birthday?>(null) }
    var showSearch by remember { mutableStateOf(false) }

    // Refresh date calculations every time the lifecycle resumes
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.refreshToday()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showSearch) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = { Text("Search names...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Text("\uD83C\uDF82 Bday Board")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSearch = !showSearch
                        if (!showSearch) viewModel.onSearchQueryChanged("")
                    }) {
                        Icon(
                            imageVector = if (showSearch) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (showSearch) "Close search" else "Search"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Birthday",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (displayList.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Next birthday highlight card
                    item {
                        NextBirthdayCard(
                            info = displayList.first(),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // All birthdays
                    items(
                        items = displayList,
                        key = { it.birthday.id }
                    ) { info ->
                        BirthdayCard(
                            info = info,
                            onEdit = { editingBirthday = info.birthday },
                            onSwipeToDelete = { deletingBirthday = info.birthday }
                        )
                    }
                }
            }
        }
    }

    // Add dialog
    if (showAddDialog) {
        AddEditBirthdayDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, day, month, year ->
                viewModel.addBirthday(name, day, month, year)
                showAddDialog = false
            }
        )
    }

    // Edit dialog
    editingBirthday?.let { birthday ->
        AddEditBirthdayDialog(
            birthdayToEdit = birthday,
            onDismiss = { editingBirthday = null },
            onSave = { name, day, month, year ->
                viewModel.updateBirthday(
                    birthday.copy(name = name, day = day, month = month, year = year)
                )
                editingBirthday = null
            }
        )
    }

    // Delete confirm dialog
    deletingBirthday?.let { birthday ->
        DeleteConfirmDialog(
            name = birthday.name,
            onConfirm = {
                viewModel.deleteBirthday(birthday)
                deletingBirthday = null
            },
            onDismiss = { deletingBirthday = null }
        )
    }
}
