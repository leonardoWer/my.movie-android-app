package com.leonardower.mymovie.ui.components.tiles.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.ui.theme.GrayButtonColor

@Composable
fun GenreTile(
    genre: Genre,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    tileSize: Dp = 60.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(tileSize)
            .clickable {onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(tileSize)
                .width(tileSize)
                .background(GrayButtonColor)
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
private fun Preview() {
    GenreTile(genre = Genre(
        name = "Драма",
        type = "system",
        iconUrl = "https://res.cloudinary.com/demo/image/upload/w_400,h_400,c_fill/sample.jpg"
    ))
}