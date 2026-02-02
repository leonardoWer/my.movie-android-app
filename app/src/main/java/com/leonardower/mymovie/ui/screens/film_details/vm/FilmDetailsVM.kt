package com.leonardower.mymovie.ui.screens.film_details.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

class FilmDetailVM(
    private val filmId: Long,
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilmDetailUiState())
    val uiState: StateFlow<FilmDetailUiState> = _uiState.asStateFlow()

    private val _filmFlow: StateFlow<Film?> = filmManager
        .getFilmById(filmId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _filmGenreNamesFlow: StateFlow<List<String>> = genreManager.getAllGenres()
        .map { allGenres ->
            val genreIds = runBlocking {
                genreManager.getGenreIdsForFilm(filmId)
            }
            genreIds.mapNotNull { id ->
                allGenres.find { it.id == id }?.name
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filmWithGenreNames: StateFlow<FilmWithGenreNames?> = combine(
        _filmFlow, _filmGenreNamesFlow
    ) { film, genres ->
        film?.let {
            FilmWithGenreNames(it, genres)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        loadFilmDetails()
    }

    private fun loadFilmDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Ждем загрузки фильма
                val film = _filmFlow
                    .filterNotNull()
                    .firstOrNull()

                if (film != null) {
                    _uiState.update {
                        it.copy(
                            film = film,
                            isLoading = false,
                            isRated = film.userRating != null,
                            rating = film.userRating,
                            isInWatchLater = film.isWatchLater
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки фильма"
                )
            }
        }
    }

    fun onRateClick() {
        // TODO: Открыть диалог оценки
        _uiState.update { state ->
            state.copy(
                isRated = !state.isRated,
                rating = if (!state.isRated) 8f else null
            )
        }
    }

    fun onWatchLaterClick() {
        viewModelScope.launch {
            val currentFilm = _filmFlow.value

            if (currentFilm != null) {
                val newWatchLaterStatus = !_uiState.value.isInWatchLater

                try {
                    filmManager.updateWatchLaterStatus(currentFilm.id, newWatchLaterStatus)

                    _uiState.update { state ->
                        state.copy(isInWatchLater = newWatchLaterStatus)
                    }
                } catch (e: Exception) {
                    _uiState.update { state ->
                        state.copy(error = "Не удалось добавить в Буду смотреть")
                    }
                }
            }
        }
    }
}

data class FilmDetailUiState(
    val film: Film? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRated: Boolean = false,
    val rating: Float? = null,
    val isInWatchLater: Boolean = false
)