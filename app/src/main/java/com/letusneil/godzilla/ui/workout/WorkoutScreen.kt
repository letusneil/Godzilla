package com.letusneil.godzilla.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.routines.RoutinesUiState
import com.letusneil.godzilla.routines.RoutinesViewModel
import org.koin.androidx.compose.koinViewModel

const val WORKOUT_ROUTE = "workout"

fun NavGraphBuilder.workoutRoute() {
    composable(WORKOUT_ROUTE) {
        WorkoutScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: RoutinesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var sheetEntry by remember { mutableStateOf<RoutineSheetEntry?>(null) }

    sheetEntry?.let { entry ->
        RoutineBottomSheet(
            entry = entry,
            onDismiss = { sheetEntry = null },
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Workout") }) },
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Button(
                    onClick = { sheetEntry = RoutineSheetEntry.Create },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Create a new Routine")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            when (val state = uiState) {
                is RoutinesUiState.Loading -> Unit

                is RoutinesUiState.Success -> {
                    if (state.routines.isEmpty()) {
                        item {
                            Text(
                                text = "No routines yet. Tap the button above to create one.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        items(state.routines, key = { it.id }) { routine ->
                            RoutineItem(
                                routine = routine,
                                onClick = { sheetEntry = RoutineSheetEntry.Detail(routine.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineItem(routine: RoutineEntity, onClick: () -> Unit) {
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
