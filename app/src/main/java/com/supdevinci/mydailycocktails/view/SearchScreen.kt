package com.supdevinci.mydailycocktails.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supdevinci.mydailycocktails.model.Cocktail
import com.supdevinci.mydailycocktails.ui.theme.Card as CardColor
import com.supdevinci.mydailycocktails.ui.theme.DarkCard as DarkCardColor
import com.supdevinci.mydailycocktails.ui.theme.DarkTextPrimary as DarkTextPrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.DarkTextSecondary as DarkTextSecondaryColor
import com.supdevinci.mydailycocktails.ui.theme.Primary as PrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.TextPrimary as TextPrimaryColor
import com.supdevinci.mydailycocktails.ui.theme.TextSecondary as TextSecondaryColor
import com.supdevinci.mydailycocktails.view.components.AppCard
import com.supdevinci.mydailycocktails.view.components.ErrorCard
import com.supdevinci.mydailycocktails.view.components.LoadingCard
import com.supdevinci.mydailycocktails.view.components.PageHeader
import com.supdevinci.mydailycocktails.view.components.ScrollToTopFab
import com.supdevinci.mydailycocktails.view.components.SearchResultCard
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel
import kotlinx.coroutines.launch

private fun titleColor(darkMode: Boolean): Color =
    if (darkMode) DarkTextPrimaryColor else TextPrimaryColor

private fun subColor(darkMode: Boolean): Color =
    if (darkMode) DarkTextSecondaryColor else TextSecondaryColor

private fun chipBgColor(darkMode: Boolean): Color =
    if (darkMode) DarkCardColor else CardColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: CocktailViewModel,
    darkMode: Boolean,
    onToggleTheme: () -> Unit,
    bottomPadding: PaddingValues,
    onOpenDetail: (Cocktail) -> Unit
) {
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    var showFilters by rememberSaveable { mutableStateOf(false) }
    var typeFilter by rememberSaveable { mutableStateOf("Tous") }
    var categoryFilter by rememberSaveable { mutableStateOf("Toutes") }

    val categories = listOf(
        "Toutes",
        "Ordinary Drink",
        "Cocktail",
        "Shot",
        "Coffee / Tea",
        "Soft Drink",
        "Cocoa"
    )

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 500
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            contentPadding = PaddingValues(bottom = bottomPadding.calculateBottomPadding() + 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PageHeader(
                    title = "Rechercher",
                    subtitle = "Trouve ton cocktail par son nom ou un ingrédient",
                    darkMode = darkMode,
                    onToggleTheme = onToggleTheme
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchUiState.query,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = {
                            Text(
                                text = "Ex: Mojito, Gin, Kiwi...",
                                color = subColor(darkMode)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .size(46.dp)
                            .clickable {
                                viewModel.searchCocktails(
                                    query = searchUiState.query,
                                    typeFilter = typeFilter,
                                    categoryFilter = categoryFilter
                                )
                            },
                        color = PrimaryColor,
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Rechercher",
                            tint = Color.White,
                            modifier = Modifier.padding(11.dp)
                        )
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.clickable { showFilters = !showFilters },
                    shape = RoundedCornerShape(12.dp),
                    color = chipBgColor(darkMode),
                    shadowElevation = 0.dp
                ) {
                    Text(
                        text = "⌕ Filtres",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = titleColor(darkMode)
                    )
                }
            }

            if (showFilters) {
                item {
                    AppCard(darkMode = darkMode) {
                        Text(
                            text = "Type",
                            color = titleColor(darkMode)
                        )

                        Spacer(modifier = Modifier.padding(top = 4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChipItem(
                                text = "Tous",
                                isSelected = typeFilter == "Tous",
                                darkMode = darkMode,
                                onClick = { typeFilter = "Tous" }
                            )
                            FilterChipItem(
                                text = "🍸 Alcoolisé",
                                isSelected = typeFilter == "Alcoolisé",
                                darkMode = darkMode,
                                onClick = { typeFilter = "Alcoolisé" }
                            )
                            FilterChipItem(
                                text = "🧃 Sans alcool",
                                isSelected = typeFilter == "Sans alcool",
                                darkMode = darkMode,
                                onClick = { typeFilter = "Sans alcool" }
                            )
                        }

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Catégorie",
                            color = titleColor(darkMode)
                        )

                        Spacer(modifier = Modifier.padding(top = 4.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { label ->
                                FilterChipItem(
                                    text = label,
                                    isSelected = categoryFilter == label,
                                    darkMode = darkMode,
                                    onClick = { categoryFilter = label }
                                )
                            }
                        }
                    }
                }
            }

            when {
                searchUiState.isLoading -> {
                    item { LoadingCard(darkMode = darkMode) }
                }

                !searchUiState.hasSearched -> {
                    item { SearchEmptyBlock(darkMode = darkMode) }
                }

                searchUiState.errorMessage != null -> {
                    item {
                        ErrorCard(
                            message = searchUiState.errorMessage ?: "Erreur",
                            darkMode = darkMode,
                            onRetry = {
                                viewModel.searchCocktails(
                                    query = searchUiState.query,
                                    typeFilter = typeFilter,
                                    categoryFilter = categoryFilter
                                )
                            }
                        )
                    }
                }

                searchUiState.results.isEmpty() -> {
                    item {
                        Text(
                            text = "0 résultat",
                            color = titleColor(darkMode)
                        )
                    }
                }

                else -> {
                    item {
                        Text(
                            text = "${searchUiState.results.size} résultats",
                            color = titleColor(darkMode)
                        )
                    }

                    items(searchUiState.results, key = { it.idDrink }) { cocktail ->
                        SearchResultCard(
                            cocktail = cocktail,
                            darkMode = darkMode,
                            isFavorite = favoriteIds.contains(cocktail.idDrink),
                            onFavoriteClick = { viewModel.toggleFavorite(cocktail) },
                            onOpenDetail = { onOpenDetail(cocktail) }
                        )
                    }
                }
            }
        }

        ScrollToTopFab(
            visible = showScrollToTop,
            darkMode = darkMode,
            bottomOffset = bottomPadding.calculateBottomPadding() + 24.dp,
            onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        )
    }
}

@Composable
private fun FilterChipItem(
    text: String,
    isSelected: Boolean,
    darkMode: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PrimaryColor else chipBgColor(darkMode),
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            color = if (isSelected) Color.White else titleColor(darkMode)
        )
    }
}

@Composable
private fun SearchEmptyBlock(darkMode: Boolean) {
    AppCard(darkMode = darkMode) {
        Text(
            text = "Recherche un cocktail pour commencer",
            color = titleColor(darkMode)
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(
            text = "Essaye \"Mojito\", un ingrédient comme \"Gin\", ou juste les filtres.",
            color = subColor(darkMode)
        )
    }
}