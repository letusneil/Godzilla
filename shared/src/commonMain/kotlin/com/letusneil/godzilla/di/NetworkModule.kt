package com.letusneil.godzilla.di

import com.letusneil.godzilla.data.remote.WgerApiClient
import com.letusneil.godzilla.data.remote.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { WgerApiClient(get()) }
}
