package com.letusneil.godzilla.di

import com.letusneil.godzilla.data.local.GodzillaDatabase
import com.letusneil.godzilla.data.local.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { getDatabaseBuilder(androidContext()).build() }
    single { get<GodzillaDatabase>().routineDao() }
    single { get<GodzillaDatabase>().exerciseDao() }
}
