package com.letusneil.godzilla.data.repository

import com.letusneil.godzilla.data.model.Exercise
import com.letusneil.godzilla.data.remote.WgerApiClient

interface ExerciseRepository {
    suspend fun getExercises(page: Int = 0): List<Exercise>
}

class ExerciseRepositoryImpl(
    private val apiClient: WgerApiClient,
) : ExerciseRepository {

    override suspend fun getExercises(page: Int): List<Exercise> =
        apiClient.fetchExercises(offset = page * WgerApiClient.PAGE_SIZE).results
}
