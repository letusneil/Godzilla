package com.letusneil.godzilla.di

import com.letusneil.godzilla.exercises.ExercisesViewModel
import com.letusneil.godzilla.routines.RoutinesViewModel
import com.letusneil.godzilla.search.SearchViewModel
import com.letusneil.godzilla.theme.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ExercisesViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { RoutinesViewModel(get()) }
}
