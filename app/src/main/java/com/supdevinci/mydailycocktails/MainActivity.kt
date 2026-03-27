package com.supdevinci.mydailycocktails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.supdevinci.mydailycocktails.navigation.CocktailNavHost
import com.supdevinci.mydailycocktails.ui.theme.MyDailyCocktailsTheme
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var darkMode by rememberSaveable { mutableStateOf(false) }

            MyDailyCocktailsTheme(darkTheme = darkMode) {
                CocktailNavHost(
                    viewModel = viewModel,
                    darkMode = darkMode,
                    onToggleTheme = { darkMode = !darkMode }
                )
            }
        }
    }
}