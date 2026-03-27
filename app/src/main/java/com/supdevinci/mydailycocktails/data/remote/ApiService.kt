package com.supdevinci.mydailycocktails.data.remote

import com.supdevinci.mydailycocktails.model.CocktailResponse
import com.supdevinci.mydailycocktails.model.FilterResponse
import com.supdevinci.mydailycocktails.model.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailResponse

    @GET("search.php")
    suspend fun searchCocktailsByName(
        @Query("s") name: String
    ): CocktailResponse

    @GET("lookup.php")
    suspend fun getCocktailById(
        @Query("i") id: String
    ): CocktailResponse

    @GET("filter.php")
    suspend fun filterCocktailsByIngredient(
        @Query("i") ingredient: String
    ): FilterResponse

    @GET("filter.php")
    suspend fun filterCocktailsByCategory(
        @Query("c") category: String
    ): FilterResponse

    @GET("filter.php")
    suspend fun filterCocktailsByAlcoholic(
        @Query("a") alcoholic: String
    ): FilterResponse

    @GET("search.php")
    suspend fun searchCocktailsByFirstLetter(
        @Query("f") letter: String
    ): CocktailResponse

    @GET("list.php")
    suspend fun getCategories(
        @Query("c") value: String = "list"
    ): ListResponse

    @GET("list.php")
    suspend fun getAlcoholicFilters(
        @Query("a") value: String = "list"
    ): ListResponse

    @GET("list.php")
    suspend fun getGlassList(
        @Query("g") value: String = "list"
    ): ListResponse

    @GET("list.php")
    suspend fun getIngredientList(
        @Query("i") value: String = "list"
    ): ListResponse
}