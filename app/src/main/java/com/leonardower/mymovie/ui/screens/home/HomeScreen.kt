package com.leonardower.mymovie.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.domain.genre.GenreData
import com.leonardower.mymovie.ui.components.list.FilmList
import com.leonardower.mymovie.ui.components.state.AddFilmEmptyState
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.genre.GenreCard
import com.leonardower.mymovie.ui.screens.home.vm.HomeUiState
import com.leonardower.mymovie.ui.screens.home.vm.HomeVM
import com.leonardower.mymovie.ui.screens.home.vm.HomeViewModelFactory

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeVM = viewModel(
        factory = HomeViewModelFactory.factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                onClick = { AppNavigation.manager.navigateToAddFilm() },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_film)
                )
            }
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            watchLaterFilms = uiState.watchLaterFilms,
            allGenres = uiState.allGenres,
            filmsByGenre = uiState.filmsByGenre,
            onFilmClick = viewModel::onFilmClick,
            onGenreClick = viewModel::onGenreClick
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    watchLaterFilms: List<FilmWithGenreNames>?,
    allGenres: List<Genre>,
    filmsByGenre: Map<Genre, List<FilmWithGenreNames>>?,
    onFilmClick: (Long) -> Unit = {},
    onGenreClick: (Long) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.my_movie__logo),
                    contentDescription = "MyMovie logo",
                    modifier = Modifier.height((32.sp).value.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (uiState.error != null) {
            item {
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
            }
        } else if (uiState.isEmpty) {
            item {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    AddFilmEmptyState(
                        onClick = { AppNavigation.manager.navigateToAddFilm() }
                    )
                }
            }
        } else {
            item { Spacer(Modifier.height(8.dp)) }
            item {
                if (allGenres.isNotEmpty() && filmsByGenre?.isNotEmpty() == true) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        items(allGenres) { genre ->
                            GenreCard(
                                genreData = GenreData(
                                    genreName = genre.name,
                                    _previewImgFileNameList = listOf(
                                        "drama__1.png", "drama__2.png"
                                    )
                                ),
                                onClick = { onGenreClick(genre.id) }
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(12.dp)) }
            if (watchLaterFilms?.isNotEmpty() == true) {
                item {
                    FlowRow(
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        watchLaterFilms.forEach { filmWithGenres ->
                            FilmTile(
                                film = filmWithGenres.film,
                                filmGenreNames = filmWithGenres.genreNames,
                                modifier = Modifier.weight(1f).fillMaxWidth(0.5f),
                                onClick = { onFilmClick(filmWithGenres.film.id) }
                            )
                        }
                        if (watchLaterFilms.size % 2 != 0) Box(Modifier.weight(1f))
                    }
                }
            }

            // Секции по жанрам
            filmsByGenre?.forEach { (genre, films) ->
                if (films.isNotEmpty()) {
                    item {
                        FilmList(
                            title = genre.name,
                            content = {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(films.size) { index ->
                                        val it = films[index]
                                        FilmTile(
                                            film = it.film,
                                            filmGenreNames = it.genreNames,
                                            modifier = Modifier.width(180.dp),
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