package com.letusneil.godzilla.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.letusneil.godzilla.data.local.entity.ExerciseEntity
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.data.model.ExerciseSuggestion
import com.letusneil.godzilla.routines.RoutinesUiState
import com.letusneil.godzilla.routines.RoutinesViewModel
import com.letusneil.godzilla.search.SearchUiState
import com.letusneil.godzilla.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBottomSheet(
    onDismiss: () -> Unit,
    routinesViewModel: RoutinesViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(key = "routineSearch"),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uiState by routinesViewModel.uiState.collectAsStateWithLifecycle()
    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.query.collectAsStateWithLifecycle()
    var showCreateForm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        if (showCreateForm) {
            CreateRoutineForm(
                searchQuery = searchQuery,
                searchState = searchState,
                onQueryChange = searchViewModel::onQueryChange,
                onSave = { name, description, exercises ->
                    routinesViewModel.createRoutineWithExercises(name, description, exercises)
                    searchViewModel.onQueryChange("")
                    showCreateForm = false
                },
                onCancel = {
                    searchViewModel.onQueryChange("")
                    showCreateForm = false
                },
            )
        } else {
            RoutineListContent(
                uiState = uiState,
                onNewRoutine = { showCreateForm = true },
            )
        }
    }
}

@Composable
private fun RoutineListContent(
    uiState: RoutinesUiState,
    onNewRoutine: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Routines", style = MaterialTheme.typography.titleLarge)
            FloatingActionButton(onClick = onNewRoutine) {
                Icon(Icons.Default.Add, contentDescription = "New Routine")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is RoutinesUiState.Loading -> Text(
                text = "Loading…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            is RoutinesUiState.Success -> {
                if (uiState.routines.isEmpty()) {
                    Text(
                        text = "No routines yet. Tap + to create one.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.routines, key = { it.id }) { routine ->
                            RoutineCard(routine)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RoutineCard(routine: RoutineEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = routine.name, style = MaterialTheme.typography.titleSmall)
            if (routine.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = routine.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateRoutineForm(
    searchQuery: String,
    searchState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSave: (name: String, description: String, exercises: List<ExerciseEntity>) -> Unit,
    onCancel: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedExercises by remember { mutableStateOf(listOf<ExerciseEntity>()) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
    ) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            Text("New Routine", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = { onSave(name, description, selectedExercises) },
                enabled = name.isNotBlank(),
            ) { Text("Save") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selected exercises as dismissible chips
        if (selectedExercises.isNotEmpty()) {
            Text("Added", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectedExercises, key = { it.exerciseId }) { exercise ->
                    InputChip(
                        selected = true,
                        onClick = {
                            selectedExercises = selectedExercises.filter { it.exerciseId != exercise.exerciseId }
                        },
                        label = { Text(exercise.name) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.size(16.dp),
                            )
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Exercise search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search exercises…") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search results — fills all remaining vertical space
        Box(modifier = Modifier.weight(1f)) {
            when (val state = searchState) {
                is SearchUiState.Idle -> Text(
                    text = "Search above to add exercises to this routine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                is SearchUiState.Loading -> CircularProgressIndicator()
                is SearchUiState.Empty -> Text(
                    text = "No exercises found for \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                is SearchUiState.Error -> Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
                is SearchUiState.Success -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.results, key = { it.data.id }) { suggestion ->
                        val isSelected = selectedExercises.any { it.exerciseId == suggestion.data.baseId }
                        ExerciseResultItem(
                            suggestion = suggestion,
                            isSelected = isSelected,
                            onToggle = {
                                selectedExercises = if (isSelected) {
                                    selectedExercises.filter { it.exerciseId != suggestion.data.baseId }
                                } else {
                                    selectedExercises + ExerciseEntity(
                                        exerciseId = suggestion.data.baseId,
                                        name = suggestion.data.name,
                                        category = suggestion.data.category,
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ExerciseResultItem(
    suggestion: ExerciseSuggestion,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = suggestion.data.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = suggestion.data.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isSelected) "Added" else "Add",
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
