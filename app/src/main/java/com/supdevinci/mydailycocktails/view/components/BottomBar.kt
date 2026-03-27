package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supdevinci.mydailycocktails.navigation.Routes
import com.supdevinci.mydailycocktails.ui.theme.AppGlassDark
import com.supdevinci.mydailycocktails.ui.theme.AppGlassStrongLight
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderDark
import com.supdevinci.mydailycocktails.ui.theme.GlassBorderLight
import com.supdevinci.mydailycocktails.ui.theme.IconMutedDark
import com.supdevinci.mydailycocktails.ui.theme.IconMutedLight
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryDark
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryLight

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    visible: Boolean = true
) {
    if (!visible) return

    val items = listOf(
        Triple(Routes.RANDOM, Icons.Outlined.Home, "Accueil"),
        Triple(Routes.SEARCH, Icons.Outlined.Search, "Rechercher"),
        Triple(Routes.FAVORITES, Icons.Outlined.FavoriteBorder, "Favoris"),
        Triple(Routes.HISTORY, Icons.Outlined.History, "Historique")
    )

    Surface(
        color = AppGlassStrongLight,
        shadowElevation = 16.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (route, icon, label) ->
                val selected = currentRoute == route
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.95f else 1f,
                    label = "bottom_bar_item_scale_$route"
                )

                Column(
                    modifier = Modifier
                        .scale(scale)
                        .animateContentSize()
                        .background(
                            if (selected) BrandRed.copy(alpha = 0.12f) else Color.Transparent,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onNavigate(route) }
                        .padding(horizontal = 18.dp, vertical = 9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selected) BrandRed else IconMutedLight
                    )

                    Text(
                        text = label,
                        color = if (selected) BrandRed else TextPrimaryLight,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}