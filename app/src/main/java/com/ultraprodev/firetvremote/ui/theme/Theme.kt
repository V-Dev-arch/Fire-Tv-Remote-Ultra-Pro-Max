package com.ultraprodev.firetvremote.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// A media-remote app lives in dark rooms next to a TV — dark-first palette,
// not a generic Material default. Expanded from a single flat accent to a
// real multi-color system: teal stays the primary "connected/active" tone,
// with ember and violet as secondary accents for variety across buttons,
// app tiles, and status states, so the UI doesn't read as one flat color
// repeated everywhere.
// --- Fixed brand colours: safe in both themes because they're only used as
// FILLS (gradients, icon backgrounds) with white/dark text on top. ---
val FireAccentFill = Color(0xFF22E7BE)   // bright teal fill
val FireAccentDim = Color(0xFF12A98C)
val Ember = Color(0xFFFF8A4C)             // secondary accent - warm, "Fire" wordplay
val Violet = Color(0xFF8B7CF6)            // tertiary accent - used sparingly for variety
val WarnAmber = Color(0xFFFFB020)
val DangerRed = Color(0xFFE5484D)

/**
 * Theme-dependent colours. These used to be fixed dark-mode values, which
 * is why text vanished in light mode: near-white TextPrimary and bright
 * teal FireAccent drawn on a near-white background. Every screen reads them
 * through the getters below, so they now follow the active theme.
 */
@Immutable
data class FirePalette(
    val isDark: Boolean,
    val surfaceDeep: Color,
    val surfaceRaised: Color,
    val surfaceRaisedHigh: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    /** Accent usable for TEXT and ICONS (readable on the background). */
    val accent: Color
)

val DarkPalette = FirePalette(
    isDark = true,
    surfaceDeep = Color(0xFF0A0D11),
    surfaceRaised = Color(0xFF161B21),
    surfaceRaisedHigh = Color(0xFF212832),
    textPrimary = Color(0xFFF2F5F5),
    textSecondary = Color(0xFF97A3AD),
    accent = FireAccentFill
)

val LightPalette = FirePalette(
    isDark = false,
    surfaceDeep = Color(0xFFF4F6F8),
    surfaceRaised = Color(0xFFFFFFFF),
    surfaceRaisedHigh = Color(0xFFE6EAEE),
    textPrimary = Color(0xFF12171C),
    textSecondary = Color(0xFF55606B),
    accent = Color(0xFF00806A) // darker teal: ~5:1 contrast on white
)

val LocalFirePalette = staticCompositionLocalOf { DarkPalette }

val SurfaceDeep: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.surfaceDeep
val SurfaceRaised: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.surfaceRaised
val SurfaceRaisedHigh: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.surfaceRaisedHigh
val TextPrimary: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.textPrimary
val TextSecondary: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.textSecondary
/** Accent for text/icons; use [FireAccentFill] for backgrounds and gradients. */
val FireAccent: Color @Composable @ReadOnlyComposable get() = LocalFirePalette.current.accent
val IsDarkTheme: Boolean @Composable @ReadOnlyComposable get() = LocalFirePalette.current.isDark

/** Reusable gradient brushes for buttons/highlights that want real depth instead of a flat fill. */
val SignalGradient = Brush.linearGradient(listOf(FireAccentFill, Color(0xFF4DA8E8)))
val EmberGradient = Brush.linearGradient(listOf(Ember, Color(0xFFFF6B6B)))
val VioletGradient = Brush.linearGradient(listOf(Violet, Color(0xFFB57CF6)))

/** A rotating set of distinct tile gradients — used so a grid of items (e.g. app tiles) doesn't read as identical repeated boxes. */
val TileGradients = listOf(
    Brush.linearGradient(listOf(Color(0xFFE5484D), Color(0xFFFF8A4C))), // red -> ember (Netflix-adjacent warmth)
    Brush.linearGradient(listOf(Color(0xFF2D6CDF), Color(0xFF4DA8E8))), // blue (Prime/Disney-adjacent)
    Brush.linearGradient(listOf(FireAccentFill, Color(0xFF12A98C))),        // teal
    Brush.linearGradient(listOf(Violet, Color(0xFFB57CF6))),            // violet
    Brush.linearGradient(listOf(Color(0xFFFFB020), Ember)),             // amber -> ember
    Brush.linearGradient(listOf(Color(0xFF2DBF91), FireAccentFill)),        // green -> teal
)

private val DarkColors = darkColorScheme(
    primary = FireAccentFill,
    onPrimary = Color(0xFF00201C),
    secondary = Ember,
    onSecondary = Color(0xFF2A1100),
    tertiary = Violet,
    background = DarkPalette.surfaceDeep,
    onBackground = DarkPalette.textPrimary,
    surface = DarkPalette.surfaceRaised,
    onSurface = DarkPalette.textPrimary,
    surfaceVariant = DarkPalette.surfaceRaisedHigh,
    onSurfaceVariant = DarkPalette.textSecondary,
    surfaceContainer = DarkPalette.surfaceRaised,
    surfaceContainerHigh = DarkPalette.surfaceRaisedHigh,
    error = DangerRed
)

// Light scheme now defines every role the app uses, instead of only four
// -- the missing ones fell back to Material defaults that didn't match the
// hand-picked palette above.
private val LightColors = lightColorScheme(
    primary = LightPalette.accent,
    onPrimary = Color.White,
    secondary = Color(0xFFD9602A),
    onSecondary = Color.White,
    tertiary = Color(0xFF5B4BD6),
    background = LightPalette.surfaceDeep,
    onBackground = LightPalette.textPrimary,
    surface = LightPalette.surfaceRaised,
    onSurface = LightPalette.textPrimary,
    surfaceVariant = LightPalette.surfaceRaisedHigh,
    onSurfaceVariant = LightPalette.textSecondary,
    surfaceContainer = LightPalette.surfaceRaised,
    surfaceContainerHigh = Color(0xFFF0F2F5),
    error = Color(0xFFC62828)
)

@Composable
fun FireTvRemoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) DarkPalette else LightPalette

    // Status/navigation bar icons must flip too, or they vanish (white icons
    // on a light background) -- especially with Android 15's forced edge-to-edge.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            @Suppress("DEPRECATION")
            window.statusBarColor = palette.surfaceDeep.toArgb()
            @Suppress("DEPRECATION")
            window.navigationBarColor = palette.surfaceDeep.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalFirePalette provides palette) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = FireTvTypography,
            content = content
        )
    }
}
