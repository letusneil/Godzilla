package com.letusneil.godzilla.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RoutineWithExercises(
    @Embedded val routine: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineExerciseCrossRef::class),
    )
    val exercises: List<ExerciseEntity>,
)
