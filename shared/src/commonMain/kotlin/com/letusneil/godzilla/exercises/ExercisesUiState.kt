package com.letusneil.godzilla.exercises

import com.letusneil.godzilla.data.model.ExerciseInfo

sealed class ExercisesUiState {
    data object Loading : ExercisesUiState()
    data class Success(val exercises: List<ExerciseInfo>) : ExercisesUiState()
    data class Error(val message: String) : ExercisesUiState()
}
