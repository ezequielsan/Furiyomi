package com.example.furiyomi.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme

@Composable
fun ExpandableItem(
    title: String,
    items: List<Pair<String, Boolean>>,
    onItemCheckedChange: (String, Boolean) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(start = 12.dp, end = 8.dp, bottom = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expandedState = !expandedState
                }
        ) {
            Text(
                text = title,
                style = FuriyomiTheme.typography.labelNormal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = FuriyomiTheme.colorScheme.onBackground
            )
            IconButton(
                modifier = Modifier
                    .alpha(0.7f)
                    .rotate(rotationState),
                onClick = {
                    expandedState = !expandedState
                }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Drop-Down Arrow",
                    tint = FuriyomiTheme.colorScheme.onBackground
                )
            }
        }
        if (expandedState) {
            items.forEach { (text, isChecked) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            onItemCheckedChange(text, checked)
                        }
                    )
                    Text(
                        text = text.replaceFirstChar { it.uppercase() },
                        style = FuriyomiTheme.typography.labelNormal,
                        color = FuriyomiTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ExpandableCardPreview() {
    var items by remember {
        mutableStateOf(
            listOf(
                "Seguro" to false,
                "Sugestivo" to false,
                "Erótico" to false,
            )
        )
    }

    ExpandableItem(
        title = "Classificação de conteúdo",
        items = items,
        onItemCheckedChange = { text, isChecked ->
            items = items.map {
                if (it.first == text) it.copy(second = isChecked) else it
            }
        }
    )
}