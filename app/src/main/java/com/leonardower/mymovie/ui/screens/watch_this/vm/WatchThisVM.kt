package com.leonardower.mymovie.ui.screens.watch_this.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchThisVM(
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchThisUiState())
    val uiState: StateFlow<WatchThisUiState> = _uiState.asStateFlow()

    // Поток всех фильмов из базы данных
    private val allFilmsStream = filmManager.getAllFilms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            allFilmsStream.collect { films ->
                updateRandomFilms(films)
            }
        }
    }

     // Обновляет случайные фильмы: один фильм дня и список случайных фильмов
    private fun updateRandomFilms(films: List<Film>) {
        if (films.isEmpty()) {
            _uiState.update { it.copy(isLoading = false, filmsEmpty = true) }
            return
        }

        val filmOfTheDay = if (films.isNotEmpty()) {
            // Используем детерминированный подход: берем фильм дня по индексу,
            // основанному на дате (чтобы он менялся раз в день)
            val todayIndex = (System.currentTimeMillis() / (1000 * 60 * 60 * 24)).toInt() % films.size
            films[todayIndex]
        } else {
            null
        }

        val randomFilms = if (films.size > 1) {
            // Генерируем список случайных фильмов, исключая фильм дня
            val availableFilms = if (filmOfTheDay != null) {
                films.filter { it.id != filmOfTheDay.id }
            } else {
                films
            }

            // Берем до 10 случайных фильмов (или меньше, если их мало)
            val count = minOf(10, availableFilms.size)
            availableFilms.shuffled().take(count)
        } else {
            emptyList()
        }

        _uiState.update { currentState ->
            currentState.copy(
                filmOfTheDay = filmOfTheDay,
                randomFilms = randomFilms,
                isLoading = false,
                filmsEmpty = false
            )
        }
    }

    fun toggleWatchLaterStatus(filmId: Long) {
        viewModelScope.launch {
            try {
                // Находим текущий фильм, чтобы узнать его текущее состояние
                val currentFilm = _uiState.value.filmOfTheDay?.takeIf { it.id == filmId }
                    ?: _uiState.value.randomFilms.find { it.id == filmId }

                currentFilm?.let { film ->
                    val newWatchLaterStatus = !film.isWatchLater

                    // Обновляем в базе данных
                    filmManager.updateWatchLaterStatus(filmId, newWatchLaterStatus)

                    // Обновляем локальное состояние
                    _uiState.update { currentState ->
                        val updatedFilmOfTheDay = if (currentState.filmOfTheDay?.id == filmId) {
                            currentState.filmOfTheDay.copy(isWatchLater = newWatchLaterStatus)
                        } else {
                            currentState.filmOfTheDay
                        }

                        val updatedRandomFilms = currentState.randomFilms.map { film ->
                            if (film.id == filmId) {
                                film.copy(isWatchLater = newWatchLaterStatus)
                            } else {
                                film
                            }
                        }

                        currentState.copy(
                            filmOfTheDay = updatedFilmOfTheDay,
                            randomFilms = updatedRandomFilms
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // Обновляет список случайных фильмов вручную
    fun refreshRandomFilms() {
        viewModelScope.launch {
            val films = allFilmsStream.value
            if (films.isNotEmpty()) {
                updateRandomFilms(films)
            }
        }
    }

    fun onFilmClick(filmId: Long) {
        AppNavigation.manager.navigateToFilmDetail(filmId)
    }

}

data class WatchThisUiState(
    val filmOfTheDay: Film? = null,
    val randomFilms: List<Film> = emptyList(),

    // State
    val isLoading: Boolean = true,
    val filmOfTheDayEmpty: Boolean = false,
    val filmsEmpty: Boolean = false,
    val error: String? = null
)