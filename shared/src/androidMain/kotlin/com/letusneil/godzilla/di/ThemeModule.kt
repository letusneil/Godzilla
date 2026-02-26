package com.letusneil.godzilla.di

import com.letusneil.godzilla.theme.ThemeRepository
import com.letusneil.godzilla.theme.ThemeRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val themeModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl(androidContext()) }
}
