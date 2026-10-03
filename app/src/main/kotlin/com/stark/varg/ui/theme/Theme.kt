package com.stark.varg.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val VargDarkColorScheme = darkColorScheme(
    primary = VargStarkRed,
    onPrimary = VargWhite,
    primaryContainer = VargStarkRedDark,
    onPrimaryContainer = VargStarkRedGlow,
    secondary = VargSecondary,
    onSecondary = VargPitchBlack,
    secondaryContainer = VargBronzeDark,
    onSecondaryContainer = VargSecondary,
    background = VargPitchBlack,
    onBackground = VargTextPrimary,
    surface = VargSurfaceCard,
    onSurface = VargTextPrimary,
    surfaceVariant = VargSurfaceCardElevated,
    onSurfaceVariant = VargTextSecondary,
    outline = VargBorder,
    error = VargStarkRed,
    onError = VargWhite,
)

@Composable
fun StarkTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = VargPitchBlack.toArgb()
            window.navigationBarColor = VargPitchBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = VargDarkColorScheme,
        typography = StarkTypography,
        content = content,
    )
}
