package com.leonardower.mymovie.ui.components.tiles.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.ui.theme.GrayBg

@Composable
fun GenreChip(
    genre: Genre,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(GrayBg)
            .padding(horizontal = 25.dp, vertical = 8.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreChipPreview() {
    GenreChip(
        genre = Genre(
            id = 1,
            name = "Драма",
        )
    )
}