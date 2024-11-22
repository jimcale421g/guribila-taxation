package com.qarantech.guribila.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Light Theme Colors
val RedLight = Color(0xFFE57373)
val GreenLight = Color(0xFF81C784)
val RedDark = Color(0xFFB71C1C)
val GreenDark = Color(0xFF1B5E20)

// Gradient Brushes
val gradientBrush = Brush.verticalGradient(
    colors = listOf(
        RedLight,
        GreenLight
    )
)

val gradientBrushDark = Brush.verticalGradient(
    colors = listOf(
        RedDark,
        GreenDark
    )
)

// Other colors for the theme
val Background = Color(0xFFFAFAFA)
val BackgroundDark = Color(0xFF121212)
val Surface = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF242424)