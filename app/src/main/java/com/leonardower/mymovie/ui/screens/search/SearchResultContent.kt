package com.leonardower.mymovie.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.components.tiles.genre.GenreTile
import com.leonardower.mymovie.ui.screens.search.vm.SearchResult
import com.leonardower.mymovie.ui.screens.search.vm.SearchUiState

@Composable
fun SearchResultContent(
    uiState: SearchUiState,
    onFilmClick: (Long) -> Unit,
    onGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        uiState.searchQuery.isNotEmpty() -> {
            SearchResult(
                uiState = uiState,
                onFilmClick = onFilmClick,
                onGenreClick = onGenreClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun SearchResult(
    uiState: SearchUiState,
    onFilmClick: (Long) -> Unit,
    onGenreClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.searchResults.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.searchResults.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.search_results),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            items(uiState.searchResults) { item ->
                when (item) {

                    is SearchResult.FilmResult -> {
                        FilmTile(
                            film = item.film,
                            size = FilmTileSize.Large,
                            onClick = { onFilmClick(item.film.id) }
                        )
                    }

                    is SearchResult.GenreResult -> {
                        GenreTile(
                            genre = item.genre,
                            onClick = { onGenreClick(item.genre.id) }
                        )
                    }

                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ничего не найдено",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}