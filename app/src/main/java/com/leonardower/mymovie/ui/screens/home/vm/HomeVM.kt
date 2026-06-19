package com.leonardower.mymovie.ui.screens.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeVM(
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Загружаем все данные параллельно
                val watchLaterFilmsDeferred = async { getWatchLaterFilmsWithGenreNames() }
                val allGenresDeferred = async { genreManager.getAllGenres().first() }
                val filmsByGenreDeferred = async { getFilmsGroupedByGenre() }

                val watchLaterFilms = watchLaterFilmsDeferred.await()
                val allGenres = allGenresDeferred.await()
                val filmsByGenre = filmsByGenreDeferred.await()

                val hasAnyFilms = watchLaterFilms.isNotEmpty() ||
                        filmsByGenre.values.any { it.isNotEmpty() }

                _uiState.update {
                    it.copy(
                        watchLaterFilms = watchLaterFilms,
                        allGenres = allGenres,
                        filmsByGenre = filmsByGenre,
                        isLoading = false,
                        isEmpty = !hasAnyFilms,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки: ${e.message}"
                    )
                }
            }
        }
    }

    private suspend fun getFilmsGroupedByGenre(): Map<Genre, List<FilmWithGenreNames>> {
        return try {
            val filmsMap = filmManager.getFilmsGroupedByGenre().first()
            filmsMap.mapValues { (_, filmList) ->
                getFilmsWithGenreNames(filmList)
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private suspend fun getFilmsWithGenreNames(films: List<Film>): List<FilmWithGenreNames> {
        return films.map { film ->
            val genreNames = genreManager.getGenreNamesForFilm(film.id)
            FilmWithGenreNames(film, genreNames)
        }
    }

    private suspend fun getWatchLaterFilmsWithGenreNames(): List<FilmWithGenreNames> {
        return try {
            val films = filmManager.getWatchLaterFilms().first()
            films.map { film ->
                val genreNames = genreManager.getGenreNamesForFilm(film.id)
                FilmWithGenreNames(film, genreNames)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun onFilmClick(filmId: Long) {
        AppNavigation.manager.navigateToFilmDetail(filmId)
    }

    fun onGenreClick(genreId: Long) {
        AppNavigation.manager.navigateToFilmsInGenre(genreId)
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