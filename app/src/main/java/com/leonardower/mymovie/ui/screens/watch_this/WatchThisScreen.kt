package com.leonardower.mymovie.ui.screens.watch_this

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.ui.components.common.GrayButton
import com.leonardower.mymovie.ui.components.common.WatchLaterButton
import com.leonardower.mymovie.ui.components.item.RatingItem
import com.leonardower.mymovie.ui.components.list.FilmList
import com.leonardower.mymovie.ui.components.state.AddFilmEmptyState
import com.leonardower.mymovie.ui.components.tiles.film.FilmDetail
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.screens.watch_this.vm.ManagersVMFactory
import com.leonardower.mymovie.ui.screens.watch_this.vm.WatchThisUiState
import com.leonardower.mymovie.ui.screens.watch_this.vm.WatchThisVM
import com.leonardower.mymovie.ui.theme.GrayButtonColor
import com.leonardower.mymovie.ui.theme.LightGray
import com.leonardower.mymovie.ui.theme.MyMovieTheme
import com.leonardower.mymovie.ui.theme.OrangePrimary

@Composable
fun WatchThisScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: WatchThisVM = viewModel(
        factory = ManagersVMFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // TODO: Можно показать Snackbar или Toast
            Log.e("WatchThisScreen", "Error: $error")
        }
    }

    WatchThisScreenContent(
        modifier = modifier,
        uiState = uiState,
        viewModel = viewModel
    )
}

@Composable
private fun WatchThisScreenContent(
    modifier: Modifier,
    uiState: WatchThisUiState,
    viewModel: WatchThisVM
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Что посмотреть",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Состояние загрузки
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (uiState.filmsEmpty) {
            item {
                AddFilmEmptyState(
                    { AppNavigation.manager.navigateToAddFilm() }
                )
            }
        } else {
            // Фильм дня
            uiState.filmOfTheDay?.let { film ->
                item {
                    FilmOfTheDaySection(
                        film = film,
                        onWatchLaterClick = { viewModel.toggleWatchLaterStatus(film.id) },
                        onFilmClick = { viewModel.onFilmClick(film.id) },
                    )
                }
            }

            if (uiState.randomFilms.isNotEmpty()) {
                item {
                    FilmList(
                        title = "Подборка случайных фильмов",
                        content = {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(uiState.randomFilms.size) { index ->
                                    val it = uiState.randomFilms[index]
                                    FilmTile(
                                        film = it,
                                        filmGenreNames = emptyList(),
                                        size = FilmTileSize.Big,
                                        onClick = { viewModel.onFilmClick(it.id) }
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

@Composable
private fun FilmOfTheDaySection(
    film: Film,
    onWatchLaterClick: () -> Unit,
    onFilmClick: () -> Unit,
) {
    FilmDetail(
        title = film.title,
        posterUrl = film.posterUrl
    ) {
        // Название фильма
        Text(
            text = film.title,
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Описание
        if (film.description?.isNotEmpty() == true) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = film.description,
                style = MaterialTheme.typography.bodyMedium,
                color = LightGray,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        film.userRating?.let {
            RatingItem(film.userRating)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GrayButton(
                text = "К фильму",
                onClick = onFilmClick,
                backgroundColor = OrangePrimary,
                contentPadding = PaddingValues(horizontal = 20.dp)
            )

            WatchLaterButton(
                text = "",
                onClick = onWatchLaterClick,
                backgroundColor = GrayButtonColor,
                isInWatchLater = film.isWatchLater
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    MyMovieTheme {
        WatchThisScreen()
    }
}