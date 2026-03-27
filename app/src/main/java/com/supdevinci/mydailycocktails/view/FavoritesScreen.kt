package com.supdevinci.mydailycocktails.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supdevinci.mydailycocktails.data.utils.LocalCocktailState
import com.supdevinci.mydailycocktails.view.components.EmptyCard
import com.supdevinci.mydailycocktails.view.components.ErrorCard
import com.supdevinci.mydailycocktails.view.components.FavoriteCard
import com.supdevinci.mydailycocktails.view.components.Header
import com.supdevinci.mydailycocktails.view.components.LoadingCard
import com.supdevinci.mydailycocktails.view.components.StatsCard
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel

@Composable
fun FavoritesScreen(
    viewModel: CocktailViewModel,
    darkMode: Boolean,
    onToggleTheme: () -> Unit,
    onOpenDetail: (String) -> Unit
) {
    val localState by viewModel.localState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Header(
                title = "Mes favoris",
                subtitle = when (localState) {
                    is LocalCocktailState.Success ->
                        "${(localState as LocalCocktailState.Success).cocktails.size} cocktail sauvegardé(s)"
                    else -> "Tes cocktails sauvegardés"
                },
                darkMode = darkMode,
                onToggleTheme = onToggleTheme
            )
        }

        when (localState) {
            is LocalCocktailState.Loading -> {
                item {
                    LoadingCard(darkMode = darkMode)
                }
            }

            is LocalCocktailState.Empty -> {
                item {
                    EmptyCard(
                        title = "Aucun favori",
                        subtitle = "Ajoute des cocktails depuis l'accueil ou la recherche.",
                        darkMode = darkMode
                    )
                }
            }

            is LocalCocktailState.Error -> {
                item {
                    ErrorCard(
                        message = (localState as LocalCocktailState.Error).message,
                        darkMode = darkMode,
                        onRetry = {}
                    )
                }
            }

            is LocalCocktailState.Success -> {
                val cocktails = (localState as LocalCocktailState.Success).cocktails

                items(cocktails, key = { it.idDrink }) { cocktail ->
                    FavoriteCard(
                        cocktail = cocktail,
                        darkMode = darkMode,
                        rating = viewModel.getRating(cocktail.idDrink),
                        onRemoveFavorite = { viewModel.removeFavorite(cocktail.idDrink) },
                        onOpenDetail = { onOpenDetail(cocktail.idDrink) }
                    )
                }

                item {
                    val alcoholicCount = cocktails.count {
                        it.alcoholic.equals("Alcoholic", ignoreCase = true)
                    }

                    val nonAlcoholicCount = cocktails.count {
                        !it.alcoholic.equals("Alcoholic", ignoreCase = true)
                    }

                    StatsCard(
                        totalFavorites = cocktails.size,
                        totalAlcoholic = alcoholicCount,
                        totalNonAlcoholic = nonAlcoholicCount,
                        darkMode = darkMode
                    )
                }
            }
        }
    }
}