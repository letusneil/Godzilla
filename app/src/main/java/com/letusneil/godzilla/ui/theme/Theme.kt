package com.letusneil.godzilla.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Dark_Primary,
    onPrimary = Dark_OnPrimary,
    secondary = Dark_Secondary,
    onSecondary = Dark_OnSecondary,
    background = Dark_Background,
    onBackground = Dark_OnBackground,
    surface = Dark_Surface,
    onSurface = Dark_OnSurface,
    surfaceVariant = Dark_SurfaceVariant,
    onSurfaceVariant = Dark_OnSurfaceVariant,
    outline = Dark_Outline,
)

private val LightColorScheme = lightColorScheme(
    primary = Light_Primary,
    onPrimary = Light_OnPrimary,
    secondary = Light_Secondary,
    onSecondary = Light_OnSecondary,
    background = Light_Background,
    onBackground = Light_OnBackground,
    surface = Light_Surface,
    onSurface = Light_OnSurface,
    surfaceVariant = Light_SurfaceVariant,
    onSurfaceVariant = Light_OnSurfaceVariant,
    outline = Light_Outline,
)

@Composable
fun GodzillaTheme(
    themeConfig: ThemeConfig = ThemeConfig(),
    content: @Composable () -> Unit,
) {
    val isDark = when (themeConfig.themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        // Dynamic colour is guarded by the flag – ready to enable in the future.
        themeConfig.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
