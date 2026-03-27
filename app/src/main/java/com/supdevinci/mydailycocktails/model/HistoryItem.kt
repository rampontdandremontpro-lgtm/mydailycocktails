package com.supdevinci.mydailycocktails.model

data class HistoryItem(
    val idDrink: String,
    val name: String,
    val thumbnail: String,
    val category: String,
    val viewedAt: Long
)