package com.example.furiyomi.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int,
    style: TextStyle
) {
    var isExpanded by remember { mutableStateOf(false) }
    var iconOffset = 32.dp

    Box {
        Column(modifier = Modifier
            .animateContentSize(animationSpec = tween(160))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isExpanded = !isExpanded })
        {

            if (isExpanded) {
                Text(text = text, style = style)
            } else {
                Text(text = text, maxLines = minimizedMaxLines, style = style)
            }
        }

        if (!isExpanded) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.8f to FuriyomiTheme.colorScheme.background.copy(0.8f),
                            1f to FuriyomiTheme.colorScheme.background,
                        )
                    )
                    .align(Alignment.BottomStart)
            )

            iconOffset = 0.dp
        }

        // Animação para o ângulo de rotação
        val rotationAngle by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f, // 180 graus para girar, 0 para voltar
            animationSpec = tween(durationMillis = 160),
            label = ""
        )

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "Show more Button",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = iconOffset)
                .graphicsLayer(
                    rotationZ = rotationAngle // Aplica a rotação no eixo Z
                ),
        )
    }

    // Adiciona um Spacer animado para empurrar o conteúdo abaixo
    Spacer(modifier = Modifier.height(iconOffset))
}