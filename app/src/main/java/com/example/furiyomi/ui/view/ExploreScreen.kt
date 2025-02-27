package com.example.furiyomi.ui.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.furiyomi.R
import com.example.furiyomi.model.Manga
import com.example.furiyomi.ui.components.CardManga
import com.example.furiyomi.ui.components.DropDownMenu
import com.example.furiyomi.ui.components.ExpandableItem
import com.example.furiyomi.ui.theme.Black60
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White60
import com.example.furiyomi.viewmodel.MangaViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: MangaViewModel,
    context: Context,
    navHostController: NavHostController,
    isBottomSheetVisible: Boolean, // Recebe o estado da BottomSheet de MainScreen
    onDismissBottomSheet: () -> Unit, // Recebe a função para fechar a BottomSheet
    searchQuery: String
) {
    val lazyPagingItems: LazyPagingItems<Manga> =
        viewModel.mangasPagedListFlow.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(false)

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var contentRatingOptions by remember {
        mutableStateOf(
            listOf(
                "safe" to false,
                "suggestive" to false,
                "erotica" to false
            )
        )
    }
    var publicationDemographic by remember {
        mutableStateOf(
            listOf(
                "shounen" to false,
                "shoujo" to false,
                "josei" to false,
                "seinen" to false
            )
        )
    }
    var publicationState by remember {
        mutableStateOf(
            listOf(
                "ongoing" to false,
                "completed" to false,
                "hiatus" to false,
                "cancelled" to false
            )
        )
    }

    var selectedContentRating = remember { mutableStateListOf<String>() }
    var selectedDemographic = remember { mutableStateListOf<String>() }
    var selectedStatus = remember { mutableStateListOf<String>() }

    // Variáveis para controle de texto de busca
    var localSearchQuery by remember { mutableStateOf(searchQuery) }
    var selectedOrder by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .padding(top = 18.dp, start = 18.dp, end = 18.dp)
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                lazyPagingItems.refresh()
            },
        ) {
            val screenWithDp = LocalConfiguration.current.screenWidthDp
            val height = LocalConfiguration.current.screenHeightDp
            val columns = max(1, (screenWithDp / 120))

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                state = rememberLazyGridState()
            ) {

                items(lazyPagingItems.itemCount) { index ->
                    val item = lazyPagingItems[index]
                    item?.let {
                        Box {
                            CardManga(it, navHostController)
                        }
                    }
                }
                val state = lazyPagingItems.loadState
                when {
                    state.refresh is LoadState.Loading ||
                            state.append is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                LoadingIndicator()
                            }
                        }
                    }
                    state.append is LoadState.Error ||
                            state.refresh is LoadState.Error -> {
                        item {
                            ErrorRetryIndicator(
                                onRefresh = {
                                    lazyPagingItems.refresh()
                                }
                            )
                        }
                    }
                }
            }
        }

        if (isBottomSheetVisible) {
            ModalBottomSheet (
                onDismissRequest = onDismissBottomSheet,
                sheetState = sheetState,
                containerColor = FuriyomiTheme.colorScheme.background,
                contentColor = FuriyomiTheme.colorScheme.onBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            modifier = Modifier.padding(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = FuriyomiTheme.colorScheme.primary,
                                containerColor = Color.Transparent
                            ),
                            onClick = {
                                contentRatingOptions = contentRatingOptions.map { it.copy(second = false) }
                                publicationDemographic = publicationDemographic.map { it.copy(second = false) }
                                publicationState = publicationState.map { it.copy(second = false) }

                                selectedContentRating.clear()
                                selectedStatus.clear()
                                selectedDemographic.clear()

                                // Atualize os filtros no ViewModel
                                viewModel.updateFilters(
                                    title = searchQuery,          // Mantém o título de busca
                                    demographic = emptyList(),    // Redefine para lista vazia
                                    contentRating = emptyList(),  // Redefine para lista vazia
                                    status = emptyList()          // Redefine para lista vazia
                                )

                                Log.d("Teste", selectedDemographic.toString())
                            }
                        ) {
                            Text(
                                "Redefinir",
                                style = FuriyomiTheme.typography.labelNormal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = FuriyomiTheme.colorScheme.background,
                                containerColor = FuriyomiTheme.colorScheme.primary
                            ),
                            onClick = {
                                viewModel.updateFilters(
                                    title = searchQuery,
                                    demographic = selectedDemographic,
                                    contentRating = selectedContentRating,
                                    status = selectedStatus
                                )
                            }
                        ) {
                            Text("Filtrar")
                        }
                    }

                    Divider(
                        thickness = 1.dp,
                        color = if (isSystemInDarkTheme()) Black60 else White60
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        ExpandableItem(
                            title = "Classificação de conteúdo",
                            items = contentRatingOptions,
                            onItemCheckedChange = { text, isChecked ->
                                contentRatingOptions = contentRatingOptions.map {
                                    if (it.first == text) it.copy(second = isChecked) else it
                                }
                                if (isChecked) {
                                    selectedContentRating.add(text.lowercase())
                                } else {
                                    selectedContentRating.remove(text.lowercase())
                                }
                                Log.d("Log de demographic", selectedContentRating.toString())
                            }
                        )

                        ExpandableItem(
                            title = "Demografia da publicação",
                            items = publicationDemographic,
                            onItemCheckedChange = { text, isChecked ->
                                publicationDemographic = publicationDemographic.map {
                                    if (it.first == text) it.copy(second = isChecked) else it
                                }
                                if (isChecked) {
                                    selectedDemographic.add(text.lowercase())
                                } else {
                                    selectedDemographic.remove(text.lowercase())
                                }
                                Log.d("Log de demographic", selectedDemographic.toString())
                            }
                        )

                        ExpandableItem(
                            title = "Estado",
                            items = publicationState,
                            onItemCheckedChange = { text, isChecked ->
                                publicationState = publicationState.map {
                                    if (it.first == text) it.copy(second = isChecked) else it
                                }
                                if (isChecked) {
                                    selectedStatus.add(text.lowercase())
                                } else {
                                    selectedStatus.remove(text.lowercase())
                                }
                                Log.d("Log de status", selectedStatus.toString())
                            }
                        )

                        // DropDownMenu()
                    }
                }
            }
        }
    }

}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorRetryIndicator(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onRefresh
        ) {
            Text(text = "Retry")
        }
    }
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}
