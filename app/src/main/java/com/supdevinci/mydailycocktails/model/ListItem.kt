package com.supdevinci.mydailycocktails.model

data class ListItem(
    val strCategory: String? = null,
    val strAlcoholic: String? = null,
    val strGlass: String? = null,
    val strIngredient1: String? = null
) {
    fun displayName(): String {
        return strCategory
            ?: strAlcoholic
            ?: strGlass
            ?: strIngredient1
            ?: ""
    }
}