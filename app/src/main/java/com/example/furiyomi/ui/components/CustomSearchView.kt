package com.example.furiyomi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.viewmodel.MangaViewModel

@Composable
fun CustomSearchView(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    viewModel: MangaViewModel
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .padding(vertical = 20.dp)
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {
        TextField(
            value = text,
            textStyle = FuriyomiTheme.typography.body,
            onValueChange = onTextChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = FuriyomiTheme.colorScheme.onSecondary,
                unfocusedTextColor = FuriyomiTheme.colorScheme.onSecondary
            ),
            placeholder = {
                Text(
                    "Pesquisar...",
                    style = FuriyomiTheme.typography.body,
                    color = FuriyomiTheme.colorScheme.onSecondary
                )
            },
            singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(text)
                }
            ),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                onTextChange("")
                            } else {
                                onCloseClick()
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Fechar",
                        tint = FuriyomiTheme.colorScheme.onSecondary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .focusRequester(focusRequester),
        )

        // Solicita o foco assim que o CustomSearchView for composto
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}