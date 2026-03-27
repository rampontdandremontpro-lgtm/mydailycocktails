package com.supdevinci.mydailycocktails.model

sealed interface CocktailState {
    data object Loading : CocktailState
    data class Success(val cocktail: Cocktail) : CocktailState
    data class SearchSuccess(val cocktails: List<Cocktail>) : CocktailState
    data class FilterSuccess(val cocktails: List<CocktailListItem>) : CocktailState
    data class ListSuccess(val items: List<ListItem>) : CocktailState
    data class Error(val message: String) : CocktailState
}