package com.example.furiyomi.ui.view

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.furiyomi.ThemeOption
import com.example.furiyomi.ui.components.BottomNavigationBar
import com.example.furiyomi.ui.components.DefaultTopAppBar
import com.example.furiyomi.ui.components.TopAppBarMore
import com.example.furiyomi.ui.components.TopBarDetails
import com.example.furiyomi.ui.components.TopBarExplore
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.AuthViewModel
import com.example.furiyomi.viewmodel.MangaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MangaViewModel,
    authViewModel: AuthViewModel,
    context: Context,
    themeOption: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit

) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute in listOf("library", "history", "explore", "more")
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var mangaTitle by rememberSaveable { mutableStateOf("Manga Details") } // Título dinâmico
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FuriyomiTheme.colorScheme.background,
        contentColor = FuriyomiTheme.colorScheme.onBackground,
        topBar = {
            when (currentRoute) {
                "explore" -> TopBarExplore(
                    viewModel,  searchQuery,
                    onFilterClick = { isBottomSheetVisible = true },
                    onSearchQueryChange = { newQuery -> searchQuery = newQuery },
                )
                "mangaDetails/{mangaId}" -> TopBarDetails(
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                    hasNavIcon = true,
                    title = mangaTitle
                )
                "library" -> DefaultTopAppBar(currentRoute, navController, viewModel)
//                "history" -> DefaultTopAppBar(currentRoute, navController, viewModel)
                "more" -> TopAppBarMore(authViewModel, navController)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(authViewModel, navController) }
            composable("register") { RegisterScreen(authViewModel, navController) }
            composable("forgotPassword") { ForgotPasswordScreen(authViewModel, navController) }
            composable("library") { LibraryScreen(viewModel, authViewModel, navController) }
//            composable("history") { HistoryScreen(navController) }
            composable("explore") { ExploreScreen(
                viewModel,
                LocalContext.current,
                navController,
                isBottomSheetVisible,
                onDismissBottomSheet = { isBottomSheetVisible = false },
                searchQuery = searchQuery)
            }
            composable("more") { MoreScreen(authViewModel, navController, themeOption, onThemeChange) }
            composable("mangaDetails/{mangaId}") { backStackEntry ->
                val mangaId = backStackEntry.arguments?.getString("mangaId")
                MangaDetailsScreen(mangaId!!, navController, viewModel, authViewModel, context) { newTitle ->
                    mangaTitle = newTitle
                }
            }
        }
    }
}
