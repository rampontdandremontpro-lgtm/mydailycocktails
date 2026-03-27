package com.supdevinci.mydailycocktails.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BrandRed,
    secondary = BrandLime,
    background = AppBgMid,
    surface = AppCardLight,
    onPrimary = Color.White,
    onSecondary = TextPrimaryLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

private val DarkColors = darkColorScheme(
    primary = BrandCoral,
    secondary = BrandLime,
    background = DarkBg,
    surface = AppCardDark,
    onPrimary = Color.White,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

@Composable
fun MyDailyCocktailsTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}