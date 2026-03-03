package com.letusneil.godzilla.ui.workout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
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

private sealed interface RoutineSheetScreen {
    data object List : RoutineSheetScreen
    data object Create : RoutineSheetScreen
    data class Detail(val routineId: Long) : RoutineSheetScreen
    data class AddExercise(val routineId: Long) : RoutineSheetScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBottomSheet(
    onDismiss: () -> Unit,
    routinesViewModel: RoutinesViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(key = "routineSearch"),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val uiState by routinesViewModel.uiState.collectAsStateWithLifecycle()
    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.query.collectAsStateWithLifecycle()
    var screen by remember { mutableStateOf<RoutineSheetScreen>(RoutineSheetScreen.List) }

    LaunchedEffect(screen) {
        when (screen) {
            is RoutineSheetScreen.Create,
            is RoutineSheetScreen.Detail,
            is RoutineSheetScreen.AddExercise -> sheetState.expand()
            is RoutineSheetScreen.List -> if (sheetState.isVisible) sheetState.partialExpand()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when {
                    // + tapped → Create: new content slides up from below
                    targetState is RoutineSheetScreen.Create ->
                        (slideInVertically { it } + fadeIn()) togetherWith fadeOut()

                    // Detail → AddExercise: forward slide (left)
                    targetState is RoutineSheetScreen.AddExercise ->
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())

                    // List → Detail: forward slide (left)
                    targetState is RoutineSheetScreen.Detail && initialState is RoutineSheetScreen.List ->
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())

                    // Cancel/Save from Create → List: form slides back down
                    initialState is RoutineSheetScreen.Create ->
                        fadeIn() togetherWith (slideOutVertically { it } + fadeOut())

                    // Back from Detail → List: reverse slide (right)
                    else ->
                        (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                }
            },
            label = "routineSheet",
        ) { current ->
            when (current) {
                is RoutineSheetScreen.List -> RoutineListContent(
                    uiState = uiState,
                    onNewRoutine = { screen = RoutineSheetScreen.Create },
                    onRoutineClick = { routine -> screen = RoutineSheetScreen.Detail(routine.id) },
                )

                is RoutineSheetScreen.Create -> CreateRoutineForm(
                    searchQuery = searchQuery,
                    searchState = searchState,
                    onQueryChange = searchViewModel::onQueryChange,
                    onSave = { name, description, exercises ->
                        routinesViewModel.createRoutineWithExercises(name, description, exercises)
                        searchViewModel.onQueryChange("")
                        screen = RoutineSheetScreen.List
                    },
                    onCancel = {
                        searchViewModel.onQueryChange("")
                        screen = RoutineSheetScreen.List
                    },
                )

                is RoutineSheetScreen.Detail -> RoutineDetailContent(
                    routineId = current.routineId,
                    routinesViewModel = routinesViewModel,
                    onBack = { screen = RoutineSheetScreen.List },
                    onAddExercise = { screen = RoutineSheetScreen.AddExercise(current.routineId) },
                )

                is RoutineSheetScreen.AddExercise -> AddExerciseContent(
                    routineId = current.routineId,
                    routinesViewModel = routinesViewModel,
                    searchQuery = searchQuery,
                    searchState = searchState,
                    onQueryChange = searchViewModel::onQueryChange,
                    onBack = {
                        searchViewModel.onQueryChange("")
                        screen = RoutineSheetScreen.Detail(current.routineId)
                    },
                )
            }
        }
    }
}

// ── List ─────────────────────────────────────────────────────────────────────

@Composable
private fun RoutineListContent(
    uiState: RoutinesUiState,
    onNewRoutine: () -> Unit,
    onRoutineClick: (RoutineEntity) -> Unit,
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
                            RoutineCard(routine = routine, onClick = { onRoutineClick(routine) })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RoutineCard(routine: RoutineEntity, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
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

// ── Detail ────────────────────────────────────────────────────────────────────

@Composable
private fun RoutineDetailContent(
    routineId: Long,
    routinesViewModel: RoutinesViewModel,
    onBack: () -> Unit,
    onAddExercise: () -> Unit,
) {
    val routineWithExercises by routinesViewModel
        .getRoutineWithExercises(routineId)
        .collectAsStateWithLifecycle(initialValue = null)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
    ) {
        // Back navigation header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text(
                text = routineWithExercises?.routine?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        val detail = routineWithExercises
        if (detail == null) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (detail.routine.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = detail.routine.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Exercises (${detail.exercises.size})",
                    style = MaterialTheme.typography.titleSmall,
                )
                IconButton(onClick = onAddExercise) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add exercises",
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (detail.exercises.isEmpty()) {
                Text(
                    text = "No exercises added yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(detail.exercises, key = { it.exerciseId }) { exercise ->
                        ExerciseItem(exercise)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ExerciseItem(exercise: ExerciseEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleSmall)
            Text(
                text = exercise.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Add Exercise ──────────────────────────────────────────────────────────────

@Composable
private fun AddExerciseContent(
    routineId: Long,
    routinesViewModel: RoutinesViewModel,
    searchQuery: String,
    searchState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    val routineWithExercises by routinesViewModel
        .getRoutineWithExercises(routineId)
        .collectAsStateWithLifecycle(initialValue = null)
    val currentExerciseIds = routineWithExercises?.exercises?.map { it.exerciseId }.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text("Add Exercises", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(8.dp))

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

        Box(modifier = Modifier.weight(1f)) {
            when (val state = searchState) {
                is SearchUiState.Idle -> Text(
                    text = "Search above to add exercises",
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
                        val isAdded = suggestion.data.baseId in currentExerciseIds
                        ExerciseResultItem(
                            suggestion = suggestion,
                            isSelected = isAdded,
                            onToggle = {
                                if (isAdded) {
                                    routinesViewModel.removeExercise(routineId, suggestion.data.baseId)
                                } else {
                                    routinesViewModel.addExercise(
                                        routineId,
                                        ExerciseEntity(
                                            exerciseId = suggestion.data.baseId,
                                            name = suggestion.data.name,
                                            category = suggestion.data.category,
                                        ),
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

// ── Create ────────────────────────────────────────────────────────────────────

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
