package com.leonardower.mymovie.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    background = DarkBg,
    onBackground = Color.White,
    primary = OrangePrimary,
    onPrimary = Color.White,
    surface = GrayButtonColor,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyMovieTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    SetupSystemUI()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SetupSystemUI() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )

        systemUiController.setNavigationBarColor(
            color = DarkBg,
            darkIcons = false,
            navigationBarContrastEnforced = false
        )
    }
}