package com.supdevinci.mydailycocktails.view

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.supdevinci.mydailycocktails.R
import com.supdevinci.mydailycocktails.model.CocktailState
import com.supdevinci.mydailycocktails.navigation.Routes
import com.supdevinci.mydailycocktails.ui.theme.BrandCoral
import com.supdevinci.mydailycocktails.ui.theme.BrandLime
import com.supdevinci.mydailycocktails.ui.theme.BrandRed
import com.supdevinci.mydailycocktails.ui.theme.PeachEnd
import com.supdevinci.mydailycocktails.ui.theme.PurpleMid
import com.supdevinci.mydailycocktails.ui.theme.PurpleStart
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: CocktailViewModel
) {
    val context = LocalContext.current
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    var progress by remember { mutableStateOf(0f) }
    var minDurationDone by remember { mutableStateOf(false) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    LaunchedEffect(Unit) {
        repeat(100) { step ->
            progress = (step + 1) / 100f
            delay(35)
        }
        minDurationDone = true
    }

    LaunchedEffect(minDurationDone, homeState) {
        if (minDurationDone && homeState !is CocktailState.Loading) {
            navController.navigate(Routes.RANDOM) {
                popUpTo(Routes.SPLASH) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PurpleStart, PurpleMid, PeachEnd)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.raw.cocktail_splash)
                    .crossfade(false)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Splash cocktail",
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Daily Cocktails",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF13264C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "636 cocktail à découvrir 🍹",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF475467)
            )

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedGradientProgressBar(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Chargement : ${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF667085)
            )
        }
    }
}

@Composable
private fun AnimatedGradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(50.dp)

    val infiniteTransition = rememberInfiniteTransition(label = "progress_bar_shimmer")

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -160f,
        targetValue = 420f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress_bar_shimmer_offset"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = 0.55f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(shape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            BrandRed,
                            BrandCoral,
                            BrandLime
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.28f)
                    .offset(x = shimmerOffset.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}