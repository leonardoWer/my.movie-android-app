package com.leonardower.mymovie.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = DarkBg,
    onBackground = Color.White,
    primary = OrangePrimary,
    onPrimary = Color.White,
    surface = GrayButton,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyMovieTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}