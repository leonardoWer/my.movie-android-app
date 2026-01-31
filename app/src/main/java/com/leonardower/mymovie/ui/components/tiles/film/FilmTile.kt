package com.leonardower.mymovie.ui.components.tiles.film

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.ui.theme.DarkBg
import com.leonardower.mymovie.ui.theme.GrayButton

sealed class FilmTileSize(val width: Dp, val height: Dp) {
    object Large : FilmTileSize(180.dp, 300.dp)
    object Big : FilmTileSize(140.dp, 240.dp)
    object Medium : FilmTileSize(120.dp, 220.dp)
}

@Composable
fun FilmTile(
    film: Film,
    size: FilmTileSize,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(size.width)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(size.width)
                .height(size.height)
                .background(DarkBg)
        ) {
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // TODO: Добавить кастомное изображение как у кинопоиска
            film.rating?.let { rating ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(GrayButton)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = String.format("%.1f", rating),
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            // Название
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Жанры
            if (film.genres.isNotEmpty()) {
                Text(
                    text = film.genres.joinToString(", ") { it.name },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}