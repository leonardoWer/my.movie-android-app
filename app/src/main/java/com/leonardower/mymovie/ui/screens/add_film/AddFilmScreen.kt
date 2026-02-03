package com.leonardower.mymovie.ui.screens.add_film

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.common.GrayTextField
import com.leonardower.mymovie.ui.components.common.IconState
import com.leonardower.mymovie.ui.components.common.RatingButton
import com.leonardower.mymovie.ui.components.common.WatchLaterButton
import com.leonardower.mymovie.ui.components.dialog.RatingDialog
import com.leonardower.mymovie.ui.components.tiles.genre.GenreChip
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmUiState
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmVM
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmViewModelFactory
import com.leonardower.mymovie.ui.screens.add_film.vm.PosterState
import com.leonardower.mymovie.ui.theme.GrayBg
import com.leonardower.mymovie.ui.theme.GrayButton
import com.leonardower.mymovie.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFilmScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: AddFilmVM = viewModel(
        factory = AddFilmViewModelFactory.factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Добавить фильм",
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
        floatingActionButton = {
            // Кнопка сохранения
            Button(
                modifier = Modifier.width(180.dp),
                shape = RectangleShape,
                onClick = { viewModel.onSaveClick(onSaveSuccess) },
                enabled = uiState.isFormValid && !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.save))
                }
            }
        }
    ) { paddingValues ->
        AddFilmContent(
            uiState = uiState,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddFilmContent(
    uiState: AddFilmUiState,
    viewModel: AddFilmVM,
    modifier: Modifier = Modifier
) {
    var showRatingDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Название фильма
        GrayTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            placeholder = stringResource(R.string.name),
            singleLine = true,
            isError = uiState.titleError == null,
            errorMessage = uiState.titleError
        )

        // Поле ввода жанра с автодополнением
        GrayTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.genreInput,
            onValueChange = viewModel::onGenreInputChange,
            placeholder = stringResource(R.string.select_genre),
            singleLine = true
        )
        // Подсказки жанров
        if (uiState.genreSuggestions.isNotEmpty() && uiState.showGenreSuggestions) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(GrayBg)
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                ) {
                    uiState.genreSuggestions.forEach { suggestion ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onGenreSelect(suggestion) }
                                .padding(vertical = 12.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            text = suggestion,
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 0.8.dp,
                            color = GrayButton
                        )
                    }
                }
            }
        }
        // Выбранные жанры
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.selectedGenres.forEach { genreName ->
                Row(
                    modifier = Modifier
                        .clickable { viewModel.onRemoveGenre(genreName) }
                        .background(GrayBg)
                        .align(Alignment.CenterVertically),
                ) {
                    GenreChip(genreName)
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.remove_genre),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                }
            }
        }


        // Прикрепление картинки (Grid 1x3)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .heightIn(min = 42.dp, max = 70.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Поле для URL картинки (занимает 2 колонки)
            item(span = { GridItemSpan(2) }) {
                GrayTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.posterUrl,
                    onValueChange = viewModel::onPosterUrlChange,
                    placeholder = stringResource(R.string.poster_url),
                    leadingIcon = Icons.Default.Search,
                    leadingIconState = when (uiState.posterState) {
                        is PosterState.Loading -> IconState.Loading
                        is PosterState.Valid -> IconState.Success
                        is PosterState.Error -> IconState.Error
                        else -> IconState.None
                    },
                    trailingIcon = Icons.Default.Clear,
                    onTrailingIconClick = { viewModel.onPosterUrlChange("")},
                    isError = uiState.posterState == PosterState.Error,
                    errorMessage = uiState.posterValidationMessage,
                    showSuccessBorder = uiState.posterState == PosterState.Valid,
                )
            }

            // Кнопка выбора из галереи
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    contentPadding = PaddingValues(start = 8.dp),
                    shape = RectangleShape,
                    onClick = { /* TODO: Открыть галерею */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayButton,
                        contentColor = Color.White.copy(alpha = 0.9f),
                        disabledContainerColor = GrayButton,
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ico__file),
                            contentDescription = stringResource(R.string.file),
                            modifier = Modifier.size(20.dp),
                            tint = LightGray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.file),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Описание
        GrayTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            placeholder = stringResource(R.string.description),
            singleLine = false,
            maxLines = 5,
        )

        // Рейтинг и буду смотреть
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(42.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                RatingButton(
                    isRated = uiState.isRated,
                    rating = uiState.rating,
                    onClick = { showRatingDialog = true }
                )
            }
            item {
                WatchLaterButton(
                    isInWatchLater = uiState.isInWatchLater,
                    onClick = { viewModel.onWatchLaterClick() }
                )
            }
        }
    }

    // Диалог
    RatingDialog(
        isVisible = showRatingDialog,
        onDismiss = { showRatingDialog = false },
        onConfirm = { rating -> viewModel.rateFilm(rating) },
        filmTitle = uiState.title,
        filmPosterUrl = uiState.posterUrl,
        currentRatingInt = uiState.rating?.toInt()
    )
}

@Preview
@Composable
private fun AddFilmContentPreview() {
    AddFilmScreen({}, {})
}