package com.letusneil.godzilla.di

import com.letusneil.godzilla.data.repository.ExerciseRepository
import com.letusneil.godzilla.data.repository.ExerciseRepositoryImpl
import com.letusneil.godzilla.data.repository.RoutineRepository
import com.letusneil.godzilla.data.repository.RoutineRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single<RoutineRepository> { RoutineRepositoryImpl(get(), get()) }
}
