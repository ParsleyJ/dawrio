package com.parsleyj.dawrio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val md_theme_light_primary = Color(0xFF156D21)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFA0F799)
val md_theme_light_onPrimaryContainer = Color(0xFF002204)
val md_theme_light_secondary = Color(0xFF7A5900)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDEA1)
val md_theme_light_onSecondaryContainer = Color(0xFF261900)
val md_theme_light_tertiary = Color(0xFF135CB9)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFD7E2FF)
val md_theme_light_onTertiaryContainer = Color(0xFF001A40)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFCFDF6)
val md_theme_light_onBackground = Color(0xFF1A1C19)
val md_theme_light_surface = Color(0xFFFCFDF6)
val md_theme_light_onSurface = Color(0xFF1A1C19)
val md_theme_light_surfaceVariant = Color(0xFFDEE5D8)
val md_theme_light_onSurfaceVariant = Color(0xFF424940)
val md_theme_light_outline = Color(0xFF72796F)
val md_theme_light_inverseOnSurface = Color(0xFFF0F1EB)
val md_theme_light_inverseSurface = Color(0xFF2F312D)
val md_theme_light_inversePrimary = Color(0xFF84DA80)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF156D21)
val md_theme_light_outlineVariant = Color(0xFFC2C9BD)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF84DA80)
val md_theme_dark_onPrimary = Color(0xFF00390A)
val md_theme_dark_primaryContainer = Color(0xFF005312)
val md_theme_dark_onPrimaryContainer = Color(0xFFA0F799)
val md_theme_dark_secondary = Color(0xFFF4BE48)
val md_theme_dark_onSecondary = Color(0xFF402D00)
val md_theme_dark_secondaryContainer = Color(0xFF5C4300)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDEA1)
val md_theme_dark_tertiary = Color(0xFFACC7FF)
val md_theme_dark_onTertiary = Color(0xFF002F67)
val md_theme_dark_tertiaryContainer = Color(0xFF004491)
val md_theme_dark_onTertiaryContainer = Color(0xFFD7E2FF)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1A1C19)
val md_theme_dark_onBackground = Color(0xFFE2E3DD)
val md_theme_dark_surface = Color(0xFF1A1C19)
val md_theme_dark_onSurface = Color(0xFFE2E3DD)
val md_theme_dark_surfaceVariant = Color(0xFF424940)
val md_theme_dark_onSurfaceVariant = Color(0xFFC2C9BD)
val md_theme_dark_outline = Color(0xFF8C9388)
val md_theme_dark_inverseOnSurface = Color(0xFF1A1C19)
val md_theme_dark_inverseSurface = Color(0xFFE2E3DD)
val md_theme_dark_inversePrimary = Color(0xFF156D21)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF84DA80)
val md_theme_dark_outlineVariant = Color(0xFF424940)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFF50A350)



private val dawrioLightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val dawrioDarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


@Composable
fun DawrioTheme(
    darkTheme:Boolean = isSystemInDarkTheme(),
    dynamicColor:Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> dawrioDarkColors
        else -> dawrioLightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}

