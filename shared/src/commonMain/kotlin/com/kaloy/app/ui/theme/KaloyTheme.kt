package com.kaloy.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============================================================
// Palette de couleurs Kaloy – inspirée musique/vibes sombres
// ============================================================

// Couleurs principales
val KaloyPurple = Color(0xFF7C3AED)
val KaloyPurpleLight = Color(0xFFA78BFA)
val KaloyPurpleDark = Color(0xFF5B21B6)

val KaloyPink = Color(0xFFEC4899)
val KaloyPinkLight = Color(0xFFF472B6)

val KaloyCyan = Color(0xFF06B6D4)
val KaloyCyanLight = Color(0xFF22D3EE)

// Surfaces sombres
val KaloyDarkBg = Color(0xFF0F0F1A)
val KaloyDarkSurface = Color(0xFF1A1A2E)
val KaloyDarkCard = Color(0xFF252542)
val KaloyDarkElevated = Color(0xFF2D2D4A)

// Textes
val KaloyTextPrimary = Color(0xFFF1F1F6)
val KaloyTextSecondary = Color(0xFFA0A0B8)
val KaloyTextMuted = Color(0xFF6B6B82)

// Accent
val KaloyGreen = Color(0xFF10B981)
val KaloyRed = Color(0xFFEF4444)
val KaloyOrange = Color(0xFFF59E0B)

// ============================================================
// Thèmes Material 3
// ============================================================

private val DarkColorScheme = darkColorScheme(
    primary = KaloyPurple,
    onPrimary = Color.White,
    primaryContainer = KaloyPurpleDark,
    onPrimaryContainer = KaloyPurpleLight,
    secondary = KaloyPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A1942),
    onSecondaryContainer = KaloyPinkLight,
    tertiary = KaloyCyan,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF0A3D4A),
    onTertiaryContainer = KaloyCyanLight,
    background = KaloyDarkBg,
    onBackground = KaloyTextPrimary,
    surface = KaloyDarkSurface,
    onSurface = KaloyTextPrimary,
    surfaceVariant = KaloyDarkCard,
    onSurfaceVariant = KaloyTextSecondary,
    outline = KaloyTextMuted,
    error = KaloyRed,
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = KaloyPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = KaloyPurpleDark,
    secondary = KaloyPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFCE7F3),
    onSecondaryContainer = Color(0xFF831843),
    tertiary = KaloyCyan,
    onTertiary = Color.White,
    background = Color(0xFFF8F7FF),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFF3F0FF),
    onSurfaceVariant = Color(0xFF4A4A6A),
    outline = Color(0xFFB0B0C8),
    error = KaloyRed,
    onError = Color.White,
)

@Composable
fun KaloyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
