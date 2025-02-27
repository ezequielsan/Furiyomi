package com.example.furiyomi.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.furiyomi.ui.theme.Blue10
import com.example.furiyomi.ui.theme.Blue100
import com.example.furiyomi.ui.theme.FuriyomiTheme

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Library", "Explore", "More")
    val itemsLabel = listOf("library", "explore", "more")

    val selectedIcons = listOf(Icons.Filled.CollectionsBookmark, Icons.Filled.History,
        Icons.Filled.Explore, Icons.Filled.MoreHoriz)
    val unselectedIcons =
        listOf(Icons.Outlined.CollectionsBookmark, Icons.Outlined.History, Icons.Outlined.Explore,
            Icons.Outlined.MoreHoriz)

    NavigationBar(
        containerColor = FuriyomiTheme.colorScheme.secondary,
        contentColor = FuriyomiTheme.colorScheme.onSecondary
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item,
                        tint = FuriyomiTheme.colorScheme.onSecondary
                    )
                },
                label = {
                    Text(
                        text = item,
                        color = FuriyomiTheme.colorScheme.onSecondary,
                        fontWeight = if (selectedItem == index) FontWeight.SemiBold else FontWeight.Medium,
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navHostController.navigate(itemsLabel[index])

                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (isSystemInDarkTheme()) Blue100 else Blue10
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, backgroundColor = 0xFFFFFF)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewBottomNavigation() {
    /*FuriyomiTheme {
        // BottomNavigationBar()
    }*/
}