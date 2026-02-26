package com.letusneil.godzilla.di

/**
 * Koin modules that are shared across all platforms.
 * Combine with platform-specific modules (e.g. [viewModelModule] on Android) when calling startKoin.
 */
val sharedModules = listOf(
    networkModule,
    repositoryModule,
)
