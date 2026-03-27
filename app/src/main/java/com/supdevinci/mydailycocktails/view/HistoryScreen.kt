package com.supdevinci.mydailycocktails.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supdevinci.mydailycocktails.ui.theme.BrandCoral
import com.supdevinci.mydailycocktails.ui.theme.BrandLime
import com.supdevinci.mydailycocktails.view.components.EmptyCard
import com.supdevinci.mydailycocktails.view.components.Header
import com.supdevinci.mydailycocktails.view.components.HistoryCard
import com.supdevinci.mydailycocktails.view.components.ScrollToTopFab
import com.supdevinci.mydailycocktails.viewmodel.CocktailViewModel
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    viewModel: CocktailViewModel,
    darkMode: Boolean,
    onToggleTheme: () -> Unit,
    bottomPadding: PaddingValues,
    onOpenDetail: (String) -> Unit
) {
    val history by viewModel.history.collectAsStateWithLifecycle()

    var selectionMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    var showClearAllDialog by remember { mutableStateOf(false) }
    var showDeleteSelectedDialog by remember { mutableStateOf(false) }

    val selectButtonBackground = if (darkMode) BrandCoral else BrandLime
    val selectButtonTextColor = Color(0xFF1C2433)

    val dangerButtonBackground = Color(0xFFE34B5F)
    val dangerButtonTextColor = Color.White

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2 || listState.firstVisibleItemScrollOffset > 500
        }
    }

    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = {
                Text("Supprimer tout l'historique ?")
            },
            text = {
                Text("Cette action supprimera définitivement tous les cocktails de l'historique.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        selectionMode = false
                        selectedIds = emptySet()
                        showClearAllDialog = false
                    }
                ) {
                    Text("Supprimer", color = dangerButtonBackground)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearAllDialog = false }
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    if (showDeleteSelectedDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteSelectedDialog = false },
            title = {
                Text("Supprimer la sélection ?")
            },
            text = {
                Text("Cette action supprimera ${selectedIds.size} cocktail(s) sélectionné(s) de l'historique.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeSelectedHistory(selectedIds)
                        selectedIds = emptySet()
                        selectionMode = false
                        showDeleteSelectedDialog = false
                    }
                ) {
                    Text("Supprimer", color = dangerButtonBackground)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteSelectedDialog = false }
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            contentPadding = PaddingValues(bottom = bottomPadding.calculateBottomPadding() + 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Header(
                    title = "Historique",
                    subtitle = "Les derniers cocktails que tu as consultés",
                    darkMode = darkMode,
                    onToggleTheme = onToggleTheme
                )
            }

            if (history.isNotEmpty()) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        HistoryActionButton(
                            text = if (selectionMode) "Annuler" else "Sélectionner",
                            background = selectButtonBackground,
                            textColor = selectButtonTextColor,
                            onClick = {
                                selectionMode = !selectionMode
                                if (!selectionMode) selectedIds = emptySet()
                            }
                        )

                        HistoryActionButton(
                            text = "Tout supprimer",
                            background = dangerButtonBackground,
                            textColor = dangerButtonTextColor,
                            onClick = {
                                showClearAllDialog = true
                            }
                        )

                        if (selectionMode && selectedIds.isNotEmpty()) {
                            HistoryActionButton(
                                text = "Supprimer (${selectedIds.size})",
                                background = dangerButtonBackground,
                                textColor = dangerButtonTextColor,
                                onClick = {
                                    showDeleteSelectedDialog = true
                                }
                            )
                        }
                    }
                }
            }

            if (history.isEmpty()) {
                item {
                    EmptyCard(
                        title = "Aucun historique",
                        subtitle = "Consulte un cocktail pour le retrouver ici.",
                        darkMode = darkMode
                    )
                }
            } else {
                items(history, key = { it.idDrink + itemKeySuffix(it.viewedAt) }) { item ->
                    HistoryCard(
                        item = item,
                        darkMode = darkMode,
                        isSelectionMode = selectionMode,
                        isSelected = selectedIds.contains(item.idDrink),
                        onSelectToggle = {
                            selectedIds = if (selectedIds.contains(item.idDrink)) {
                                selectedIds - item.idDrink
                            } else {
                                selectedIds + item.idDrink
                            }
                        },
                        onDelete = {
                            viewModel.removeHistoryItem(item.idDrink)
                            selectedIds = selectedIds - item.idDrink
                        },
                        onOpenDetail = { onOpenDetail(item.idDrink) }
                    )
                }
            }
        }

        ScrollToTopFab(
            visible = showScrollToTop,
            darkMode = darkMode,
            bottomOffset = bottomPadding.calculateBottomPadding() + 24.dp,
            onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        )
    }
}

private fun itemKeySuffix(viewedAt: Long): String = viewedAt.toString()

@Composable
private fun HistoryActionButton(
    text: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        color = background,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}