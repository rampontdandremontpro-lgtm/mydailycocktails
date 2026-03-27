package com.supdevinci.mydailycocktails.data.utils

import com.supdevinci.mydailycocktails.data.local.entities.CocktailEntity

sealed interface LocalCocktailState {
    data object Loading : LocalCocktailState
    data object Empty : LocalCocktailState
    data class Success(val cocktails: List<CocktailEntity>) : LocalCocktailState
    data class Error(val message: String) : LocalCocktailState
}