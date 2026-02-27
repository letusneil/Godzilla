package com.letusneil.godzilla.data.remote

import com.letusneil.godzilla.data.model.ExerciseResponse
import com.letusneil.godzilla.data.model.ExerciseSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WgerApiClient(private val httpClient: HttpClient) {

    suspend fun fetchExercises(
        language: Int = LANGUAGE_ENGLISH,
        limit: Int = PAGE_SIZE,
        offset: Int = 0,
    ): ExerciseResponse = httpClient
        .get("$BASE_URL/api/v2/exerciseinfo/") {
            parameter("format", "json")
            parameter("language", language)
            parameter("limit", limit)
            parameter("offset", offset)
        }
        .body()

    suspend fun searchExercises(term: String): ExerciseSearchResponse = httpClient
        .get("$BASE_URL/api/v2/exercise/search/") {
            parameter("term", term)
            parameter("language", "english")
            parameter("format", "json")
        }
        .body()

    companion object {
        const val BASE_URL = "https://wger.de"
        const val LANGUAGE_ENGLISH = 2
        const val PAGE_SIZE = 20
    }
}
