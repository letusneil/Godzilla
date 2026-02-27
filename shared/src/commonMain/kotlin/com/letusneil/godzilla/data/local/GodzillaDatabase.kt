package com.letusneil.godzilla.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.letusneil.godzilla.data.local.dao.ExerciseDao
import com.letusneil.godzilla.data.local.dao.RoutineDao
import com.letusneil.godzilla.data.local.entity.ExerciseEntity
import com.letusneil.godzilla.data.local.entity.RoutineEntity
import com.letusneil.godzilla.data.local.entity.RoutineExerciseCrossRef

@Database(
    entities = [RoutineEntity::class, ExerciseEntity::class, RoutineExerciseCrossRef::class],
    version = 1,
)
abstract class GodzillaDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao
}
