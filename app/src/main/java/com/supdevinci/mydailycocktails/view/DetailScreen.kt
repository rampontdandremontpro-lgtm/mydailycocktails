package com.supdevinci.mydailycocktails.view

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.supdevinci.mydailycocktails.model.CocktailState
import com.supdevinci.mydailycocktails.ui.theme.AppGlassStrongDark
import com.supdevinci.mydailycocktails.ui.theme.AppGlassStrongLight
import com.supdevinci.mydailycocktails.ui.theme.BrandCoral
import com.supdevinci.mydailycocktails.ui.theme.BrandLime
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.DarkTextPrimary
import com.supdevinci.mydailycocktails.ui.theme.DarkTextSecondary
import com.supdevinci.mydailycocktails.ui.theme.TextPrimary
import com.supdevinci.mydailycocktails.ui.theme.TextSecondary
import com.supdevinci.mydailycocktails.view.components.RatingStars
import com.supdevinci.mydailycocktails.view.components.ScrollToTopFab
import com.supdevinci.mydailycocktails.view.components.SuggestionCard
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    viewModel: CocktailViewModel,
    cocktailId: String,
    darkMode: Boolean,
    onToggleTheme: () -> Unit,
    bottomPadding: PaddingValues,
    onBack: () -> Unit
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf { scrollState.value > 600 }
    }

    LaunchedEffect(cocktailId) {
        viewModel.getCocktailById(cocktailId)
    }

    val cocktail = (state as? CocktailState.Success)?.cocktail

    if (cocktail == null) {
        val loadingTextColor = if (darkMode) DarkTextPrimary else TextPrimary

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chargement...",
                color = loadingTextColor
            )
        }
        return
    }

    val displayedCocktailId = cocktail.idDrink

    LaunchedEffect(displayedCocktailId) {
        scrollState.animateScrollTo(0)
    }

    var note by remember(displayedCocktailId) {
        mutableStateOf(viewModel.getNote(displayedCocktailId))
    }
    var rating by remember(displayedCocktailId) {
        mutableStateOf(viewModel.getRating(displayedCocktailId))
    }
    var showNoteSavedMessage by remember(displayedCocktailId) {
        mutableStateOf(false)
    }

    val ingredients = cocktail.getIngredientsWithMeasures()
    val isFavorite = favoriteIds.contains(displayedCocktailId)
    val isAlcoholic = cocktail.strAlcoholic.equals("Alcoholic", ignoreCase = true)

    val favoriteButtonScale = remember { Animatable(1f) }

    LaunchedEffect(isFavorite) {
        favoriteButtonScale.snapTo(0.96f)
        favoriteButtonScale.animateTo(
            targetValue = 1.04f,
            animationSpec = tween(durationMillis = 170, easing = FastOutSlowInEasing)
        )
        favoriteButtonScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 140, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(showNoteSavedMessage) {
        if (showNoteSavedMessage) {
            delay(2200)
            showNoteSavedMessage = false
        }
    }

    val pageTitleColor = if (darkMode) DarkTextPrimary else TextPrimary
    val pageSubColor = if (darkMode) DarkTextSecondary else TextSecondary
    val detailCardColor = if (darkMode) AppGlassStrongDark else AppGlassStrongLight
    val sectionSurfaceColor = if (darkMode) Color(0xFF2A2D36) else Color(0xFFF8FAFC)
    val backButtonColor = if (darkMode) Color(0xFF262A33) else Color.White.copy(alpha = 0.92f)
    val backIconColor = if (darkMode) Color(0xFFD4DAE5) else Color(0xFF475467)
    val backTextColor = if (darkMode) BrandCoral else BrandRed
    val shareButtonColor = if (darkMode) Color(0xFF262A33) else Color.White
    val shareIconColor = if (darkMode) Color(0xFFD4DAE5) else Color(0xFF374151)
    val favoriteIdleBg = if (darkMode) Color(0xFF2A2D36) else Color(0xFFE5E7EB)
    val favoriteIdleText = if (darkMode) DarkTextPrimary else Color(0xFF111827)
    val fieldContainerColor = if (darkMode) Color(0xFF23262E) else Color(0xFFFDFBFF)
    val fieldBorderColor = if (darkMode) Color(0xFF4A5060) else Color(0xFFA8A0AF)
    val saveNoteButtonColor = if (darkMode) BrandCoral else BrandLime
    val saveNoteTextColor = if (darkMode) Color(0xFF1A1D24) else Color(0xFF1C2433)

    val noteSavedBgColor = if (darkMode) Color(0xFF243126) else Color(0xFFEAF8EE)
    val noteSavedBorderColor = if (darkMode) Color(0xFF3F8F5A) else Color(0xFF9FD9AE)
    val noteSavedIconColor = if (darkMode) Color(0xFF7AE29C) else Color(0xFF2F9E59)
    val noteSavedTextColor = if (darkMode) Color(0xFFE8FFF0) else Color(0xFF1E6E3B)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(18.dp),
                    color = backButtonColor,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = backIconColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Retour",
                            color = backTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    onClick = onToggleTheme,
                    shape = RoundedCornerShape(18.dp),
                    color = backButtonColor,
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (darkMode) {
                                Icons.Outlined.LightMode
                            } else {
                                Icons.Outlined.DarkMode
                            },
                            contentDescription = "Changer le thème",
                            tint = backIconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = detailCardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box {
                    AsyncImage(
                        model = cocktail.strDrinkThumb,
                        contentDescription = cocktail.strDrink,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color(0x33000000),
                                        Color(0xB3000000)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = cocktail.strDrink,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (darkMode) Color(0xE6282B33) else Color.White.copy(alpha = 0.95f)
                            ) {
                                Text(
                                    text = cocktail.strCategory ?: "Cocktail",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    color = if (darkMode) DarkTextPrimary else Color(0xFF111827),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isAlcoholic) BrandRed else Color(0xFF18B66A)
                            ) {
                                Text(
                                    text = if (isAlcoholic) "🍸 Alcoolisé" else "🧃 Sans alcool",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.toggleFavorite(cocktail) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .scale(favoriteButtonScale.value),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFavorite) BrandRed else favoriteIdleBg,
                                contentColor = if (isFavorite) Color.White else favoriteIdleText
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) Color.White else pageSubColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = if (isFavorite) "En favori" else "Ajouter aux favoris",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            onClick = {
                                val text = buildString {
                                    append("${cocktail.strDrink}\n\n")
                                    append("Catégorie : ${cocktail.strCategory}\n")
                                    append("Type : ${cocktail.strAlcoholic}\n")
                                    append("Verre : ${cocktail.strGlass}\n\n")
                                    append("Ingrédients :\n")
                                    ingredients.forEach { append("- $it\n") }
                                    append("\nPréparation :\n${cocktail.strInstructions}")
                                }

                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, text)
                                }

                                context.startActivity(
                                    Intent.createChooser(intent, "Partager le cocktail")
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = shareButtonColor,
                            shadowElevation = 0.dp,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Partager",
                                    tint = shareIconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Votre note",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = pageTitleColor
                        )

                        RatingStars(
                            rating = rating,
                            onRatingSelected = {
                                rating = it
                                viewModel.saveRating(displayedCocktailId, it)
                            }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Type de verre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = pageTitleColor
                        )
                        Text(
                            text = "🥃 ${cocktail.strGlass ?: "Verre non renseigné"}",
                            color = pageTitleColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Ingrédients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = pageTitleColor
                        )

                        ingredients.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = sectionSurfaceColor
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    color = pageTitleColor
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Préparation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = pageTitleColor
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = sectionSurfaceColor
                        ) {
                            Text(
                                text = cocktail.strInstructions ?: "Aucune instruction",
                                modifier = Modifier.padding(16.dp),
                                color = pageTitleColor
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Notes personnelles",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = pageTitleColor
                        )

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Ajoutez vos notes, impressions, modifications...",
                                    color = pageSubColor
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = fieldContainerColor,
                                unfocusedContainerColor = fieldContainerColor,
                                disabledContainerColor = fieldContainerColor,
                                focusedTextColor = pageTitleColor,
                                unfocusedTextColor = pageTitleColor,
                                cursorColor = BrandCoral,
                                focusedBorderColor = BrandCoral,
                                unfocusedBorderColor = fieldBorderColor,
                                focusedPlaceholderColor = pageSubColor,
                                unfocusedPlaceholderColor = pageSubColor
                            )
                        )

                        Button(
                            onClick = {
                                viewModel.saveNote(displayedCocktailId, note)
                                showNoteSavedMessage = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = saveNoteButtonColor,
                                contentColor = saveNoteTextColor
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text(
                                text = "Sauvegarder la note",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        AnimatedVisibility(
                            visible = showNoteSavedMessage,
                            enter = fadeIn(animationSpec = tween(220)),
                            exit = fadeOut(animationSpec = tween(220))
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = noteSavedBgColor,
                                shadowElevation = 0.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = noteSavedIconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Votre note personnelle a bien été sauvegardée.",
                                        color = noteSavedTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (suggestions.isNotEmpty()) {
                Text(
                    text = "✨ Tu pourrais aimer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (darkMode) Color(0xFFF5C86A) else pageTitleColor
                )

                suggestions.forEach { suggestion ->
                    SuggestionCard(
                        cocktail = suggestion,
                        darkMode = darkMode,
                        onClick = {
                            viewModel.getCocktailById(suggestion.idDrink)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(bottomPadding.calculateBottomPadding() + 96.dp))
        }

        ScrollToTopFab(
            visible = showScrollToTop,
            darkMode = darkMode,
            bottomOffset = bottomPadding.calculateBottomPadding() + 24.dp,
            onClick = {
                scope.launch {
                    scrollState.animateScrollTo(0)
                }
            }
        )
    }
}