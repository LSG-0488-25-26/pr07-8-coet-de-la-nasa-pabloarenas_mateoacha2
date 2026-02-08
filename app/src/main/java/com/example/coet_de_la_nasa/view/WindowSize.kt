package com.example.coet_de_la_nasa.view

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tamaños de pantalla para diseño responsive (recomendación PR05).
 * Compact: móvil vertical.
 * Medium: móvil horizontal / tablet pequeño.
 * Extended: tablet grande / escritorio.
 */
enum class WindowSize {
    Compact,
    Medium,
    Extended
}

/** Breakpoints: < 600dp Compact, 600-839dp Medium, >= 840dp Extended. */
fun windowSizeFromWidth(width: Dp): WindowSize = when {
    width < 600.dp -> WindowSize.Compact
    width < 840.dp -> WindowSize.Medium
    else -> WindowSize.Extended
}
