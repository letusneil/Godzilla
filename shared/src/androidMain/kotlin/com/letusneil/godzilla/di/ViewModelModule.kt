package com.letusneil.godzilla.di

import com.letusneil.godzilla.exercises.ExercisesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ExercisesViewModel(get()) }
}
