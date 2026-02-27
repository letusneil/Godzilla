package com.letusneil.godzilla.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.data.local.entity.RoutineWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert
    suspend fun insert(routine: RoutineEntity): Long

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("SELECT * FROM routines ORDER BY createdAt DESC")
    fun getAll(): Flow<List<RoutineEntity>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun getRoutineWithExercises(routineId: Long): Flow<RoutineWithExercises?>
}
