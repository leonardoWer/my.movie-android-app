package com.leonardower.mymovie.ui.components.tiles.genre

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.ui.theme.GrayButton

@Composable
fun GenreTile(
    genre: Genre,
    modifier: Modifier = Modifier,
    cardSize: Dp = 50.dp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(cardSize),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .height(cardSize)
                .width(cardSize)
                .background(GrayButton)
        ) {
            AsyncImage(
                model = genre.iconUrl,
                contentDescription = genre.name + " preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = genre.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
private fun GenreTilePreview() {
    GenreTile(
        genre = Genre(
            id = 1,
            name = "Драма",
        )
    )
}