package com.leonardower.mymovie.ui.screens.add_film

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.common.GrayTextField
import com.leonardower.mymovie.ui.components.common.IconState
import com.leonardower.mymovie.ui.components.common.RatingButton
import com.leonardower.mymovie.ui.components.common.WatchLaterButton
import com.leonardower.mymovie.ui.components.img.ImgFromUrl
import com.leonardower.mymovie.ui.components.rating.RatingDialog
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmUiState
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmVM
import com.leonardower.mymovie.ui.screens.add_film.vm.AddFilmViewModelFactory
import com.leonardower.mymovie.ui.screens.add_film.vm.PosterState

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
                        style = MaterialTheme.typography.titleLarge
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
                )
            )
        },
        floatingActionButton = {
            Button(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = MaterialTheme.shapes.large,
                onClick = { viewModel.onSaveClick(onSaveSuccess) },
                enabled = uiState.isFormValid && !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.bodyMedium
                    )
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


@Composable
private fun AddFilmContent(
    uiState: AddFilmUiState,
    viewModel: AddFilmVM,
    modifier: Modifier = Modifier
) {
    var showRatingDialog by remember { mutableStateOf(false) }
    var showAddGenreDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                GrayTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    placeholder = stringResource(R.string.name),
                    singleLine = true,
                    isError = uiState.titleError != null && uiState.title.isNotEmpty(),
                    errorMessage = uiState.titleError
                )
                GrayTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = stringResource(R.string.description),
                    singleLine = false,
                    maxLines = 5,
                )
            }

            SelectGenreButton(uiState.selectedGenres) { showAddGenreDialog = true }
        }

        PosterUrlInput(uiState, viewModel)

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
                    onClick = { showRatingDialog = true },
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

    // Диалог выбора жанров
    if (showAddGenreDialog && uiState.allGenres.isNotEmpty()) {
        val selectedGenreObjects = uiState.allGenres
            .filter { uiState.selectedGenreIds.contains(it.id) }

        GenreSelectionBottomSheet(
            allGenres = uiState.allGenres,
            selectedGenres = selectedGenreObjects,
            onDismiss = { showAddGenreDialog = false },
            onConfirm = { selectedGenres ->
                viewModel.updateSelectedGenres(selectedGenres)
            }
        )
    }

    // Диалог рейтинга
    RatingDialog(
        isVisible = showRatingDialog,
        onDismiss = { showRatingDialog = false },
        onConfirm = { rating -> viewModel.rateFilm(rating) },
        filmTitle = uiState.title,
        filmPosterUrl = uiState.posterUrl,
        currentRatingInt = uiState.rating
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectGenreButton(
    selectedGenres: List<String>,
    onClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Выбранные жанры
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedGenres.forEach { genreName ->
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(vertical = 2.dp, horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = genreName,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(top = 4.dp, bottom = 8.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Добавить жанр",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.background,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "add genre",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
private fun PosterUrlInput(
    uiState: AddFilmUiState,
    viewModel: AddFilmVM
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GrayTextField(
            modifier = Modifier.weight(1f),
            value = uiState.posterUrl,
            onValueChange = viewModel::onPosterUrlChange,
            placeholder = stringResource(R.string.poster_url),
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
        )

        if (uiState.posterUrl.isNotEmpty()) {
            ImgFromUrl(
                imgUrl = uiState.posterUrl,
                modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(3f/4f)
            )
        }
    }
}