package com.leonardower.mymovie.ui.screens.add_film.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class AddFilmVM(
    private val filmRepository: FilmRepository,
    genreRepository: GenreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFilmUiState())
    val uiState: StateFlow<AddFilmUiState> = _uiState.asStateFlow()

    private val allGenres = genreRepository.getAllGenres().map { it.name }

    private var validationJob: Job? = null

    init {
        observeFormValidation()
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onPosterUrlChange(url: String) {
        // Обновляем состояние
        _uiState.update {
            it.copy(
                posterUrl = url,
                posterState = if (url.isEmpty()) PosterState.Empty else PosterState.Loading
            )
        }

        // Отменяем предыдущую задачу валидации
        validationJob?.cancel()

        if (url.isEmpty()) return

        // Запускаем новую задачу с debounce
        validationJob = viewModelScope.launch {
            delay(800)

            if (url != _uiState.value.posterUrl) return@launch

            // Проверяем URL
            val isValid = validateImageUrl(url)

            // Еще раз проверяем актуальность URL
            if (url != _uiState.value.posterUrl) return@launch

            _uiState.update { state ->
                state.copy(
                    posterState = if (isValid) PosterState.Valid else PosterState.Error,
                    posterValidationMessage = if (isValid) {
                        "Изображение загружено"
                    } else {
                        "Неверная ссылка на изображение"
                    }
                )
            }
        }
    }
    private suspend fun validateImageUrl(url: String): Boolean {
        if (url.isEmpty()) return false

        return withContext(Dispatchers.IO) {
            try {
                // Быстрая проверка по расширению
                val validExtensions = listOf(".jpg", ".jpeg", ".png", ".webp")
                if (validExtensions.any { url.lowercase().endsWith(it) }) {
                    return@withContext true
                }

                // Проверка URL
                val parsedUrl = java.net.URL(url)

                // HEAD запрос с таймаутами
                val connection = parsedUrl.openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 3000
                    readTimeout = 3000
                    requestMethod = "HEAD"
                    instanceFollowRedirects = true
                }

                connection.connect()

                // Проверяем ответ
                val isSuccess = connection.responseCode in 200..299
                val contentType = connection.contentType?.lowercase() ?: ""
                val isImage = contentType.startsWith("image/")

                connection.disconnect()

                isSuccess && isImage

            } catch (e: Exception) {
                false
            }
        }
    }

    fun onGenreInputChange(input: String) {
        _uiState.update { state ->
            val suggestions = if (input.isNotEmpty()) {
                allGenres.filter { it.lowercase().contains(input.lowercase()) }
            } else {
                emptyList()
            }

            state.copy(
                genreInput = input,
                genreSuggestions = suggestions.take(5),
                showGenreSuggestions = suggestions.isNotEmpty()
            )
        }
    }

    fun onGenreSelect(genre: String) {
        _uiState.update { state ->
            val updatedGenres = if (!state.selectedGenres.contains(genre)) {
                state.selectedGenres + genre
            } else {
                state.selectedGenres
            }

            state.copy(
                selectedGenres = updatedGenres,
                genreInput = "",
                genreSuggestions = emptyList(),
                showGenreSuggestions = false
            )
        }
    }

    fun onRemoveGenre(genre: String) {
        _uiState.update { state ->
            state.copy(selectedGenres = state.selectedGenres - genre)
        }
    }

    fun onRateClick() {
        // TODO: Открыть диалог оценки
        // Меняем состояние
        _uiState.update { state ->
            state.copy(
                isRated = !state.isRated,
                rating = if (!state.isRated) 8 else null // Пример оценки
            )
        }
    }

    fun onWatchLaterClick() {
        _uiState.update { state ->
            state.copy(isInWatchLater = !state.isInWatchLater)
        }
    }

    fun onSaveClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            try {
                // TODO: Реализовать сохранение в базу данных
                // filmRepository.addFilm(...)

                // Успешное сохранение
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Ошибка сохранения"
                    )
                }
            }
        }
    }

    private fun observeFormValidation() {
        combine(
            _uiState.map { it.title },
            _uiState.map { it.selectedGenres }
        ) { title, genres ->
            val titleError = if (title.isBlank()) "Введите название" else null
            val genresError = if (genres.isEmpty()) "Добавьте хотя бы один жанр" else null

            _uiState.update { state ->
                state.copy(
                    titleError = titleError,
                    genresError = genresError,
                    isFormValid = title.isNotBlank() && genres.isNotEmpty()
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        validationJob?.cancel()
    }
}

sealed class PosterState {
    data object Empty : PosterState()
    data object Loading : PosterState()
    data object Valid : PosterState()
    data object Error : PosterState()
}

data class AddFilmUiState(
    val title: String = "",
    val posterUrl: String = "",
    val posterState: PosterState = PosterState.Empty,
    val posterValidationMessage: String? = null,
    val genreInput: String = "",
    val selectedGenres: List<String> = emptyList(),
    val genreSuggestions: List<String> = emptyList(),
    val showGenreSuggestions: Boolean = false,
    val isRated: Boolean = false,
    val rating: Int? = null,
    val isInWatchLater: Boolean = false,
    val titleError: String? = null,
    val genresError: String? = null,
    val isFormValid: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)