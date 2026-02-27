package com.letusneil.godzilla.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.letusneil.godzilla.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val DEBOUNCE_MS = 400L

class SearchViewModel(private val repository: ExerciseRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(DEBOUNCE_MS)
                .distinctUntilChanged()
                .collectLatest { term ->
                    if (term.isBlank()) {
                        _uiState.value = SearchUiState.Idle
                    } else {
                        _uiState.value = SearchUiState.Loading
                        _uiState.value = try {
                            val results = repository.searchExercises(term)
                            if (results.isEmpty()) SearchUiState.Empty
                            else SearchUiState.Success(results)
                        } catch (e: Exception) {
                            SearchUiState.Error(e.message ?: "Search failed")
                        }
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }
}
