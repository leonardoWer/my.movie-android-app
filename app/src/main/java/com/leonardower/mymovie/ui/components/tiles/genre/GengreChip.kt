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
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.ui.theme.GrayBg

@Composable
fun GenreChip(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(GrayBg)
            .padding(horizontal = 30.dp, vertical = 12.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Composable
fun GenreChip(
    genre: Genre,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    GenreChip(
        title = genre.name,
        modifier = modifier,
        onClick = onClick
    )
}