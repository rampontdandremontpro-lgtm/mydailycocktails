package com.supdevinci.mydailycocktails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.mydailycocktails.data.local.AppPrefs
import com.supdevinci.mydailycocktails.data.local.CocktailDatabase
import com.supdevinci.mydailycocktails.data.local.entities.CocktailEntity
import com.supdevinci.mydailycocktails.data.remote.RetrofitInstance
import com.supdevinci.mydailycocktails.data.utils.LocalCocktailState
import com.supdevinci.mydailycocktails.model.Cocktail
import com.supdevinci.mydailycocktails.model.CocktailListItem
import com.supdevinci.mydailycocktails.model.CocktailState
import com.supdevinci.mydailycocktails.model.HistoryItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class SearchUiState(
    val query: String = "",
    val results: List<Cocktail> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

class CocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitInstance.api
    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val appPrefs = AppPrefs(application)

    private val _homeState = MutableStateFlow<CocktailState>(CocktailState.Loading)
    val homeState: StateFlow<CocktailState> = _homeState.asStateFlow()

    private val _detailState = MutableStateFlow<CocktailState>(CocktailState.Loading)
    val detailState: StateFlow<CocktailState> = _detailState.asStateFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    private val _localState = MutableStateFlow<LocalCocktailState>(LocalCocktailState.Loading)
    val localState: StateFlow<LocalCocktailState> = _localState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _suggestions = MutableStateFlow<List<Cocktail>>(emptyList())
    val suggestions: StateFlow<List<Cocktail>> = _suggestions.asStateFlow()

    init {
        fetchRandomCocktail()
        observeFavorites()
        refreshHistory()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            try {
                cocktailDao.getAllFavorites().collect { cocktails ->
                    _favoriteIds.value = cocktails.map { it.idDrink }.toSet()
                    _localState.value = if (cocktails.isEmpty()) {
                        LocalCocktailState.Empty
                    } else {
                        LocalCocktailState.Success(cocktails)
                    }
                }
            } catch (e: Exception) {
                _localState.value = LocalCocktailState.Error("Erreur DB : ${e.message}")
            }
        }
    }

    fun refreshHistory() {
        _history.value = appPrefs.getHistory()
    }

    private fun addToHistory(cocktail: Cocktail) {
        appPrefs.addToHistory(cocktail)
        refreshHistory()
    }

    fun removeHistoryItem(idDrink: String) {
        appPrefs.removeFromHistory(idDrink)
        refreshHistory()
    }

    fun removeSelectedHistory(selectedIds: Set<String>) {
        appPrefs.removeSelectedFromHistory(selectedIds)
        refreshHistory()
    }

    fun clearHistory() {
        appPrefs.clearHistory()
        refreshHistory()
    }

    fun fetchRandomCocktail() {
        viewModelScope.launch {
            _homeState.value = CocktailState.Loading
            try {
                val cocktail = api.getRandomCocktail().drinks?.firstOrNull()
                _homeState.value = if (cocktail != null) {
                    addToHistory(cocktail)
                    CocktailState.Success(cocktail)
                } else {
                    CocktailState.Error("Aucun cocktail trouvé")
                }
            } catch (e: Exception) {
                _homeState.value = CocktailState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchUiState.value = _searchUiState.value.copy(query = query)
    }

    fun searchCocktails(
        query: String,
        typeFilter: String,
        categoryFilter: String
    ) {
        viewModelScope.launch {
            _searchUiState.value = _searchUiState.value.copy(
                query = query,
                isLoading = true,
                errorMessage = null,
                hasSearched = true
            )

            try {
                val trimmedQuery = query.trim()
                val hasQuery = trimmedQuery.isNotEmpty()
                val hasTypeFilter = typeFilter != "Tous"
                val hasCategoryFilter = categoryFilter != "Toutes"

                if (!hasQuery && !hasTypeFilter && !hasCategoryFilter) {
                    _searchUiState.value = _searchUiState.value.copy(
                        results = emptyList(),
                        isLoading = false,
                        errorMessage = "Écris un nom, un ingrédient ou choisis au moins un filtre.",
                        hasSearched = true
                    )
                    return@launch
                }

                val results = when {
                    hasQuery -> searchByNameOrIngredient(trimmedQuery, typeFilter, categoryFilter)
                    else -> searchByFiltersOnly(typeFilter, categoryFilter)
                }

                _searchUiState.value = _searchUiState.value.copy(
                    results = results,
                    isLoading = false,
                    errorMessage = if (results.isEmpty()) "Aucun résultat" else null,
                    hasSearched = true
                )
            } catch (e: Exception) {
                _searchUiState.value = _searchUiState.value.copy(
                    results = emptyList(),
                    isLoading = false,
                    errorMessage = "Erreur : ${e.message}",
                    hasSearched = true
                )
            }
        }
    }

    private suspend fun searchByNameOrIngredient(
        query: String,
        typeFilter: String,
        categoryFilter: String
    ): List<Cocktail> {
        val byName = api.searchCocktailsByName(query).drinks.orEmpty()

        val fullResults = if (byName.isNotEmpty()) {
            byName
        } else {
            val ingredientItems = api.filterCocktailsByIngredient(query).drinks.orEmpty()
            resolveFullCocktails(ingredientItems)
        }

        return fullResults.filterByTypeAndCategory(
            typeFilter = typeFilter,
            categoryFilter = categoryFilter
        )
    }

    private suspend fun searchByFiltersOnly(
        typeFilter: String,
        categoryFilter: String
    ): List<Cocktail> {
        val hasTypeFilter = typeFilter != "Tous"
        val hasCategoryFilter = categoryFilter != "Toutes"

        val byTypeItems = when (typeFilter) {
            "Alcoolisé" -> api.filterCocktailsByAlcoholic("Alcoholic").drinks.orEmpty()
            "Sans alcool" -> api.filterCocktailsByAlcoholic("Non_Alcoholic").drinks.orEmpty()
            else -> emptyList()
        }

        val byCategoryItems = when (categoryFilter) {
            "Toutes" -> emptyList()
            else -> api.filterCocktailsByCategory(categoryFilter).drinks.orEmpty()
        }

        return when {
            hasTypeFilter && hasCategoryFilter -> {
                val categoryIds = byCategoryItems.map { it.idDrink }.toSet()
                val mergedItems = byTypeItems.filter { it.idDrink in categoryIds }

                mapFilterItemsToPartialCocktails(
                    items = mergedItems,
                    typeFilter = typeFilter,
                    categoryFilter = categoryFilter
                )
            }

            hasTypeFilter -> {
                mapFilterItemsToPartialCocktails(
                    items = byTypeItems,
                    typeFilter = typeFilter,
                    categoryFilter = categoryFilter
                )
            }

            hasCategoryFilter -> {
                // Ici on reconstruit les cocktails complets pour récupérer le type alcoolisé / non alcoolisé
                resolveFullCocktails(byCategoryItems).filterByTypeAndCategory(
                    typeFilter = typeFilter,
                    categoryFilter = categoryFilter
                )
            }

            else -> emptyList()
        }
    }

    private suspend fun resolveFullCocktails(items: List<CocktailListItem>): List<Cocktail> =
        coroutineScope {
            items
                .distinctBy { it.idDrink }
                .take(20)
                .map { item ->
                    async {
                        api.getCocktailById(item.idDrink).drinks?.firstOrNull()
                    }
                }
                .awaitAll()
                .filterNotNull()
        }

    private fun mapFilterItemsToPartialCocktails(
        items: List<CocktailListItem>,
        typeFilter: String,
        categoryFilter: String
    ): List<Cocktail> {
        val alcoholicValue = when (typeFilter) {
            "Alcoolisé" -> "Alcoholic"
            "Sans alcool" -> "Non alcoholic"
            else -> null
        }

        val categoryValue = if (categoryFilter == "Toutes") null else categoryFilter

        return items
            .distinctBy { it.idDrink }
            .map { item ->
                Cocktail(
                    idDrink = item.idDrink,
                    strDrink = item.strDrink,
                    strDrinkThumb = item.strDrinkThumb,
                    strCategory = categoryValue,
                    strAlcoholic = alcoholicValue,
                    strGlass = null,
                    strInstructions = null
                )
            }
    }

    private fun List<Cocktail>.filterByTypeAndCategory(
        typeFilter: String,
        categoryFilter: String
    ): List<Cocktail> {
        return this
            .filter { cocktail ->
                when (typeFilter) {
                    "Alcoolisé" -> cocktail.strAlcoholic.equals("Alcoholic", ignoreCase = true)
                    "Sans alcool" -> {
                        cocktail.strAlcoholic.equals("Non alcoholic", ignoreCase = true) ||
                                cocktail.strAlcoholic.equals("Non Alcoholic", ignoreCase = true)
                    }
                    else -> true
                }
            }
            .filter { cocktail ->
                if (categoryFilter == "Toutes") true
                else cocktail.strCategory == categoryFilter
            }
    }

    fun getCocktailById(id: String) {
        viewModelScope.launch {
            _detailState.value = CocktailState.Loading
            try {
                val cocktail = api.getCocktailById(id).drinks?.firstOrNull()
                _detailState.value = if (cocktail != null) {
                    addToHistory(cocktail)
                    loadSuggestions(cocktail.idDrink)
                    CocktailState.Success(cocktail)
                } else {
                    CocktailState.Error("Cocktail introuvable")
                }
            } catch (e: Exception) {
                _detailState.value = CocktailState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun loadSuggestions(excludedId: String) {
        viewModelScope.launch {
            try {
                val jobs = List(4) { async { api.getRandomCocktail().drinks?.firstOrNull() } }
                val results = jobs.mapNotNull { it.await() }
                    .filter { it.idDrink != excludedId }
                    .distinctBy { it.idDrink }
                    .take(3)
                _suggestions.value = results
            } catch (_: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }

    fun toggleFavorite(cocktail: Cocktail) {
        viewModelScope.launch {
            val currentFavorites = _favoriteIds.value.toMutableSet()
            val wasFavorite = currentFavorites.contains(cocktail.idDrink)

            if (wasFavorite) {
                currentFavorites.remove(cocktail.idDrink)
            } else {
                currentFavorites.add(cocktail.idDrink)
            }

            _favoriteIds.value = currentFavorites.toSet()

            try {
                if (wasFavorite) {
                    cocktailDao.deleteById(cocktail.idDrink)
                } else {
                    cocktailDao.insert(
                        CocktailEntity(
                            idDrink = cocktail.idDrink,
                            name = cocktail.strDrink,
                            instructions = cocktail.strInstructions ?: "",
                            thumbnail = cocktail.strDrinkThumb,
                            category = cocktail.strCategory ?: "",
                            alcoholic = cocktail.strAlcoholic ?: "",
                            glass = cocktail.strGlass ?: "",
                            createdAt = Date()
                        )
                    )
                }
            } catch (e: Exception) {
                _favoriteIds.value = if (wasFavorite) {
                    _favoriteIds.value + cocktail.idDrink
                } else {
                    _favoriteIds.value - cocktail.idDrink
                }
                _localState.value = LocalCocktailState.Error("Erreur favori : ${e.message}")
            }
        }
    }

    fun removeFavorite(idDrink: String) {
        viewModelScope.launch {
            val oldFavorites = _favoriteIds.value
            _favoriteIds.value = oldFavorites - idDrink

            try {
                cocktailDao.deleteById(idDrink)
            } catch (e: Exception) {
                _favoriteIds.value = oldFavorites
                _localState.value = LocalCocktailState.Error("Erreur suppression : ${e.message}")
            }
        }
    }

    fun isFavorite(idDrink: String): Boolean = _favoriteIds.value.contains(idDrink)

    fun saveNote(cocktailId: String, note: String) {
        appPrefs.saveNote(cocktailId, note)
    }

    fun getNote(cocktailId: String): String = appPrefs.getNote(cocktailId)

    fun saveRating(cocktailId: String, rating: Int) {
        appPrefs.saveRating(cocktailId, rating)
    }

    fun getRating(cocktailId: String): Int = appPrefs.getRating(cocktailId)
}