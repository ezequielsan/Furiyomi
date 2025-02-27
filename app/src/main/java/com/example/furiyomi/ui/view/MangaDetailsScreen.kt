package com.example.furiyomi.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.zIndex
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.furiyomi.model.Manga
import com.example.furiyomi.model.MangaRelationship
import com.example.furiyomi.ui.components.ExpandableText
import com.example.furiyomi.ui.theme.Black50
import com.example.furiyomi.ui.theme.Blue100
import com.example.furiyomi.ui.theme.Blue30
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White100
import com.example.furiyomi.ui.theme.White40
import com.example.furiyomi.viewmodel.AuthViewModel
import com.example.furiyomi.viewmodel.MangaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailsScreen(
    mangaId: String,
    navHostController: NavHostController,
    viewModel: MangaViewModel,
    authViewModel: AuthViewModel,
    context: Context,
    onTitleChange: (String) -> Unit // Callback para atualizar o título
) {
    // Cria o canal de notificação
    createNotificationChannel(context)

    val manga by viewModel.mangaById.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        viewModel.fetchMangaById(mangaId)
    }

    // Verifica se o mangá já está favoritado
    LaunchedEffect(userId, mangaId) {
        userId?.let {
            firestore.collection("users").document(it).get().addOnSuccessListener { document ->
                val favorites = document.get("favorites") as? List<String> ?: emptyList()
                isFavorite = favorites.contains(mangaId)
            }
        }
    }

    fun toggleFavorite(mangaTitle: String) {
        userId?.let {
            val userRef = firestore.collection("users").document(it)
            userRef.get().addOnSuccessListener { document ->
                val favorites = document.get("favorites") as? MutableList<String> ?: mutableListOf()
                if (favorites.contains(mangaId)) {
                    favorites.remove(mangaId)
                    isFavorite = false
                } else {
                    favorites.add(mangaId)
                    isFavorite = true
                }
                userRef.update("favorites", favorites)
            }
        }
    }

    if (manga.isNotEmpty()) {
        manga[0].let { mangaItem ->
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            val listScrollState = rememberLazyListState() // Controle do estado de rolagem
            val isScrolled = remember {
                derivedStateOf { listScrollState.firstVisibleItemIndex >= 2 }
            }
            var imageAlpha by remember { mutableStateOf(1f) }

            LaunchedEffect(mangaItem) {
                onTitleChange(mangaItem.attributes.getTitle())
            }

            LazyColumn(
                state = listScrollState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier.zIndex(999f)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 18.dp, horizontal = 18.dp)
                        ) {
                            val colorTint = if (isSystemInDarkTheme()) White40 else Black50

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CardMangaDetail(mangaItem)
                                Column(
                                    modifier = Modifier.padding(start = 24.dp)
                                ) {
                                    mangaItem.attributes.getTitle().let { it1 ->
                                        Text(
                                            text = it1,
                                            style = FuriyomiTheme.typography.titleNormal,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.padding(bottom = 5.dp),
                                            color = FuriyomiTheme.colorScheme.onBackground,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Person,
                                            contentDescription = "Autor Name",
                                            modifier = Modifier.size(18.dp),
                                            tint = colorTint
                                        )
                                        val authorName = (mangaItem.relationships.find { it is MangaRelationship.Author } as? MangaRelationship.Author)?.name

                                        Text(
                                            text = authorName ?: "autor desconhecido",
                                            style = FuriyomiTheme.typography.labelNormal,
                                            fontWeight = FontWeight.Bold,
                                            color = colorTint
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = (
                                                    if (mangaItem.attributes.status == "ongoing") {
                                                        Icons.Filled.AccessTime
                                                    } else if (mangaItem.attributes.status == "hiatus") {
                                                        Icons.Filled.Pause
                                                    } else if (mangaItem.attributes.status == "completed") {
                                                        Icons.Filled.Check
                                                    }else {
                                                        Icons.Filled.Cancel
                                                    }
                                            ),
                                            contentDescription = "Icone Status",
                                            modifier = Modifier.size(14.dp),
                                            tint = colorTint
                                        )
                                        Text(
                                            text = mangaItem.attributes.status.replaceFirstChar { it.uppercase() },
                                            modifier = Modifier.padding(start = 2.dp),
                                            color = colorTint
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp), // Ajuste de espaçamento externo
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    val favoriteSelectedTheme = if (isSystemInDarkTheme()) Blue30 else Blue100
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, // Substitua pelo ID do seu ícone
                                        contentDescription = "Heart Icon",
                                        tint = favoriteSelectedTheme, // Cor azul
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(bottom = 2.dp)
                                            .clickable {
                                                toggleFavorite(mangaItem.attributes.getTitle())

                                                if (!isFavorite) {
                                                    sendNotification(context, mangaItem.attributes.getTitle())
                                                }
                                            }
                                    )

                                    // Texto abaixo do ícone
                                    Text(
                                        text = "In library",
                                        color = favoriteSelectedTheme, // Cor azul
                                        style = FuriyomiTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold, // Peso da fonte
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .padding(8.dp), // Ajuste de espaçamento externo
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Public, // Substitua pelo ID do seu ícone
                                        contentDescription = "Heart Icon",
                                        tint = colorTint, // Cor azul
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(bottom = 2.dp)
                                            .clickable {  }
                                            .alpha(0.6f)
                                    )

                                    // Texto abaixo do ícone
                                    Text(
                                        text = "WebView",
                                        color = colorTint, // Cor azul
                                        style = FuriyomiTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold, // Peso da fonte
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.alpha(0.6f)
                                    )
                                }
                            }


                            ExpandableText(mangaItem.attributes.getDescription(), 3, FuriyomiTheme.typography.labelNormal)

                            // TagList(it.tags)

                            // Chapters list
                            Text(
                                text = 228.toString() + " chapters",
                                style = FuriyomiTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = FuriyomiTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp)
                            )
                        }
                    }
                }

                items(20) { chapter ->
                    val numberOfChapterInverted = 100 - chapter
                    ChapterItem(12, numberOfChapterInverted, mangaItem.attributes.createdAt)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    } else {
        // Mostra um loading enquanto os dados ainda não chegaram
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
           LoadingIndicator()
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


// Criar o canal de notificações
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "EVENT_CHANNEL"
        val name = "Notificações da Library"
        val descriptionText = "Canal para notificações de itens adicionados à Library"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        Log.d("NotificationDebug", "Canal de notificação criado: $channelId")
    }
}

// Enviar notificação
@SuppressLint("MissingPermission")
fun sendNotification(context: Context, mangaTitle: String) {
    Log.d("NotificationDebug", "Tentando enviar notificação para: $mangaTitle")

    val builder = NotificationCompat.Builder(context, "EVENT_CHANNEL")
        .setSmallIcon(android.R.drawable.ic_menu_info_details)
        .setContentTitle("Adicionado à Library!")
        .setContentText("O mangá '$mangaTitle' foi adicionado à sua Library.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Log.d("NotificationDebug", "Permissão para notificações não concedida")
            return
        }

        notify(mangaTitle.hashCode(), builder.build()) // ID único baseado no nome
        Log.d("NotificationDebug", "Notificação enviada para: $mangaTitle")
    }
}

@Composable
fun CardMangaDetail(
    manga: Manga
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
            .width(120.dp)
            .height(175.dp),
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

@Composable
fun TagChip(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(
                width = 0.7.dp,
                color = FuriyomiTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(6.dp)
            )
            .background(Color.Transparent, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = FuriyomiTheme.colorScheme.onSecondary,
            style = FuriyomiTheme.typography.labelSmall,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TagList(tags: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        items(tags) { tag ->
//            TagChip(tag)
            SuggestionChip(
                onClick = {},
                label = {
                    Text(
                        text = tag,
                        color = FuriyomiTheme.colorScheme.onSecondary,
                        style = FuriyomiTheme.typography.labelSmall,
                        fontSize = 12.sp
                    )
                }
            )
        }
    }
}

@Composable
fun ChapterItem(volumeNumber: Int, chapterNumber: Int, createdAt: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .clickable { }
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Vol." + volumeNumber + " Ch." + chapterNumber,
                    style = FuriyomiTheme.typography.labelLarge,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = createdAt,
                    style = FuriyomiTheme.typography.labelNormal,
                    fontSize = 12.sp,
                    color = FuriyomiTheme.colorScheme.onSecondary
                )
            }

            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.Outlined.FileDownload,
                contentDescription = "Download Button",
                tint = FuriyomiTheme.colorScheme.onSecondary
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
