package com.letusneil.godzilla.data.remote

import com.letusneil.godzilla.data.model.ExerciseListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WgerApiClient(private val httpClient: HttpClient) {

    suspend fun fetchExercises(
        language: Int = LANGUAGE_ENGLISH,
        limit: Int = PAGE_SIZE,
        offset: Int = 0,
    ): ExerciseListResponse = httpClient
        .get("$BASE_URL/api/v2/exerciseinfo/") {
            parameter("format", "json")
            parameter("language", language)
            parameter("limit", limit)
            parameter("offset", offset)
        }
        .body()

    companion object {
        const val BASE_URL = "https://wger.de"
        const val LANGUAGE_ENGLISH = 2
        const val PAGE_SIZE = 20
    }
}
