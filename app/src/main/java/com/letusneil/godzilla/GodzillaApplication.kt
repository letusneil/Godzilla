package com.letusneil.godzilla

import android.app.Application
import com.letusneil.godzilla.di.databaseModule
import com.letusneil.godzilla.di.sharedModules
import com.letusneil.godzilla.di.themeModule
import com.letusneil.godzilla.di.viewModelModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GodzillaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        startKoin {
            androidContext(this@GodzillaApplication)
            modules(sharedModules + viewModelModule + themeModule + databaseModule)
        }
    }
}
