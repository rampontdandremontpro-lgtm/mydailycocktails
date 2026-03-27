package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingStars(
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        for (index in 1..5) {
            val selected = index <= rating
            val scale = remember(index) { Animatable(1f) }

            LaunchedEffect(rating) {
                if (index == rating && rating > 0) {
                    scale.snapTo(0.75f)
                    scale.animateTo(
                        targetValue = 1.28f,
                        animationSpec = tween(
                            durationMillis = 180,
                            easing = FastOutSlowInEasing
                        )
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = 0.45f,
                            stiffness = 420f
                        )
                    )
                } else {
                    scale.snapTo(1f)
                }
            }

            Icon(
                imageVector = if (selected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Étoile $index",
                tint = if (selected) Color(0xFFF4C430) else Color(0xFFD1D5DB),
                modifier = Modifier
                    .scale(scale.value)
                    .clickable {
                        onRatingSelected(index)
                    }
            )
        }
    }
}

@Composable
fun ReadOnlyRatingStars(
    rating: Int
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (index in 1..5) {
            val selected = index <= rating

            Icon(
                imageVector = if (selected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (selected) Color(0xFFF4C430) else Color(0xFFD1D5DB)
            )
        }
    }
}