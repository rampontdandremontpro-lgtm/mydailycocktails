package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.supdevinci.mydailycocktails.navigation.Routes
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.DarkBg2
import com.supdevinci.mydailycocktails.ui.theme.IconMutedDark
import com.supdevinci.mydailycocktails.ui.theme.IconMutedLight
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryDark
import com.supdevinci.mydailycocktails.ui.theme.TextPrimaryLight

@Composable
fun BottomBar(
    currentRoute: String?,
    darkMode: Boolean,
    onNavigate: (String) -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val containerColor = if (darkMode) DarkBg2 else Color.White
        val selectedBackground = BrandRed.copy(alpha = if (darkMode) 0.18f else 0.12f)
        val selectedColor = BrandRed
        val unselectedIconColor = if (darkMode) IconMutedDark else IconMutedLight
        val unselectedTextColor = if (darkMode) TextPrimaryDark else TextPrimaryLight

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = containerColor,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    label = "Accueil",
                    selected = currentRoute == Routes.RANDOM,
                    selectedColor = selectedColor,
                    selectedBackground = selectedBackground,
                    unselectedIconColor = unselectedIconColor,
                    unselectedTextColor = unselectedTextColor,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Accueil",
                            tint = if (currentRoute == Routes.RANDOM) selectedColor else unselectedIconColor
                        )
                    },
                    onClick = { onNavigate(Routes.RANDOM) }
                )

                BottomBarItem(
                    label = "Rechercher",
                    selected = currentRoute == Routes.SEARCH,
                    selectedColor = selectedColor,
                    selectedBackground = selectedBackground,
                    unselectedIconColor = unselectedIconColor,
                    unselectedTextColor = unselectedTextColor,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Rechercher",
                            tint = if (currentRoute == Routes.SEARCH) selectedColor else unselectedIconColor
                        )
                    },
                    onClick = { onNavigate(Routes.SEARCH) }
                )

                BottomBarItem(
                    label = "Favoris",
                    selected = currentRoute == Routes.FAVORITES,
                    selectedColor = selectedColor,
                    selectedBackground = selectedBackground,
                    unselectedIconColor = unselectedIconColor,
                    unselectedTextColor = unselectedTextColor,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favoris",
                            tint = if (currentRoute == Routes.FAVORITES) selectedColor else unselectedIconColor
                        )
                    },
                    onClick = { onNavigate(Routes.FAVORITES) }
                )

                BottomBarItem(
                    label = "Historique",
                    selected = currentRoute == Routes.HISTORY,
                    selectedColor = selectedColor,
                    selectedBackground = selectedBackground,
                    unselectedIconColor = unselectedIconColor,
                    unselectedTextColor = unselectedTextColor,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "Historique",
                            tint = if (currentRoute == Routes.HISTORY) selectedColor else unselectedIconColor
                        )
                    },
                    onClick = { onNavigate(Routes.HISTORY) }
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    selected: Boolean,
    selectedColor: Color,
    selectedBackground: Color,
    unselectedIconColor: Color,
    unselectedTextColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = if (selected) selectedBackground else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        icon()
        Text(
            text = label,
            color = if (selected) selectedColor else unselectedTextColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}