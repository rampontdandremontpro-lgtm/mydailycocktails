package com.supdevinci.mydailycocktails.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.supdevinci.mydailycocktails.data.local.entities.CocktailEntity
import com.supdevinci.mydailycocktails.model.Cocktail
import com.supdevinci.mydailycocktails.model.HistoryItem
import com.supdevinci.mydailycocktails.ui.theme.Card as CardColor
import com.supdevinci.mydailycocktails.ui.theme.DarkCard as DarkCardColor
import com.supdevinci.mydailycocktails.ui.theme.DarkTextPrimary as DarkTextPrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.DarkTextSecondary as DarkTextSecondaryColor
import com.supdevinci.mydailycocktails.ui.theme.Primary as PrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.PrimarySoft as PrimarySoftColor
import com.supdevinci.mydailycocktails.ui.theme.Surface as SurfaceColor
import com.supdevinci.mydailycocktails.ui.theme.TextPrimary as TextPrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.TextSecondary as TextSecondaryColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun cardColor(darkMode: Boolean): Color =
    if (darkMode) DarkCardColor else CardColor

private fun titleColor(darkMode: Boolean): Color =
    if (darkMode) DarkTextPrimaryColor else TextPrimaryColor

private fun subColor(darkMode: Boolean): Color =
    if (darkMode) DarkTextSecondaryColor else TextSecondaryColor

@Composable
fun AppCard(
    darkMode: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(darkMode)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun LoadingCard(darkMode: Boolean) {
    AppCard(darkMode = darkMode) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = PrimaryColor)
            Text(
                text = "Chargement...",
                modifier = Modifier.padding(top = 12.dp),
                color = subColor(darkMode)
            )
        }
    }
}

@Composable
fun ErrorCard(
    message: String,
    darkMode: Boolean,
    onRetry: () -> Unit
) {
    AppCard(darkMode) {
        Text(message, color = PrimaryColor)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
        GradientPrimaryButton(
            text = "Réessayer",
            onClick = onRetry
        )
    }
}

@Composable
fun EmptyCard(
    title: String,
    subtitle: String,
    darkMode: Boolean
) {
    AppCard(darkMode) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = titleColor(darkMode)
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
        Text(subtitle, color = subColor(darkMode))
    }
}

@Composable
fun InfoChip(
    text: String,
    background: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = background,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TagChip(
    text: String,
    background: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = background,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FigmaHeroCocktailCard(
    cocktail: Cocktail,
    darkMode: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val isAlcoholic = cocktail.strAlcoholic.equals("Alcoholic", ignoreCase = true)
    val heartScale = remember { Animatable(1f) }

    LaunchedEffect(isFavorite) {
        heartScale.snapTo(0.9f)
        heartScale.animateTo(
            targetValue = 1.22f,
            animationSpec = tween(durationMillis = 170, easing = FastOutSlowInEasing)
        )
        heartScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 140, easing = FastOutSlowInEasing)
        )
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
        ) {
            AsyncImage(
                model = cocktail.strDrinkThumb,
                contentDescription = cocktail.strDrink,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color(0x33000000),
                                Color(0xAA000000)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = cocktail.strDrink,
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (darkMode) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xE6282B33)
                        ) {
                            Text(
                                text = cocktail.strCategory ?: "Cocktail",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = DarkTextPrimaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        TagChip(
                            text = cocktail.strCategory ?: "Cocktail",
                            background = Color.White,
                            textColor = TextPrimaryColor
                        )
                    }

                    TagChip(
                        text = if (isAlcoholic) "🍸 Alcoolisé" else "🧃 Sans alcool",
                        background = if (isAlcoholic) Color(0xFFFF3B3B) else Color(0xFF18B66A),
                        textColor = Color.White
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (darkMode) {
                        OutlinedButton(
                            onClick = onOpenDetail,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkCardColor,
                                contentColor = DarkTextPrimaryColor
                            ),
                            border = BorderStroke(1.dp, PrimarySoftColor)
                        ) {
                            Text(
                                text = "Voir la recette   ›",
                                color = DarkTextPrimaryColor,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clickable { onOpenDetail() },
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            shadowElevation = 0.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Voir la recette   ›",
                                    color = TextPrimaryColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .scale(heartScale.value)
                            .clickable { onFavoriteClick() },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isFavorite) PrimaryColor else Color(0x664B3A33),
                        shadowElevation = 0.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favori",
                                tint = if (isFavorite) Color.White else Color(0xFFF3F4F6),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    cocktail: Cocktail,
    darkMode: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val isAlcoholic = cocktail.strAlcoholic.equals("Alcoholic", ignoreCase = true)
    val heartScale = remember { Animatable(1f) }

    LaunchedEffect(isFavorite) {
        heartScale.snapTo(0.9f)
        heartScale.animateTo(
            targetValue = 1.18f,
            animationSpec = tween(durationMillis = 160, easing = FastOutSlowInEasing)
        )
        heartScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 140, easing = FastOutSlowInEasing)
        )
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(darkMode)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .clickable { onOpenDetail() }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = cocktail.strDrinkThumb,
                contentDescription = cocktail.strDrink,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    cocktail.strDrink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = titleColor(darkMode)
                )
                Text(
                    cocktail.strCategory ?: "Other / Unknown",
                    color = subColor(darkMode)
                )

                if (!cocktail.strAlcoholic.isNullOrBlank()) {
                    InfoChip(
                        text = if (isAlcoholic) "🍸 Alcoolisé" else "🧃 Sans alcool",
                        background = if (isAlcoholic) Color(0xFFFFF1F2) else Color(0xFFEAF8E2),
                        textColor = if (isAlcoholic) PrimaryColor else Color(0xFF18B66A)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .scale(heartScale.value)
                    .clickable { onFavoriteClick() },
                shape = RoundedCornerShape(14.dp),
                color = if (isFavorite) PrimaryColor else Color(0xFFFFEBEE),
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (isFavorite) Color.White else PrimaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    cocktail: CocktailEntity,
    darkMode: Boolean,
    rating: Int,
    onRemoveFavorite: () -> Unit,
    onOpenDetail: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(darkMode)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = cocktail.thumbnail,
                contentDescription = cocktail.name,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    cocktail.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = titleColor(darkMode)
                )
                Text(cocktail.category, color = subColor(darkMode))

                if (rating > 0) {
                    ReadOnlyRatingStars(rating = rating)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onOpenDetail,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (darkMode) DarkCardColor else SurfaceColor,
                            contentColor = if (darkMode) DarkTextPrimaryColor else TextPrimaryColor
                        ),
                        border = BorderStroke(1.dp, PrimarySoftColor)
                    ) {
                        Text("Voir la recette")
                    }

                    OutlinedButton(
                        onClick = onRemoveFavorite,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, PrimaryColor)
                    ) {
                        Text("Retirer", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    totalFavorites: Int,
    totalAlcoholic: Int,
    totalNonAlcoholic: Int,
    darkMode: Boolean
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(darkMode)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "📊 Statistiques",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = titleColor(darkMode)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = totalFavorites.toString(),
                    label = "Favoris",
                    valueColor = PrimaryColor,
                    darkMode = darkMode
                )
                StatItem(
                    value = totalAlcoholic.toString(),
                    label = "Alcoolisés",
                    valueColor = PrimaryColor,
                    darkMode = darkMode
                )
                StatItem(
                    value = totalNonAlcoholic.toString(),
                    label = "Sans alcool",
                    valueColor = Color(0xFF18B66A),
                    darkMode = darkMode
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    valueColor: Color,
    darkMode: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = valueColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = subColor(darkMode),
            fontSize = 13.sp
        )
    }
}

@Composable
fun HistoryCard(
    item: HistoryItem,
    darkMode: Boolean,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onSelectToggle: () -> Unit,
    onDelete: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val historyCardColor = cardColor(darkMode)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                if (darkMode) {
                    Color(0x33E34B5F)
                } else {
                    Color(0xFFFFEBEE)
                }
            } else {
                historyCardColor
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .clickable {
                if (isSelectionMode) {
                    onSelectToggle()
                } else {
                    onOpenDetail()
                }
            }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = item.name,
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = titleColor(darkMode)
                )
                Text(
                    item.category,
                    color = subColor(darkMode)
                )
                Text(
                    formatter.format(Date(item.viewedAt)),
                    color = subColor(darkMode)
                )
            }

            if (isSelectionMode) {
                Surface(
                    onClick = onSelectToggle,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) PrimaryColor else historyCardColor,
                    shadowElevation = 0.dp
                ) {
                    Text(
                        text = if (isSelected) "✓" else "○",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        color = if (isSelected) Color.White else subColor(darkMode)
                    )
                }
            } else {
                Surface(
                    onClick = onDelete,
                    shape = RoundedCornerShape(12.dp),
                    color = historyCardColor,
                    shadowElevation = 0.dp
                ) {
                    Text(
                        text = "🗑",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        color = PrimaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(
    cocktail: Cocktail,
    darkMode: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor(darkMode)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .clickable { onClick() }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = cocktail.strDrinkThumb,
                contentDescription = cocktail.strDrink,
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(cocktail.strDrink, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = titleColor(darkMode))
                Text(cocktail.strCategory ?: "Other / Unknown", color = subColor(darkMode))
            }
        }
    }
}

@Composable
fun TipText(text: String) {
    Text(
        text = text,
        color = TextSecondaryColor,
        fontSize = 14.sp,
        lineHeight = 22.sp
    )
}