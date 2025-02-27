package com.example.furiyomi.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.MangaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarExplore(
    viewModel: MangaViewModel,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(searchQuery) }

    // Atualizando o searchQuery da MainScreen sempre que searchText mudar
    LaunchedEffect(searchText) {
        onSearchQueryChange(searchText) // Atualiza o searchQuery da MainScreen
    }

    var searchButtonPosition by remember { mutableStateOf(IntOffset.Zero) }
    val searchWidth by animateDpAsState(if (isSearchActive) with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.toDp()
    } else 0.dp)
    val searchAlpha by animateFloatAsState(if (isSearchActive) 1f else 0f)

    AnimatedVisibility(
        visible = !isSearchActive,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = FuriyomiTheme.colorScheme.onSecondary,
                actionIconContentColor = FuriyomiTheme.colorScheme.onSecondary,
            ),
            title = {
                Text("Explore")
            },
            actions = {
                IconButton(
                    onClick = {
                        isSearchActive = true
                    },
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInRoot()
                        searchButtonPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Botão de Pesquisar",
                    )
                }

                IconButton(
                    onClick = {
                        onFilterClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Botão de Filtro",
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Botão de Mais Opções",
                    )
                }
            }
        )
    }

    AnimatedVisibility(
        visible = isSearchActive,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = FuriyomiTheme.colorScheme.onSecondary,
                actionIconContentColor = FuriyomiTheme.colorScheme.onSecondary,
            ),
            title = {
                CustomSearchView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(searchWidth)
                        .alpha(searchAlpha)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = 0
                            )
                        },
                    text = searchText,
                    onTextChange = { searchText = it },
                    onCloseClick = {
                        isSearchActive = false;
                        searchText = ""
                    },
                    onSearchClick = {
                        viewModel.updateFilters(title = searchText)
                    },
                    viewModel
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearchActive = false; searchText = "" }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = FuriyomiTheme.colorScheme.onSecondary
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { onFilterClick() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Botão de Filtro",
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Botão de Mais Opções",
                    )
                }
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, backgroundColor = 0xFFFFFF)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0x000000)
@Composable
private fun PreviewTopBar() {
   /* FuriyomiTheme {
        //TopBarExplore()
    }*/
}