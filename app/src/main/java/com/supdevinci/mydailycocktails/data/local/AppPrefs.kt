package com.supdevinci.mydailycocktails.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.supdevinci.mydailycocktails.model.Cocktail
import com.supdevinci.mydailycocktails.model.HistoryItem

class AppPrefs(context: Context) {

    private val prefs = context.getSharedPreferences("my_daily_cocktails_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val NOTES_KEY = "notes"
        private const val RATINGS_KEY = "ratings"
        private const val HISTORY_KEY = "history"
    }

    fun saveNote(cocktailId: String, note: String) {
        val notes = getNotes().toMutableMap()
        notes[cocktailId] = note
        prefs.edit().putString(NOTES_KEY, gson.toJson(notes)).apply()
    }

    fun getNote(cocktailId: String): String {
        return getNotes()[cocktailId].orEmpty()
    }

    private fun getNotes(): Map<String, String> {
        val json = prefs.getString(NOTES_KEY, null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveRating(cocktailId: String, rating: Int) {
        val ratings = getRatings().toMutableMap()
        ratings[cocktailId] = rating
        prefs.edit().putString(RATINGS_KEY, gson.toJson(ratings)).apply()
    }

    fun getRating(cocktailId: String): Int {
        return getRatings()[cocktailId] ?: 0
    }

    private fun getRatings(): Map<String, Int> {
        val json = prefs.getString(RATINGS_KEY, null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addToHistory(cocktail: Cocktail) {
        val history = getHistory().toMutableList()
        history.removeAll { it.idDrink == cocktail.idDrink }
        history.add(
            0,
            HistoryItem(
                idDrink = cocktail.idDrink,
                name = cocktail.strDrink,
                thumbnail = cocktail.strDrinkThumb,
                category = cocktail.strCategory ?: "Other / Unknown",
                viewedAt = System.currentTimeMillis()
            )
        )
        saveHistory(history.take(50))
    }

    fun getHistory(): List<HistoryItem> {
        val json = prefs.getString(HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun removeFromHistory(idDrink: String) {
        val updatedHistory = getHistory().filterNot { it.idDrink == idDrink }
        saveHistory(updatedHistory)
    }

    fun removeSelectedFromHistory(selectedIds: Set<String>) {
        val updatedHistory = getHistory().filterNot { it.idDrink in selectedIds }
        saveHistory(updatedHistory)
    }

    fun clearHistory() {
        saveHistory(emptyList())
    }

    private fun saveHistory(history: List<HistoryItem>) {
        prefs.edit().putString(HISTORY_KEY, gson.toJson(history)).apply()
    }
}