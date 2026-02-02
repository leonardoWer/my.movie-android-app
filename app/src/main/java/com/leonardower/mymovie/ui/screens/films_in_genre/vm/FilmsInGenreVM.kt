package com.leonardower.mymovie.ui.screens.films_in_genre.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.helpers.FilmWithGenreNames
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FilmsInGenreVM(
    private val genreId: Long,
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilmsInGenreUiState())
    val uiState: StateFlow<FilmsInGenreUiState> = _uiState.asStateFlow()

    val films: StateFlow<List<FilmWithGenreNames>> = this
        .getFilmsByGenreWithGenreNames(genreId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadGenreData()
    }

    private fun loadGenreData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val genre = genreManager.getGenreById(genreId)

                _uiState.value = FilmsInGenreUiState(
                    genre = genre,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    private fun getFilmsByGenreWithGenreNames(genreId: Long): Flow<List<FilmWithGenreNames>> {
        return combine(
            filmManager.getFilmsByGenre(genreId),
            genreManager.getAllGenres()
        ) { films, allGenres ->
            films.map { film ->
                val genreIds = genreManager.getGenreIdsForFilm(film.id)
                val genreNames = genreIds.mapNotNull { id ->
                    allGenres.find { it.id == id }?.name
                }
                FilmWithGenreNames(film, genreNames)
            }
        }
    }
}

data class FilmsInGenreUiState(
    val genre: Genre? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)