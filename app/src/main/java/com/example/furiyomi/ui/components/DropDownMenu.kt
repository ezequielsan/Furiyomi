package com.example.furiyomi.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu() {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )

    val options = listOf(
        "None", "Title Ascending", "Title Descending", "Highest Rating",
        "Lowest Rating", "Most Follows", "Fewest Follows",
    )

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 8.dp, bottom = 6.dp)
    ) {

        OutlinedTextField(
            label = { Text("Ordenar por") },
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(0.7f)
                        .rotate(rotationState),
                    onClick = {
                        isExpanded = !isExpanded
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop-Down Arrow",
                        tint = FuriyomiTheme.colorScheme.onBackground
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedLabelColor = FuriyomiTheme.colorScheme.onBackground,
                focusedLabelColor = FuriyomiTheme.colorScheme.onBackground,
                focusedTextColor = FuriyomiTheme.colorScheme.onBackground,
                unfocusedTextColor = FuriyomiTheme.colorScheme.onBackground,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedBorderColor = FuriyomiTheme.colorScheme.onBackground,
                focusedBorderColor = FuriyomiTheme.colorScheme.onBackground
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(FuriyomiTheme.colorScheme.secondary).fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "None",
                        color = if ("None" == selectedOption)
                            FuriyomiTheme.colorScheme.primary else FuriyomiTheme.colorScheme.onBackground,
                        style = FuriyomiTheme.typography.labelNormal

                    )
                },
                onClick = {
                    selectedOption = "None"
                    isExpanded = false
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "Most Follows",
                        color = if ("Most Follows" == selectedOption)
                            FuriyomiTheme.colorScheme.primary else FuriyomiTheme.colorScheme.onBackground,
                        style = FuriyomiTheme.typography.labelNormal

                    )
                },
                onClick = {
                    selectedOption = "Most Follows"
                    isExpanded = false
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "Fewest Follows",
                        color = if ("Fewest Follows" == selectedOption)
                            FuriyomiTheme.colorScheme.primary else FuriyomiTheme.colorScheme.onBackground,
                        style = FuriyomiTheme.typography.labelNormal

                    )
                },
                onClick = {
                    selectedOption = "Fewest Follows"
                    isExpanded = false
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}