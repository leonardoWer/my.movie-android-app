package com.leonardower.mymovie.ui.screens.films_in_genre

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.screens.films_in_genre.vm.FilmsInGenreUiState
import com.leonardower.mymovie.ui.screens.films_in_genre.vm.FilmsInGenreVM
import com.leonardower.mymovie.ui.screens.films_in_genre.vm.FilmsInGenreVMFactory
import com.leonardower.mymovie.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsInGenreScreen(
    genreId: Long,
    onBackClick: () -> Unit,
    viewModel: FilmsInGenreVM = viewModel(
        factory = FilmsInGenreVMFactory.create(genreId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val films by viewModel.films.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.genre?.name ?: stringResource(R.string.genre),
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
    ) { paddingValues ->
        FilmsInGenreList(
            uiState,
            films = films,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun FilmsInGenreList(
    uiState: FilmsInGenreUiState,
    films: List<FilmWithGenreNames>,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        films.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_films_in_genre),
                    color = LightGray
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 колонки
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    films,
                    key = { it.film.id }
                ) {
                    FilmTile(
                        film = it.film,
                        size = FilmTileSize.Large,
                        onClick = {
                            AppNavigation.manager.navigateToFilmDetail(it.film.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ShowFilmsInGenrePreview() {
    FilmsInGenreScreen(2, { } )
}