package com.supdevinci.mydailycocktails.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_cocktails")
data class CocktailEntity(
    @PrimaryKey
    val idDrink: String,
    val name: String,
    val instructions: String,
    val thumbnail: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val createdAt: Date = Date()
)