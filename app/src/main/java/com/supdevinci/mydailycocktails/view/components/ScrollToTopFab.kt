package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.supdevinci.mydailycocktails.ui.theme.AppGlassStrongDark
import com.supdevinci.mydailycocktails.ui.theme.AppGlassStrongLight
import com.supdevinci.mydailycocktails.ui.theme.BrandCoral
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderDark

@Composable
fun ScrollToTopFab(
    visible: Boolean,
    darkMode: Boolean,
    bottomOffset: Dp,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-18).dp, y = -bottomOffset),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Surface(
                onClick = onClick,
                modifier = Modifier
                    .size(52.dp)
                    .border(
                        width = 1.dp,
                        color = if (darkMode) GlassBorderDark else Color.White.copy(alpha = 0.88f),
                        shape = CircleShape
                    ),
                shape = CircleShape,
                color = if (darkMode) AppGlassStrongDark else AppGlassStrongLight,
                tonalElevation = 0.dp,
                shadowElevation = if (darkMode) 8.dp else 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Remonter en haut",
                        tint = if (darkMode) BrandCoral else BrandRed,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}