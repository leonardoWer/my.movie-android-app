package com.leonardower.mymovie.ui.components.tiles.film

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.ui.components.img.ImgFromUrl
import com.leonardower.mymovie.ui.components.rating.RatingItem

@Composable
fun FilmTile(
    film: Film,
    modifier: Modifier = Modifier,
    filmGenreNames: List<String> = emptyList(),
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .aspectRatio(3f/4.5f)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        ) {
            ImgFromUrl(
                imgUrl = film.posterUrl,
                contentDescription = film.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            film.userRating?.let { rating ->
                Box(Modifier.padding(4.dp)) {
                    RatingItem(rating)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            // Название
            Text(
                text = film.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Жанры
            if (filmGenreNames.isNotEmpty()) {
                Text(
                    text = filmGenreNames.joinToString(", ") { it },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}