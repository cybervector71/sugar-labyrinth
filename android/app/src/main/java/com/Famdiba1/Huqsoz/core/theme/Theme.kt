package com.Famdiba1.Huqsoz.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CandySweetScheme = lightColorScheme(
    primary = Raspberry,
    onPrimary = VanillaCream,
    primaryContainer = RaspberryLight,
    onPrimaryContainer = RaspberryDeep,
    secondary = Pistachio,
    onSecondary = VanillaCream,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = Copper,
    onTertiary = VanillaCream,
    tertiaryContainer = CopperLight,
    onTertiaryContainer = DarkScrim,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfaceSoft,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = Copper,
    outlineVariant = DisabledFill,
    error = Raspberry,
    onError = VanillaCream,
    scrim = DarkScrim
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CandySweetScheme,
        typography = AppTypography,
        content = content
    )
}
