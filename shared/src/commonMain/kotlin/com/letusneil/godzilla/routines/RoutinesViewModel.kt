package com.letusneil.godzilla.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.letusneil.godzilla.data.local.entity.ExerciseEntity
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.data.repository.RoutineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutinesViewModel(private val repository: RoutineRepository) : ViewModel() {

    val uiState: StateFlow<RoutinesUiState> = repository.getAllRoutines()
        .map { RoutinesUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RoutinesUiState.Loading,
        )

    fun createRoutine(name: String, description: String) {
        viewModelScope.launch {
            repository.createRoutine(name, description)
        }
    }

    fun createRoutineWithExercises(name: String, description: String, exercises: List<ExerciseEntity>) {
        viewModelScope.launch {
            val routineId = repository.createRoutine(name, description)
            exercises.forEach { exercise ->
                repository.addExerciseToRoutine(routineId, exercise)
            }
        }
    }

    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun addExercise(routineId: Long, exercise: ExerciseEntity) {
        viewModelScope.launch {
            repository.addExerciseToRoutine(routineId, exercise)
        }
    }

    fun removeExercise(routineId: Long, exerciseId: Int) {
        viewModelScope.launch {
            repository.removeExerciseFromRoutine(routineId, exerciseId)
        }
    }
}
