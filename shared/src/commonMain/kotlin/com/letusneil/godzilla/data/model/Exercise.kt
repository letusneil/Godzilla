package com.letusneil.godzilla.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ExerciseResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Exercise>
)

@Serializable
data class Exercise(
    val id: Int,
    val uuid: String,
    val created: String,
    @SerialName("last_update")
    val lastUpdate: String,
    @SerialName("last_update_global")
    val lastUpdateGlobal: String,
    val category: Category,
    val muscles: List<Muscle>,
    @SerialName("muscles_secondary")
    val musclesSecondary: List<Muscle>,
    val equipment: List<Equipment>,
    val license: License,
    @SerialName("license_author")
    val licenseAuthor: String?,
    val images: List<ExerciseImage>,
    val translations: List<Translation>,
    val variations: Int?,
    // Using JsonElement as the array was always empty in the sample.
    // Update to a specific data class if you know the video object structure.
    val videos: List<JsonElement>,
    @SerialName("author_history")
    val authorHistory: List<String>,
    @SerialName("total_authors_history")
    val totalAuthorsHistory: List<String>
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

@Serializable
data class Muscle(
    val id: Int,
    val name: String,
    @SerialName("name_en")
    val nameEn: String,
    @SerialName("is_front")
    val isFront: Boolean,
    @SerialName("image_url_main")
    val imageUrlMain: String,
    @SerialName("image_url_secondary")
    val imageUrlSecondary: String
)

@Serializable
data class Equipment(
    val id: Int,
    val name: String
)

@Serializable
data class License(
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("short_name")
    val shortName: String,
    val url: String
)

@Serializable
data class ExerciseImage(
    val id: Int,
    val uuid: String,
    val exercise: Int,
    @SerialName("exercise_uuid")
    val exerciseUuid: String,
    val image: String,
    @SerialName("is_main")
    val isMain: Boolean,
    val style: String,
    val license: Int,
    @SerialName("license_title")
    val licenseTitle: String,
    @SerialName("license_object_url")
    val licenseObjectUrl: String,
    @SerialName("license_author")
    val licenseAuthor: String?,
    @SerialName("license_author_url")
    val licenseAuthorUrl: String,
    @SerialName("license_derivative_source_url")
    val licenseDerivativeSourceUrl: String,
    @SerialName("author_history")
    val authorHistory: List<String>
)

@Serializable
data class Translation(
    val id: Int,
    val uuid: String,
    val name: String,
    val exercise: Int,
    val description: String,
    val created: String,
    val language: Int,
    val aliases: List<Alias>,
    val notes: List<Note>,
    val license: Int,
    @SerialName("license_title")
    val licenseTitle: String,
    @SerialName("license_object_url")
    val licenseObjectUrl: String,
    @SerialName("license_author")
    val licenseAuthor: String?,
    @SerialName("license_author_url")
    val licenseAuthorUrl: String,
    @SerialName("license_derivative_source_url")
    val licenseDerivativeSourceUrl: String,
    @SerialName("author_history")
    val authorHistory: List<String>
)

@Serializable
data class Alias(
    val id: Int,
    val uuid: String,
    val alias: String
)

@Serializable
data class Note(
    val id: Int,
    val uuid: String,
    val translation: Int,
    val comment: String
)