package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supdevinci.mydailycocktails.ui.theme.AppGlassDark
import com.supdevinci.mydailycocktails.ui.theme.AppGlassLight
import com.supdevinci.mydailycocktails.ui.theme.BrandHeading
import com.supdevinci.mydailycocktails.ui.theme.BrandHeadingDark
import com.supdevinci.mydailycocktails.ui.theme.BrandLimeDeep
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderDark
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderLight
import com.supdevinci.mydailycocktails.ui.theme.IconMutedDark
import com.supdevinci.mydailycocktails.ui.theme.IconMutedLight
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryDark
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryLight
import com.supdevinci.mydailycocktails.ui.theme.TextSecondaryDark
import com.supdevinci.mydailycocktails.ui.theme.TextSecondaryLight

@Composable
private fun ThemeToggle(
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        label = "theme_toggle_scale"
    )

    Box(
        modifier = Modifier
            .size(42.dp)
            .scale(scale)
            .background(
                color = if (darkMode) AppGlassDark else AppGlassLight,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = if (darkMode) GlassBorderDark else GlassBorderLight,
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onToggleTheme() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (darkMode) Icons.Outlined.WbSunny else Icons.Outlined.Bedtime,
            contentDescription = "Changer de thème",
            tint = if (darkMode) IconMutedDark else IconMutedLight,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun HomeHeader(
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp)
        ) {
            ThemeToggle(
                darkMode = darkMode,
                onToggleTheme = onToggleTheme
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Cocktails\uD83C\uDF79",
                    color = if (darkMode) BrandHeadingDark else BrandHeading,
                    fontSize = 33.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Découvre des recettes aléatoires chaque jour",
                color = if (darkMode) TextSecondaryDark else TextSecondaryLight,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PageHeader(
    title: String,
    subtitle: String,
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            ThemeToggle(
                darkMode = darkMode,
                onToggleTheme = onToggleTheme
            )
        }

        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(
                text = title,
                color = if (darkMode) TextPrimaryDark else TextPrimaryLight,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = if (darkMode) TextSecondaryDark else TextSecondaryLight,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun Header(
    title: String,
    subtitle: String,
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    PageHeader(
        title = title,
        subtitle = subtitle,
        darkMode = darkMode,
        onToggleTheme = onToggleTheme
    )
}