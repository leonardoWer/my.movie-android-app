package com.leonardower.mymovie.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        }
    }

    fun onFilmClick(filmId: Long) {
        // Обработка клика по фильму
        // Можно открыть детали фильма
    }

    fun onGenreClick(genreId: Long) {
        // Обработка клика по жанру
        // Можно открыть страницу жанра
    }
}

// Состояние экрана
data class HomeUiState(
    val watchLaterFilms: List<Film> = emptyList(),
    val allGenres: List<Genre> = emptyList(),
    val filmsByGenre: Map<Genre, List<Film>> = emptyMap(),
    val isLoading: Boolean = true
)