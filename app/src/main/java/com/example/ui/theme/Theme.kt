package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NexaColorScheme = darkColorScheme(
    primary = NexaCyan,
    onPrimary = NexaBgDark,
    primaryContainer = NexaCyanDark,
    onPrimaryContainer = NexaTextPrimary,
    secondary = NexaMagenta,
    onSecondary = NexaTextPrimary,
    secondaryContainer = NexaPurple,
    onSecondaryContainer = NexaTextPrimary,
    tertiary = NexaGold,
    onTertiary = NexaBgDark,
    background = NexaBgDark,
    onBackground = NexaTextPrimary,
    surface = NexaSurfaceDark,
    onSurface = NexaTextPrimary,
    surfaceVariant = NexaCardDark,
    onSurfaceVariant = NexaTextSecondary,
    outline = NexaBorderDark,
    error = TeamRedColor,
    onError = NexaTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always enforce the rich cyber theme for Nexa World
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NexaColorScheme,
        typography = Typography,
        content = content
    )
}
