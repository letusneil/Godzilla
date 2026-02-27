package com.letusneil.godzilla.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val exerciseId: Int,
    val name: String,
    val category: String,
)
