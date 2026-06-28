package com.example.uicompose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = primary_blue,
    background = background_dark,
    surface = background_dark,
    onPrimary = white,
    onSecondary = black,
    onBackground = white,
    onSurface = white,
    secondary = rating_orange,
)
@Composable
fun AppTheme (content: @Composable () -> Unit){
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
