package com.leonardower.mymovie.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.components.tiles.genre.GenreTile

@Composable
fun SearchResultContent(
    searchResults: List<Any>, // Могут быть фильмы и жанры
    onFilmClick: (Long) -> Unit,
    onGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.search_results),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(searchResults) { item ->
            when (item) {

                is Film -> {
                    FilmTile(
                        film = item,
                        size = FilmTileSize.Large,
                        onClick = { onFilmClick(item.id) }
                    )
                }

                is Genre -> {
                    GenreTile(
                        genre = item,
                        onClick = { onGenreClick(item.id) }
                    )
                }

            }
        }
    }
}