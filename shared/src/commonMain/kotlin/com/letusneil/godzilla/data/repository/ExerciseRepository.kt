package com.letusneil.godzilla.data.repository

import com.letusneil.godzilla.data.model.ExerciseInfo
import com.letusneil.godzilla.data.remote.WgerApiClient

interface ExerciseRepository {
    suspend fun getExercises(page: Int = 0): List<ExerciseInfo>
}

class ExerciseRepositoryImpl(
    private val apiClient: WgerApiClient = WgerApiClient(),
) : ExerciseRepository {

    override suspend fun getExercises(page: Int): List<ExerciseInfo> =
        apiClient.fetchExercises(offset = page * WgerApiClient.PAGE_SIZE).results
}
