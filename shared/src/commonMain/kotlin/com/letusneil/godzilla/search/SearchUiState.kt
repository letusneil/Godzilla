package com.letusneil.godzilla.search

import com.letusneil.godzilla.data.model.ExerciseSuggestion

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data object Empty : SearchUiState()
    data class Success(val results: List<ExerciseSuggestion>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
