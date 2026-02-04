package com.leonardower.mymovie.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.ui.components.list.FilmList
import com.leonardower.mymovie.ui.components.state.AddFilmEmptyState
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.components.tiles.genre.GenreChip
import com.leonardower.mymovie.ui.screens.home.vm.HomeUiState
import com.leonardower.mymovie.ui.screens.home.vm.HomeVM
import com.leonardower.mymovie.ui.screens.home.vm.HomeViewModelFactory
import com.leonardower.mymovie.ui.theme.LightGray

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeVM = viewModel(
        factory = HomeViewModelFactory.factory
    )
) {
    val watchLaterFilms by viewModel.watchLaterFilms.collectAsState()
    val allGenres by viewModel.allGenres.collectAsState()
    val filmsByGenre by viewModel.filmsByGenre.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RectangleShape,
                onClick = { AppNavigation.manager.navigateToAddFilm() },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_film)
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            watchLaterFilms = watchLaterFilms,
            allGenres = allGenres,
            filmsByGenre = filmsByGenre,
            onFilmClick = viewModel::onFilmClick,
            onGenreClick = viewModel::onGenreClick
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    watchLaterFilms: List<FilmWithGenreNames>,
    allGenres: List<Genre>,
    filmsByGenre: Map<Genre, List<FilmWithGenreNames>>,
    onFilmClick: (Long) -> Unit = {},
    onGenreClick: (Long) -> Unit = {},

    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (uiState.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Вы не добавили ни одного фильма",
                    style = MaterialTheme.typography.titleMedium,
                    color = LightGray
                )
            }
        } else if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            // Секция "Буду смотреть"
            if (watchLaterFilms.isNotEmpty()) {
                FilmList(
                    title = "Буду смотреть",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(watchLaterFilms.size) { index ->
                                val it = watchLaterFilms[index]
                                FilmTile(
                                    film = it.film,
                                    filmGenreNames = it.genreNames,
                                    size = FilmTileSize.Big,
                                    onClick = { onFilmClick(it.film.id) }
                                )
                            }
                        }
                    }
                )
            }

            // Секция "Жанры"
            if (allGenres.isNotEmpty() && filmsByGenre.isNotEmpty()) {
                FilmList(
                    title = "Жанры",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(allGenres.size) { index ->
                                val genre = allGenres[index]
                                GenreChip(
                                    genre = genre,
                                    onClick = { onGenreClick(genre.id) }
                                )
                            }
                        }
                    }
                )
            }

            // Секции по жанрам
            if (filmsByGenre.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    AddFilmEmptyState(
                        onClick = { AppNavigation.manager.navigateToAddFilm() }
                    )
                }

            } else {
                filmsByGenre.forEach { (genre, films) ->
                    if (films.isNotEmpty()) {
                        FilmList(
                            title = genre.name,
                            content = {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(films.size) { index ->
                                        val it = films[index]
                                        FilmTile(
                                            film = it.film,
                                            filmGenreNames = it.genreNames,
                                            size = FilmTileSize.Medium,
                                            onClick = { onFilmClick(it.film.id) }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HomeScreen()
}