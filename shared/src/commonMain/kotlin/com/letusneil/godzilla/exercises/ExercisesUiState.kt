package com.letusneil.godzilla.exercises

import com.letusneil.godzilla.data.model.Exercise


sealed class ExercisesUiState {
    data object Loading : ExercisesUiState()
    data class Success(val exercises: List<Exercise>) : ExercisesUiState()
    data class Error(val message: String) : ExercisesUiState()
}
