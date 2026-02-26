package com.letusneil.godzilla

import android.app.Application
import com.letusneil.godzilla.di.sharedModules
import com.letusneil.godzilla.di.themeModule
import com.letusneil.godzilla.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GodzillaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GodzillaApplication)
            modules(sharedModules + viewModelModule + themeModule)
        }
    }
}
