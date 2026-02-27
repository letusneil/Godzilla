package com.letusneil.godzilla.data.repository

import com.letusneil.godzilla.data.model.ExercisePage
import com.letusneil.godzilla.data.remote.WgerApiClient

interface ExerciseRepository {
    suspend fun getExercises(page: Int = 0): ExercisePage
}

class ExerciseRepositoryImpl(
    private val apiClient: WgerApiClient,
) : ExerciseRepository {

    override suspend fun getExercises(page: Int): ExercisePage {
        val response = apiClient.fetchExercises(offset = page * WgerApiClient.PAGE_SIZE)
        return ExercisePage(
            exercises = response.results,
            hasMore = response.next != null,
        )
    }
}
