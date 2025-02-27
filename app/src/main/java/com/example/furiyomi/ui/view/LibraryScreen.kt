package com.example.furiyomi.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import com.example.furiyomi.repository.AuthRepository
import com.example.furiyomi.ui.components.CardManga
import com.example.furiyomi.viewmodel.AuthViewModel
import com.example.furiyomi.viewmodel.MangaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.max


@Composable
fun LibraryScreen(
    viewModel: MangaViewModel,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
) {

    val favoriteMangaList by viewModel.mangasById.collectAsState()
    val favoriteIds = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()

    // Buscar favoritos do Firestore quando a tela inicia
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val favoritesRef = db.collection("users").document(it)

            favoritesRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("LibraryScreen", "Erro ao buscar favoritos", error)
                    return@addSnapshotListener
                }
                snapshot?.let { doc ->
                    val ids = doc.get("favorites") as? List<String> ?: emptyList()
                    favoriteIds.clear()
                    favoriteIds.addAll(ids)

                    if (favoriteIds.isNotEmpty()) {
                        viewModel.fetchMangasById(favoriteIds)
                    }
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .padding(top = 18.dp, start = 18.dp, end = 18.dp)
    ) {

        val screenWithDp = LocalConfiguration.current.screenWidthDp
        val columns = max(1, (screenWithDp / 120))

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            state = rememberLazyGridState()
        ) {

            items(favoriteMangaList) { manga ->
                manga?.let {
                    Box {
                        CardManga(it, navHostController)
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
