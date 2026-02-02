package com.leonardower.mymovie.ui.screens.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeVM(
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Поток фильмов "Буду смотреть"
    val watchLaterFilms: StateFlow<List<FilmWithGenreNames>> = this
        .getWatchLaterFilmsWithGenreNames()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Поток всех жанров
    val allGenres: StateFlow<List<Genre>> = genreManager
        .getAllGenres()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Фильмы по жанрам
    val filmsByGenre: StateFlow<Map<Genre, List<FilmWithGenreNames>>> = this
        .getFilmsGroupedByGenre()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Ждем загрузки хотя бы одного потока
                allGenres.first()
                watchLaterFilms.first()
                _uiState.update { it.copy(isLoading = false, isEmpty = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки данных: ${e.message}",
                        isEmpty = true
                    )
                }
            }
        }
    }

    private fun getFilmsGroupedByGenre(): Flow<Map<Genre, List<FilmWithGenreNames>>> {
        return filmManager.getFilmsGroupedByGenre()
            .map { filmsMap ->
                filmsMap.mapValues { (_, filmList) ->
                    getFilmsWithGenreNames(filmList)
                }
        }
    }

    // Получение фильмов с названиями жанров
    private suspend fun getFilmsWithGenreNames(films: List<Film>): List<FilmWithGenreNames> {
        return films.map { film ->
            val genreNames = genreManager.getGenreNamesForFilm(film.id)
            FilmWithGenreNames(film, genreNames)
        }
    }
    private fun getWatchLaterFilmsWithGenreNames(): Flow<List<FilmWithGenreNames>> {
        return filmManager.getWatchLaterFilms().map { films ->
            films.map { film ->
                val genreNames = genreManager.getGenreNamesForFilm(film.id)
                FilmWithGenreNames(film, genreNames)
            }
        }
    }

    fun onFilmClick(filmId: Long) {
        AppNavigation.manager.navigateToFilmDetail(filmId)
    }

    fun onGenreClick(genreId: Long) {
        AppNavigation.manager.navigateToFilmsInGenre(genreId)
    }
}

// Состояние экрана
data class HomeUiState(
    val none: Boolean = true,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)