package com.example.furiyomi.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.furiyomi.R
import com.example.furiyomi.ThemeOption
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoreScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    theme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FuriyomiTheme.colorScheme.background) // Cor de fundo da tela
            .padding(16.dp)
    ) {
        // Ícone centralizado
        Image(
            painter = painterResource(id = R.drawable.furiyomi_logo_png), // Substitua pelo seu ícone
            contentDescription = "Ícone do App",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp)
                .padding(bottom = 24.dp)
        )

        Divider(modifier = Modifier.height(1.dp), color = FuriyomiTheme.colorScheme.secondary)

        SettingToggle("Notification", "Enable ou disable notifications")
        SettingToggle("Animations", "Enable ou disable notifications")

        Divider(modifier = Modifier.height(1.dp), color = FuriyomiTheme.colorScheme.secondary)


        ThemeSelector(theme, onThemeChange)

        Divider(modifier = Modifier.height(1.dp), color = FuriyomiTheme.colorScheme.secondary)

        SettingItem("Settings")
        SettingItem("About")
        SettingItem("Help")

    }
}

@Composable
fun SettingToggle(title: String, subtitle: String) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp)
        }
        Switch(checked = isChecked, onCheckedChange = { isChecked = it })
    }
}

@Composable
fun SettingItem(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp)
    )
}

@Composable
fun ThemeSelector(theme: ThemeOption, onThemeChange: (ThemeOption) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Theme",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column (
            modifier = Modifier.fillMaxWidth(),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(
                    checked = theme == ThemeOption.LIGHT,
                    onCheckedChange = { if (it) onThemeChange(ThemeOption.LIGHT) }
                ) {
                    Icon(Icons.Filled.Brightness7, contentDescription = "Light Mode")
                }
                Text("Tema Claro")

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(
                    checked = theme == ThemeOption.DARK,
                    onCheckedChange = { if (it) onThemeChange(ThemeOption.DARK) }
                ) {
                    Icon(Icons.Filled.Brightness4, contentDescription = "Dark Mode")
                }
                Text("Tema Escuro")

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(
                    checked = theme == ThemeOption.AUTO,
                    onCheckedChange = { if (it) onThemeChange(ThemeOption.AUTO) }
                ) {
                    Icon(Icons.Filled.BrightnessMedium, contentDescription = "Auto Mode")
                }
                Text("Automático")

            }

        }
    }
}
