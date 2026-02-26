package com.letusneil.godzilla.ui.theme

import androidx.compose.runtime.compositionLocalOf
import com.letusneil.godzilla.theme.ThemeMode

data class ThemeConfig(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    /** Reserved for future dynamic colour support (Material You / Android 12+). */
    val dynamicColor: Boolean = false,
)

/** Read the current [ThemeConfig] anywhere in the composition tree. */
val LocalThemeConfig = compositionLocalOf { ThemeConfig() }

/** Call this lambda to change [ThemeMode] from any composable. */
val LocalThemeController = compositionLocalOf<(ThemeMode) -> Unit> {
    error("LocalThemeController not provided")
}
