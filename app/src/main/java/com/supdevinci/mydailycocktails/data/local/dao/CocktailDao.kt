package com.supdevinci.mydailycocktails.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supdevinci.mydailycocktails.data.local.entities.CocktailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cocktail: CocktailEntity)

    @Query("DELETE FROM favorite_cocktails WHERE idDrink = :idDrink")
    suspend fun deleteById(idDrink: String)

    @Query("SELECT * FROM favorite_cocktails ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<CocktailEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cocktails WHERE idDrink = :idDrink)")
    suspend fun isFavorite(idDrink: String): Boolean
}