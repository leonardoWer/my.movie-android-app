//package com.leonardower.mymovie.ui.screens.films_in_genre.vm
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.leonardower.mymovie.domain.model.Film
//import com.leonardower.mymovie.domain.model.Genre
//import com.leonardower.mymovie.domain.repo.FilmRepository
//import com.leonardower.mymovie.domain.repo.GenreRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class FilmsInGenreVM(
//    private val genreId: Long,
//    private val filmRepository: FilmRepository,
//    private val genreRepository: GenreRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(GenreDetailUiState())
//    val uiState: StateFlow<GenreDetailUiState> = _uiState.asStateFlow()
//
//    init {
//        loadGenreData()
//    }
//
//    private fun loadGenreData() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//
//            try {
//                val genre = genreRepository.getGenreById(genreId)
//                val films = filmRepository.getFilmsByGenre(genreId)
//
//                _uiState.value = GenreDetailUiState(
//                    genre = genre,
//                    films = films,
//                    isLoading = false
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    error = e.message ?: "Ошибка загрузки"
//                )
//            }
//        }
//    }
//}
//
//data class GenreDetailUiState(
//    val genre: Genre? = null,
//    val films: List<Film> = emptyList(),
//    val isLoading: Boolean = true,
//    val error: String? = null
//)