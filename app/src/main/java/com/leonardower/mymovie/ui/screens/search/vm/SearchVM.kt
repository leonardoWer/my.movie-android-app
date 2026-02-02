package com.leonardower.mymovie.ui.screens.search.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.common.nav.AppNavigation
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchVM(
    private val filmManager: FilmManager,
    private val genreManager: GenreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // Поток всех жанров
    val allGenres: StateFlow<List<Genre>> = genreManager
        .getAllGenres()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Поток для поискового запроса с дебаунсом
    private val searchQuery = MutableStateFlow("")

    init {
        // Настраиваем реактивный поиск
        setupSearch()
    }

    private fun setupSearch() {
        searchQuery
            .debounce(300) // Дебаунс 300ms
            .distinctUntilChanged()
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query

        // Обновляем UI состояние только если запрос изменился
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                // Поиск фильмов
                val films = filmManager.searchFilms(query)

                // Поиск жанров (фильтруем локально)
                val allGenres = genreManager.getAllGenres().first()
                val genres = allGenres.filter { genre ->
                    genre.name.contains(query, ignoreCase = true)
                }

                // Объединяем результаты
                val results = mutableListOf<SearchResult>()

                // Добавляем фильмы
                results.addAll(films.map { SearchResult.FilmResult(it) })

                // Добавляем жанры
                results.addAll(genres.map { SearchResult.GenreResult(it) })

                _uiState.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Не удалось выполнить запрос"
                    )
                }
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

sealed class SearchResult {
    data class FilmResult(val film: Film) : SearchResult()
    data class GenreResult(val genre: Genre) : SearchResult()
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)