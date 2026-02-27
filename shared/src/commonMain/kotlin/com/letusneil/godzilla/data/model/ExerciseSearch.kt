package com.letusneil.godzilla.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSearchResponse(
    val suggestions: List<ExerciseSuggestion>,
)

@Serializable
data class ExerciseSuggestion(
    val value: String,
    val data: ExerciseSuggestionData,
)

@Serializable
data class ExerciseSuggestionData(
    val id: Int,
    @SerialName("base_id") val baseId: Int,
    val name: String,
    val category: String,
    val image: String?,
    @SerialName("image_thumbnail") val imageThumbnail: String?,
)
