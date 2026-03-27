package com.supdevinci.mydailycocktails.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.supdevinci.mydailycocktails.ui.theme.AppBgBottom
import com.supdevinci.mydailycocktails.ui.theme.AppBgMid
import com.supdevinci.mydailycocktails.ui.theme.AppBgTop
import com.supdevinci.mydailycocktails.ui.theme.DarkBg
import com.supdevinci.mydailycocktails.ui.theme.DarkBg2
import com.supdevinci.mydailycocktails.ui.theme.DarkBg3
import com.supdevinci.mydailycocktails.view.CocktailScreen
import com.supdevinci.mydailycocktails.view.DetailScreen
import com.supdevinci.mydailycocktails.view.FavoritesScreen
import com.supdevinci.mydailycocktails.view.HistoryScreen
import com.supdevinci.mydailycocktails.view.SearchScreen
import com.supdevinci.mydailycocktails.view.SplashScreen
import com.supdevinci.mydailycocktails.view.components.BottomBar
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel

object Routes {
    const val SPLASH = "splash"
    const val RANDOM = "random"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val HISTORY = "history"
    const val DETAIL = "detail"
    const val DETAIL_ARG = "cocktailId"
    const val DETAIL_WITH_ARG = "$DETAIL/{$DETAIL_ARG}"

    fun detailRoute(cocktailId: String): String = "$DETAIL/$cocktailId"
}

@Composable
fun CocktailNavHost(
    viewModel: CocktailViewModel,
    darkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val backgroundBrush = if (darkMode) {
        Brush.verticalGradient(listOf(DarkBg, DarkBg2, DarkBg3))
    } else {
        Brush.verticalGradient(listOf(AppBgTop, AppBgMid, AppBgBottom))
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    val popped = navController.popBackStack(
                        route = route,
                        inclusive = false
                    )

                    if (!popped) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                visible = currentDestination?.hierarchy?.any {
                    it.route in listOf(
                        Routes.RANDOM,
                        Routes.SEARCH,
                        Routes.FAVORITES,
                        Routes.HISTORY,
                        Routes.DETAIL_WITH_ARG
                    )
                } == true && currentRoute != Routes.SPLASH
            )
        }
    ) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH
            ) {
                composable(
                    route = Routes.SPLASH,
                    enterTransition = { fadeIn(tween(300)) },
                    exitTransition = { fadeOut(tween(250)) }
                ) {
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Routes.RANDOM,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    }
                ) {
                    CocktailScreen(
                        viewModel = viewModel,
                        darkMode = darkMode,
                        onToggleTheme = onToggleTheme,
                        bottomPadding = innerPadding,
                        onOpenDetail = { cocktail ->
                            navController.navigate(Routes.detailRoute(cocktail.idDrink))
                        }
                    )
                }

                composable(
                    route = Routes.SEARCH,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    }
                ) {
                    SearchScreen(
                        viewModel = viewModel,
                        darkMode = darkMode,
                        onToggleTheme = onToggleTheme,
                        bottomPadding = innerPadding,
                        onOpenDetail = { cocktail ->
                            navController.navigate(Routes.detailRoute(cocktail.idDrink))
                        }
                    )
                }

                composable(
                    route = Routes.FAVORITES,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    }
                ) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        darkMode = darkMode,
                        onToggleTheme = onToggleTheme,
                        onOpenDetail = { id ->
                            navController.navigate(Routes.detailRoute(id))
                        }
                    )
                }

                composable(
                    route = Routes.HISTORY,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 4 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 4 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(250))
                    }
                ) {
                    HistoryScreen(
                        viewModel = viewModel,
                        darkMode = darkMode,
                        onToggleTheme = onToggleTheme,
                        onOpenDetail = { id ->
                            navController.navigate(Routes.detailRoute(id))
                        }
                    )
                }

                composable(
                    route = Routes.DETAIL_WITH_ARG,
                    arguments = listOf(
                        navArgument(Routes.DETAIL_ARG) {
                            type = NavType.StringType
                        }
                    ),
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it / 3 },
                            animationSpec = tween(340)
                        ) + fadeIn(tween(340))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 3 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(240))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(320)
                        ) + fadeIn(tween(320))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it / 3 },
                            animationSpec = tween(300)
                        ) + fadeOut(tween(240))
                    }
                ) { backStackEntry ->
                    val cocktailId = backStackEntry.arguments?.getString(Routes.DETAIL_ARG) ?: ""

                    DetailScreen(
                        viewModel = viewModel,
                        cocktailId = cocktailId,
                        darkMode = darkMode,
                        onToggleTheme = onToggleTheme,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}