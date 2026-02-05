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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.ui.components.item.RatingItem
import com.leonardower.mymovie.ui.theme.GrayButtonColor

sealed class FilmTileSize(val width: Dp, val height: Dp) {
    data object Large : FilmTileSize(180.dp, 300.dp)
    data object Big : FilmTileSize(160.dp, 230.dp)
    data object Medium : FilmTileSize(140.dp, 200.dp)
}

@Composable
fun FilmTile(
    film: Film,
    size: FilmTileSize,
    modifier: Modifier = Modifier,
    filmGenreNames: List<String> = emptyList(),
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
                .background(GrayButtonColor)
        ) {
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            film.userRating?.let { rating ->
                Box(modifier.padding(4.dp)) {
                    RatingItem(rating)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            // Название
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Жанры
            if (filmGenreNames.isNotEmpty()) {
                Text(
                    text = filmGenreNames.joinToString(", ") { it },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}