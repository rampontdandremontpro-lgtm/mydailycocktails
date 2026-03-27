package com.supdevinci.mydailycocktails.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supdevinci.mydailycocktails.model.Cocktail
import com.supdevinci.mydailycocktails.model.CocktailState
import com.supdevinci.mydailycocktails.view.components.ErrorCard
import com.supdevinci.mydailycocktails.view.components.FigmaHeroCocktailCard
import com.supdevinci.mydailycocktails.view.components.GradientPrimaryButton
import com.supdevinci.mydailycocktails.view.components.HomeHeader
import com.supdevinci.mydailycocktails.view.components.LoadingCard
import com.supdevinci.mydailycocktails.view.components.TipText
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel

@Composable
fun CocktailScreen(
    viewModel: CocktailViewModel,
    darkMode: Boolean,
    onToggleTheme: () -> Unit,
    bottomPadding: PaddingValues,
    onOpenDetail: (Cocktail) -> Unit
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    var displayedCocktail by remember { mutableStateOf<Cocktail?>(null) }

    LaunchedEffect(state) {
        if (state is CocktailState.Success) {
            displayedCocktail = (state as CocktailState.Success).cocktail
        }
    }

    val isRefreshing = state is CocktailState.Loading && displayedCocktail != null
    val errorMessage = (state as? CocktailState.Error)?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = bottomPadding.calculateBottomPadding() + 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        HomeHeader(
            darkMode = darkMode,
            onToggleTheme = onToggleTheme
        )

        when {
            displayedCocktail != null -> {
                AnimatedContent(
                    targetState = displayedCocktail!!,
                    transitionSpec = {
                        (
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth / 2 },
                                    animationSpec = tween(420)
                                ) + fadeIn(
                                    animationSpec = tween(420)
                                ) + scaleIn(
                                    initialScale = 0.96f,
                                    animationSpec = tween(420)
                                )
                                ).togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                                    animationSpec = tween(300)
                                ) + fadeOut(
                                    animationSpec = tween(220)
                                ) + scaleOut(
                                    targetScale = 0.98f,
                                    animationSpec = tween(300)
                                )
                            )
                    },
                    label = "random_cocktail_animation"
                ) { animatedCocktail ->
                    FigmaHeroCocktailCard(
                        cocktail = animatedCocktail,
                        darkMode = darkMode,
                        isFavorite = favoriteIds.contains(animatedCocktail.idDrink),
                        onFavoriteClick = { viewModel.toggleFavorite(animatedCocktail) },
                        onOpenDetail = { onOpenDetail(animatedCocktail) }
                    )
                }

                GradientPrimaryButton(
                    text = if (isRefreshing) "Chargement..." else "⟳   Nouveau cocktail",
                    onClick = {
                        if (!isRefreshing) {
                            viewModel.fetchRandomCocktail()
                        }
                    }
                )

                TipText("💡 Astuce : Ajoute tes cocktails préférés en favoris pour les retrouver facilement")
            }

            state is CocktailState.Loading -> {
                LoadingCard(darkMode = darkMode)
            }

            errorMessage != null -> {
                ErrorCard(
                    message = errorMessage,
                    darkMode = darkMode,
                    onRetry = { viewModel.fetchRandomCocktail() }
                )
            }

            else -> {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}