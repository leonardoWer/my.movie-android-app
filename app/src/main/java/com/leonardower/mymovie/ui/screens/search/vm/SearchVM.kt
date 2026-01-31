package com.leonardower.mymovie.ui.screens.search.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre
import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchVM(
    private val filmRepository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadAllGenres()
    }

    private fun loadAllGenres() {
        viewModelScope.launch {
            val genres = genreRepository.getAllGenres()
            _uiState.update { it.copy(allGenres = genres) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isNotEmpty()) {
            performSearch(query)
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            // Поиск по фильмам
            val films = filmRepository.getAllFilms()
            val filteredFilms = films.filter { film ->
                film.title.contains(query, ignoreCase = true)
            }

            // Поиск по жанрам
            val genres = genreRepository.getAllGenres()
            val filteredGenres = genres.filter { genre ->
                genre.name.contains(query, ignoreCase = true)
            }

            // Объединяем результаты
            val results = mutableListOf<Any>()
            results.addAll(filteredFilms)
            results.addAll(filteredGenres)

            _uiState.update { it.copy(searchResults = results) }
        }
    }

    fun onFilmClick(filmId: Long) {
        // Открыть детали фильма
    }

    fun onGenreClick(genreId: Long) {
        // Открыть страницу жанра
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val allGenres: List<Genre> = emptyList(),
    val searchResults: List<Any> = emptyList() // Могут быть фильмы или жанры
)