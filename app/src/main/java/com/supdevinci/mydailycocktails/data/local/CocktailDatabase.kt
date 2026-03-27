package com.supdevinci.mydailycocktails.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.supdevinci.mydailycocktails.data.local.dao.CocktailDao
import com.supdevinci.mydailycocktails.data.local.entities.CocktailEntity

@Database(
    entities = [CocktailEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CocktailDatabase : RoomDatabase() {

    abstract fun cocktailDao(): CocktailDao

    companion object {
        @Volatile
        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(context: Context): CocktailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CocktailDatabase::class.java,
                    "cocktail_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}