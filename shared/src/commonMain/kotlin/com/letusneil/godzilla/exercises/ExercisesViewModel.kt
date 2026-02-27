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

    private var currentPage = 0

    private val _uiState = MutableStateFlow<ExercisesUiState>(ExercisesUiState.Loading)
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    fun retry() {
        currentPage = 0
        loadExercises()
    }

    fun loadNextPage() {
        val current = _uiState.value as? ExercisesUiState.Success ?: return
        if (current.isLoadingMore || !current.canLoadMore) return
        viewModelScope.launch {
            _uiState.value = current.copy(isLoadingMore = true)
            try {
                currentPage++
                val page = repository.getExercises(page = currentPage)
                _uiState.value = current.copy(
                    exercises = current.exercises + page.exercises,
                    isLoadingMore = false,
                    canLoadMore = page.hasMore,
                )
            } catch (e: Exception) {
                currentPage--
                _uiState.value = current.copy(isLoadingMore = false)
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = ExercisesUiState.Loading
            _uiState.value = try {
                val page = repository.getExercises(page = 0)
                ExercisesUiState.Success(
                    exercises = page.exercises,
                    canLoadMore = page.hasMore,
                )
            } catch (e: Exception) {
                ExercisesUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
