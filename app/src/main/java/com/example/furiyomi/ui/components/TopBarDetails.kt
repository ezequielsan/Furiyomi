package com.example.furiyomi.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.furiyomi.ui.theme.Black60
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetails(scrollBehavior: TopAppBarScrollBehavior, hasNavIcon: Boolean, title: String?) {
    val interactionSource = remember { MutableInteractionSource() }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = if (isSystemInDarkTheme()) White80 else Black60,
            titleContentColor = if (isSystemInDarkTheme()) White80 else Black60,
            actionIconContentColor = if (isSystemInDarkTheme()) White80 else Black60,
            scrolledContainerColor = FuriyomiTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(
                onClick = {},
                interactionSource = interactionSource,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {}
                )
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Botão de voltar"
                )

            }
        },
        title = {
            if (hasNavIcon) {
                Text(
                    if (title.isNullOrBlank()) "" else title,
                    style = FuriyomiTheme.typography.titleNormal,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = FuriyomiTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = "Botão de Download chaptes",
                )
            }

            IconButton(
                onClick = {}
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
        },
        modifier = Modifier.zIndex(-999f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, backgroundColor = 0xFFFFFF)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0x000000)
@Composable
private fun PreviewTopBar() {
   /* FuriyomiTheme {
        // TopBarDetails()
    }*/
}