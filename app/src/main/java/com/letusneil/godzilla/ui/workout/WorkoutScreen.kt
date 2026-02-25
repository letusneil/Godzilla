package com.letusneil.godzilla.ui.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val WORKOUT_ROUTE = "workout"

fun NavGraphBuilder.workoutRoute() {
    composable(WORKOUT_ROUTE) {
        WorkoutScreen()
    }
}

@Composable
fun WorkoutScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Workout")
    }
}