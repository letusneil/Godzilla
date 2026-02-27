package com.letusneil.godzilla.data.repository

import com.letusneil.godzilla.data.local.dao.ExerciseDao
import com.letusneil.godzilla.data.local.dao.RoutineDao
import com.letusneil.godzilla.data.local.entity.ExerciseEntity
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.data.local.entity.RoutineExerciseCrossRef
import com.letusneil.godzilla.data.local.entity.RoutineWithExercises
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<RoutineEntity>>
    suspend fun createRoutine(name: String, description: String): Long
    suspend fun deleteRoutine(routine: RoutineEntity)
    suspend fun addExerciseToRoutine(routineId: Long, exercise: ExerciseEntity)
    suspend fun removeExerciseFromRoutine(routineId: Long, exerciseId: Int)
    fun getRoutineWithExercises(routineId: Long): Flow<RoutineWithExercises?>
}

class RoutineRepositoryImpl(
    private val routineDao: RoutineDao,
    private val exerciseDao: ExerciseDao,
) : RoutineRepository {

    override fun getAllRoutines(): Flow<List<RoutineEntity>> = routineDao.getAll()

    override suspend fun createRoutine(name: String, description: String): Long =
        routineDao.insert(RoutineEntity(name = name, description = description))

    override suspend fun deleteRoutine(routine: RoutineEntity) = routineDao.delete(routine)

    override suspend fun addExerciseToRoutine(routineId: Long, exercise: ExerciseEntity) {
        exerciseDao.upsert(exercise)
        exerciseDao.insertCrossRef(RoutineExerciseCrossRef(routineId = routineId, exerciseId = exercise.exerciseId))
    }

    override suspend fun removeExerciseFromRoutine(routineId: Long, exerciseId: Int) {
        exerciseDao.deleteCrossRef(RoutineExerciseCrossRef(routineId = routineId, exerciseId = exerciseId))
    }

    override fun getRoutineWithExercises(routineId: Long): Flow<RoutineWithExercises?> =
        routineDao.getRoutineWithExercises(routineId)
}
