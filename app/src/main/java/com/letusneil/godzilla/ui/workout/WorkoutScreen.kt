package com.letusneil.godzilla.ui.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.letusneil.godzilla.exercises.ExercisesUiState
import com.letusneil.godzilla.exercises.ExercisesViewModel
import org.koin.androidx.compose.koinViewModel

const val WORKOUT_ROUTE = "workout"

fun NavGraphBuilder.workoutRoute() {
    composable(WORKOUT_ROUTE) {
        WorkoutScreen()
    }
}

@Composable
fun WorkoutScreen(
    viewModel: ExercisesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val state = uiState) {
            is ExercisesUiState.Loading -> CircularProgressIndicator()
            is ExercisesUiState.Error -> Text(text = state.message)
            is ExercisesUiState.Success -> Text(text = "${state.exercises.size} exercises loaded")
        }
    }
}
