package com.letusneil.godzilla.di

import com.letusneil.godzilla.data.repository.ExerciseRepository
import com.letusneil.godzilla.data.repository.ExerciseRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
}
