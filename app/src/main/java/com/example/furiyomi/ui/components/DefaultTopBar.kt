package com.example.furiyomi.ui.components

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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.MangaViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    currentRoute: String?,
    navController: NavController,
    viewModel: MangaViewModel,
) {
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }


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
                Text(currentRoute!!.replaceFirstChar { it.uppercase() })
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
                    onClick = {  }
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