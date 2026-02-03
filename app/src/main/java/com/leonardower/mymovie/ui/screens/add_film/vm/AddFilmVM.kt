package com.leonardower.mymovie.ui.screens.add_film.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

@OptIn(FlowPreview::class)
class AddFilmVM(
    private val filmManager: FilmManager,
    private val genreManager: GenreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFilmUiState())
    val uiState: StateFlow<AddFilmUiState> = _uiState.asStateFlow()

    // Flow для всех жанров
    private val allGenresFlow: StateFlow<List<Genre>> = genreManager
        .getSystemGenres() // TODO: добавить пользовательские жанры
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Загружаем все жанры при инициализации
        viewModelScope.launch {
            allGenresFlow.collect { genres ->
                _uiState.update { state ->
                    state.copy(allGenres = genres)
                }
            }
        }

        // Следим за изменениями в UI для валидации
        viewModelScope.launch {
            _uiState
                .debounce(300) // Дебаунс для предотвращения слишком частой валидации
                .collect { validateForm(it) }
        }
    }

    private var suggestionsJob: Job? = null
    private var validationJob: Job? = null


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
            delay(1000)

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

                isSuccess || isImage

            } catch (e: Exception) {
                false
            }
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }


    fun onGenreInputChange(input: String) {
        _uiState.update { state ->
            state.copy(
                genreInput = input,
                showGenreSuggestions = input.isNotEmpty()
            )
        }
        updateGenreSuggestions(input)
    }
    private fun updateGenreSuggestions(input: String) {
        if (input.isEmpty()) {
            _uiState.update { it.copy(genreSuggestions = emptyList()) }
            return
        }

        suggestionsJob?.cancel()

        suggestionsJob = viewModelScope.launch {
            delay(200)

            val currentState = _uiState.value
            val allGenres = currentState.allGenres

            val filtered = allGenres
                .filter { genre ->
                    genre.name.contains(input, ignoreCase = true) &&
                            !currentState.selectedGenres.contains(genre.name)
                }
                .map { it.name }
                .distinct()
                .take(5) // Ограничиваем количество подсказок

            _uiState.update {
                it.copy(
                    genreSuggestions = filtered,
                    showGenreSuggestions = filtered.isNotEmpty()
                )
            }
        }
    }
    fun onGenreSelect(genreName: String) {
        viewModelScope.launch {
            // Ищем жанр по имени
            val genre = genreManager.findGenreByName(genreName)

            if (genre != null) {
                _uiState.update { state ->
                    val updatedSelectedGenres = if (state.selectedGenreIds.contains(genre.id)) {
                        state.selectedGenreIds
                    } else {
                        state.selectedGenreIds + genre.id
                    }

                    val updatedSelectedGenreNames = if (state.selectedGenres.contains(genreName)) {
                        state.selectedGenres
                    } else {
                        state.selectedGenres + genreName
                    }

                    state.copy(
                        selectedGenreIds = updatedSelectedGenres,
                        selectedGenres = updatedSelectedGenreNames,
                        genreInput = "",
                        showGenreSuggestions = false
                    )
                }
            } else {
                // Если жанр не найден, создаем новый пользовательский
                val newGenreId = genreManager.createGenre(
                    name = genreName,
                    type = "user",
                    iconUrl = null
                )

                _uiState.update { state ->
                    state.copy(
                        selectedGenreIds = state.selectedGenreIds + newGenreId,
                        selectedGenres = state.selectedGenres + genreName,
                        genreInput = "",
                        showGenreSuggestions = false
                    )
                }
            }
        }
    }
    fun onRemoveGenre(genreName: String) {
        viewModelScope.launch {
            val genre = genreManager.findGenreByName(genreName)

            if (genre != null) {
                _uiState.update { state ->
                    state.copy(
                        selectedGenreIds = state.selectedGenreIds.filter { it != genre.id },
                        selectedGenres = state.selectedGenres.filter { it != genreName }
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        selectedGenres = state.selectedGenres.filter { it != genreName }
                    )
                }
            }
        }
    }

    fun rateFilm(rating: Int?) {
        _uiState.update { state ->
            val isRated = (rating != null && rating != 0)
            state.copy(
                rating = rating,
                isRated = isRated
            )
        }
    }
    fun onWatchLaterClick() {
        _uiState.update { state ->
            state.copy(
                isInWatchLater = !state.isInWatchLater
            )
        }
    }

    fun onSaveClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true) }

                val currentState = _uiState.value

                // Создаем фильм
                val film = Film(
                    title = currentState.title,
                    posterUrl = currentState.posterUrl,
                    description = currentState.description,
                    userRating = currentState.rating,
                    isWatchLater = currentState.isInWatchLater,
                    isViewed = !currentState.isInWatchLater,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                // Сохраняем фильм с выбранными жанрами
                filmManager.addFilm(
                    film = film,
                    genreIds = currentState.selectedGenreIds
                )

                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Ошибка сохранения фильма"
                    )
                }
            }
        }
    }


    private fun validateForm(state: AddFilmUiState) {
        val titleValid = state.title.isNotBlank()
        val posterValid = state.posterUrl.isNotBlank()
        val genresValid = state.selectedGenres.isNotEmpty()

        _uiState.update {
            it.copy(
                titleError = if (!titleValid) "Введите название" else null,
                posterError = if (!posterValid) "Введите ссылку на постер" else null,
                genresError = if (!genresValid) "Выберите хотя бы один жанр" else null,
                isFormValid = titleValid && posterValid && genresValid
            )
        }
    }


}

data class AddFilmUiState(
    val title: String = "",
    val posterUrl: String = "",
    val description: String = "",
    val rating: Int? = null,
    val isRated: Boolean = false,
    val isInWatchLater: Boolean = false,

    // Жанры
    val allGenres: List<Genre> = emptyList(),
    val selectedGenres: List<String> = emptyList(),
    val selectedGenreIds: List<Long> = emptyList(),
    val genreInput: String = "",
    val genreSuggestions: List<String> = emptyList(),
    val showGenreSuggestions: Boolean = false,

    // Валидация
    val titleError: String? = null,
    val posterError: String? = null,
    val genresError: String? = null,
    val isFormValid: Boolean = false,

    // Состояние
    val posterState: PosterState = PosterState.Empty,
    val posterValidationMessage: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

sealed class PosterState {
    data object Empty : PosterState()
    data object Loading : PosterState()
    data object Valid : PosterState()
    data object Error : PosterState()
}