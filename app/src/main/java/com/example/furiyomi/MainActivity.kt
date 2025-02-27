package com.example.furiyomi

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Calendar

enum class ThemeOption {
    LIGHT, DARK, AUTO
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var theme by remember { mutableStateOf(ThemeOption.AUTO) }

            val darkTheme = when (theme) {
                ThemeOption.LIGHT -> false
                ThemeOption.DARK -> true
                ThemeOption.AUTO -> getCurrentTheme() // Usa o horário atual para definir o tema
            }

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

            FuriyomiTheme(
                colorScheme = if (darkTheme) com.example.furiyomi.ui.theme.darkColorScheme else com.example.furiyomi.ui.theme.lightColorScheme
            ) {
               MainScreen(
                   navController,
                   viewModel,
                   authViewModel,
                   context = currentContext,
                   theme,
                   onThemeChange = { theme = it }
               )
            }
        }
    }
}

fun requestNotificationPermission(activity: android.app.Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
    }
}

fun getCurrentTheme(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return currentHour in 18..23 || currentHour in 0..6 // Modo escuro entre 18h e 6h
}
