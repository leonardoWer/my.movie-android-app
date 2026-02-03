package com.leonardower.mymovie.ui.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.ui.components.tiles.genre.GenreTile

@Composable
fun GenreList(
    allGenres: List<Genre>,
    onGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.genres),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        items(allGenres) { genre ->
            GenreTile(
                genre = genre,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGenreClick(genre.id) }
            )
        }
    }
}