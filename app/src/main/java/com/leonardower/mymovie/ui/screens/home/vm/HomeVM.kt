package com.leonardower.mymovie.ui.screens.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
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

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Получаем потоки данных
            val watchLaterFlow = filmManager.getWatchLaterFilms()
            val allGenresFlow = genreManager.getAllGenres()
            val filmsByGenreFlow = filmManager.getFilmsGroupedByGenre()

            // Комбинируем все потоки
            combine(
                watchLaterFlow,
                allGenresFlow,
                filmsByGenreFlow
            ) { watchLaterFilms, allGenres, filmsByGenre ->
                // Для каждого фильма получаем названия жанров
                val watchLaterWithGenres = watchLaterFilms.map { film ->
                    val genreNames = genreManager.getGenreNamesForFilm(film.id)
                    FilmWithGenreNames(film, genreNames)
                }

                val filmsByGenreWithNames = filmsByGenre.mapValues { (_, filmList) ->
                    filmList.map { film ->
                        val genreNames = genreManager.getGenreNamesForFilm(film.id)
                        FilmWithGenreNames(film, genreNames)
                    }
                }

                val hasAnyFilms = watchLaterWithGenres.isNotEmpty() ||
                        filmsByGenreWithNames.values.any { it.isNotEmpty() }

                HomeUiState(
                    watchLaterFilms = watchLaterWithGenres,
                    allGenres = allGenres,
                    filmsByGenre = filmsByGenreWithNames,
                    isLoading = false,
                    isEmpty = !hasAnyFilms,
                    error = null
                )
            }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Ошибка загрузки: ${error.message}"
                        )
                    }
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun onFilmClick(filmId: Long) {
        AppNavigation.manager.navigateToFilmDetail(filmId)
    }

    fun onGenreClick(genreId: Long) {
        AppNavigation.manager.navigateToFilmsInGenre(genreId)
    }

    fun refresh() {
        observeData()
    }
}

data class HomeUiState(
    val watchLaterFilms: List<FilmWithGenreNames> = emptyList(),
    val allGenres: List<Genre> = emptyList(),
    val filmsByGenre: Map<Genre, List<FilmWithGenreNames>> = emptyMap(),

    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)