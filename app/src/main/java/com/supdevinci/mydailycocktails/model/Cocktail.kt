package com.supdevinci.mydailycocktails.model

data class Cocktail(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strCategory: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,

    val strIngredient1: String? = null,
    val strIngredient2: String? = null,
    val strIngredient3: String? = null,
    val strIngredient4: String? = null,
    val strIngredient5: String? = null,
    val strIngredient6: String? = null,
    val strIngredient7: String? = null,
    val strIngredient8: String? = null,
    val strIngredient9: String? = null,
    val strIngredient10: String? = null,

    val strMeasure1: String? = null,
    val strMeasure2: String? = null,
    val strMeasure3: String? = null,
    val strMeasure4: String? = null,
    val strMeasure5: String? = null,
    val strMeasure6: String? = null,
    val strMeasure7: String? = null,
    val strMeasure8: String? = null,
    val strMeasure9: String? = null,
    val strMeasure10: String? = null
) {
    fun getIngredientsWithMeasures(): List<String> {
        val items = listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5,
            strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7,
            strIngredient8 to strMeasure8,
            strIngredient9 to strMeasure9,
            strIngredient10 to strMeasure10
        )

        return items.mapNotNull { (ingredient, measure) ->
            if (!ingredient.isNullOrBlank()) {
                "${measure?.trim().orEmpty()} ${ingredient.trim()}".trim()
            } else {
                null
            }
        }
    }
}