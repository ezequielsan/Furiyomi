package com.example.furiyomi.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.furiyomi.R
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMore(viewModel: AuthViewModel, navHostController: NavHostController) {

    var userName by remember { mutableStateOf("") }
    var userPhotoUrl by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getUserName { name ->
            userName = name ?: "John Doe"
        }

        viewModel.getUserPhoto { photoUrl ->
            userPhotoUrl = photoUrl ?: ""
        }
        isVisible = true
    }

    TopAppBar(
        modifier = Modifier.padding(12.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = FuriyomiTheme.colorScheme.onSecondary,
            actionIconContentColor = FuriyomiTheme.colorScheme.onSecondary,
        ),
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = if (userPhotoUrl.isNullOrEmpty()) R.drawable.perfil_default else userPhotoUrl,
                    contentDescription = null,
                )

                Text(
                    text = "${userName}" + "",
                    style = FuriyomiTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.logout()
                    navHostController.navigate("login") // Volta para a tela de login
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Output,
                    contentDescription = "Botão de Sair",
                )
            }
        }
    )
}