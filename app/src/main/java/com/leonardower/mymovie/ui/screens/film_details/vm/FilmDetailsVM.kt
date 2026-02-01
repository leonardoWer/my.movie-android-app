package com.leonardower.mymovie.ui.screens.film_details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.repo.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilmDetailVM(
    private val filmId: Long,
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilmDetailUiState())
    val uiState: StateFlow<FilmDetailUiState> = _uiState.asStateFlow()

    init {
        loadFilmDetails()
    }

    private fun loadFilmDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val film = filmRepository.getFilmById(filmId)

                _uiState.value = FilmDetailUiState(
                    film = film,
                    isLoading = false,
                    isRated = film?.rating != null,
                    rating = film?.rating?.toInt(),
                    isInWatchLater = film?.isWatchLater == true
                )
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
                rating = if (!state.isRated) 8 else null
            )
        }
    }

    fun onWatchLaterClick() {
        _uiState.update { state ->
            state.copy(isInWatchLater = !state.isInWatchLater)
        }
    }
}

data class FilmDetailUiState(
    val film: Film? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRated: Boolean = false,
    val rating: Int? = null,
    val isInWatchLater: Boolean = false
)