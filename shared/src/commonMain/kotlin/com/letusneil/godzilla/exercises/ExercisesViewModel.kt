package com.letusneil.godzilla.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.letusneil.godzilla.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val repository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExercisesUiState>(ExercisesUiState.Loading)
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    fun retry() {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = ExercisesUiState.Loading
            _uiState.value = try {
                val exercises = repository.getExercises()
                ExercisesUiState.Success(exercises)
            } catch (e: Exception) {
                ExercisesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
