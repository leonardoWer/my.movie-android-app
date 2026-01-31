package com.leonardower.mymovie.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.ui.components.list.FilmList
import com.leonardower.mymovie.ui.components.tiles.film.FilmTile
import com.leonardower.mymovie.ui.components.tiles.film.FilmTileSize
import com.leonardower.mymovie.ui.components.tiles.genre.GenreChip

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    watchLaterFilms: List<Film> = emptyList(),
    allGenres: List<Genre> = emptyList(),
    filmsByGenre: Map<Genre, List<Film>> = emptyMap(),
    onFilmClick: (Long) -> Unit = {},
    onGenreClick: (Long) -> Unit = {},
) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Секция "Буду смотреть"
        if (watchLaterFilms.isNotEmpty()) {
            FilmList(
                title = "Буду смотреть",
                modifier = modifier,
                content = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(watchLaterFilms) { film ->
                            FilmTile(
                                film = film,
                                size = FilmTileSize.Medium,
                                onClick = { onFilmClick(film.id) }
                            )
                        }
                    }
                }
            )
        }

        // Секция "Жанры"
        if (allGenres.isNotEmpty()) {
            FilmList(
                title = "Жанры",
                content = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(allGenres) { genre ->
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
        filmsByGenre.forEach { (genre, films) ->
            if (films.isNotEmpty()) {
                FilmList(
                    title = genre.name,
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(films) { film ->
                                FilmTile(
                                    film = film,
                                    size = FilmTileSize.Medium,
                                    onClick = { onFilmClick(film.id) }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}