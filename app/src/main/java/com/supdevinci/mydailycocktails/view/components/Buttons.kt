package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance
import com.supdevinci.mydailycocktails.ui.theme.AppGlassDark
import com.supdevinci.mydailycocktails.ui.theme.AppGlassLight
import com.supdevinci.mydailycocktails.ui.theme.ButtonGradientEnd
import com.supdevinci.mydailycocktails.ui.theme.ButtonGradientMid
import com.supdevinci.mydailycocktails.ui.theme.ButtonGradientStart
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderDark
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderLight
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryLight

@Composable
fun GradientPrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    val isDarkMode = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val gradient = Brush.horizontalGradient(
        colors = if (isDarkMode) {
            listOf(
                ButtonGradientEnd,
                ButtonGradientMid,
                ButtonGradientStart
            )
        } else {
            listOf(
                ButtonGradientStart,
                ButtonGradientMid,
                ButtonGradientEnd
            )
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PrimaryActionButton(
    text: String,
    darkMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "primary_action_scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = BrandRed,
        shadowElevation = 8.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SmallPillButton(
    text: String,
    darkMode: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "small_pill_scale"
    )

    Surface(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (darkMode) AppGlassDark else AppGlassLight,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (darkMode) GlassBorderDark else GlassBorderLight
        ),
        shadowElevation = 6.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            color = if (darkMode) Color.White else TextPrimaryLight,
            fontWeight = FontWeight.Medium
        )
    }
}