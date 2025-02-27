package com.example.furiyomi

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.furiyomi.repository.AuthRepository
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.view.MainScreen
import com.example.furiyomi.ui.view.createNotificationChannel
import com.example.furiyomi.viewmodel.AuthViewModel
import com.example.furiyomi.viewmodel.AuthViewModelFactory
import com.example.furiyomi.viewmodel.MangaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val viewModel: MangaViewModel = viewModel()
            val currentContext = LocalContext.current

            createNotificationChannel(currentContext)

            // Solicitar permissão de notificação (se necessário)
            requestNotificationPermission(this)

            val repository = AuthRepository()
            val authViewModel = ViewModelProvider(
                this,
                AuthViewModelFactory(repository)
            ).get(AuthViewModel::class.java)

            FuriyomiTheme(isDarkTheme = isSystemInDarkTheme()) {
               MainScreen(navController, viewModel, authViewModel, context = currentContext)
            }
        }
    }
}

fun requestNotificationPermission(activity: android.app.Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
    }
}
