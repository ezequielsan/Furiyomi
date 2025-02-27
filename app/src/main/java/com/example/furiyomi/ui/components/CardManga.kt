package com.example.furiyomi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.furiyomi.model.Manga
import com.example.furiyomi.model.MangaRelationship
import com.example.furiyomi.ui.theme.White100

@Composable
fun CardManga(
    manga: Manga,
    navHostController: NavHostController
) {

    // buscando no servidor a imagem do Mangá
    val coverURL = (
            manga.relationships.find {
                it is MangaRelationship.CoverArt
            } as? MangaRelationship.CoverArt)?.fileName?.let {
        "https://uploads.mangadex.org/covers/${manga.id}/$it"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .width(120.dp)
            .height(175.dp)
            .clickable {
                navHostController.navigate("mangaDetails/${manga.id}")
            },
        // elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val bottomFade =
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.8f to Color.Black.copy(0.5f),
                    1f to Color.Black.copy(0.8f),
                )

            Box() {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = "$coverURL.512.jpg",
                    contentDescription =  manga.attributes.getTitle(),
                    contentScale = ContentScale.FillBounds
                )

                Box(modifier = Modifier.height(220.dp).fillMaxWidth().background(bottomFade))
            }

            Text(
                text = manga.attributes.getTitle(),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,       // Cor da sombra
                        offset = Offset(4f, 4f), // Deslocamento da sombra (x, y)
                        blurRadius = 8f          // Raio de desfoque
                    ),
                    textAlign = TextAlign.Center
                ),
                color = White100,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 6.dp, end = 6.dp, bottom = 8.dp),
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }