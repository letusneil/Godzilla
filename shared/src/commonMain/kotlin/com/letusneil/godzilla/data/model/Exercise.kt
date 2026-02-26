package com.letusneil.godzilla.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ExerciseInfo>,
)

@Serializable
data class ExerciseInfo(
    val id: Int,
    val uuid: String,
    val category: ExerciseCategory,
    val muscles: List<Muscle>,
    @SerialName("muscles_secondary") val musclesSecondary: List<Muscle>,
    val equipment: List<Equipment>,
    val images: List<ExerciseImage>,
    val translations: List<ExerciseTranslation>,
)

@Serializable
data class ExerciseCategory(
    val id: Int,
    val name: String,
)

@Serializable
data class Muscle(
    val id: Int,
    val name: String,
    @SerialName("name_en") val nameEn: String,
    @SerialName("is_front") val isFront: Boolean,
)

@Serializable
data class Equipment(
    val id: Int,
    val name: String,
)

@Serializable
data class ExerciseImage(
    val uuid: String,
    @SerialName("exercise_base") val exerciseBase: Int,
    val image: String,
    @SerialName("is_main") val isMain: Boolean,
)

@Serializable
data class ExerciseTranslation(
    val id: Int,
    val uuid: String,
    val name: String,
    val description: String,
    val language: Int,
)