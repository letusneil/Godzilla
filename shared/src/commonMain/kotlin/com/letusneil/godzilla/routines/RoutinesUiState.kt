package com.letusneil.godzilla.routines

import com.letusneil.godzilla.data.local.entity.RoutineEntity

sealed interface RoutinesUiState {
    data object Loading : RoutinesUiState
    data class Success(val routines: List<RoutineEntity>) : RoutinesUiState
}
