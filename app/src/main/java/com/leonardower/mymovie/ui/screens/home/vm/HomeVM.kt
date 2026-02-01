package com.leonardower.mymovie.ui.screens.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeVM(
    private val filmRepository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Загружаем данные из репозиториев
                val watchLaterFilms = filmRepository.getWatchLaterFilms()
                val allGenres = genreRepository.getAllGenres()
                val filmsByGenre = mutableMapOf<Genre, List<Film>>()

                // Группируем фильмы по жанрам
                allGenres.forEach { genre ->
                    val films = filmRepository.getFilmsByGenre(genre.id)
                    if (films.isNotEmpty()) {
                        filmsByGenre[genre] = films
                    }
                }

                _uiState.value = HomeUiState(
                    watchLaterFilms = watchLaterFilms,
                    allGenres = allGenres,
                    filmsByGenre = filmsByGenre,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Обработка ошибок
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
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
    val watchLaterFilms: List<Film> = emptyList(),
    val allGenres: List<Genre> = emptyList(),
    val filmsByGenre: Map<Genre, List<Film>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)