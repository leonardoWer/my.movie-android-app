package com.leonardower.mymovie.ui.screens.film_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.components.common.RatingButton
import com.leonardower.mymovie.ui.components.common.WatchLaterButton
import com.leonardower.mymovie.ui.components.dialog.RatingDialog
import com.leonardower.mymovie.ui.screens.film_details.vm.FilmDetailVM
import com.leonardower.mymovie.ui.screens.film_details.vm.FilmDetailVMFactory
import com.leonardower.mymovie.ui.theme.DarkBg
import com.leonardower.mymovie.ui.theme.GrayBg
import com.leonardower.mymovie.ui.theme.LightGray

@Composable
fun FilmDetailScreen(
    filmId: Long,
    onBackClick: () -> Unit,
    viewModel: FilmDetailVM = viewModel(
        factory = FilmDetailVMFactory.create(filmId)
    )
) {
    Scaffold(
        topBar = {
            FilmDetailTopAppBar(
                onBackClick = onBackClick
            )
        },
    ) { pv ->
        FilmDetailContent(viewModel)
        Modifier.padding(pv)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilmDetailTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = Modifier.background(Color.Transparent),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@Composable
private fun FilmDetailContent(
    viewModel: FilmDetailVM,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val filmWithGenreNames by viewModel.filmWithGenreNames.collectAsState()

    val film = filmWithGenreNames?.film

    var showRatingDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Постер с градиентным затемнением
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp * 0.7).dp)
                    .background(GrayBg)
            ) {
                // Постер фильма
                AsyncImage(
                    model = film?.posterUrl,
                    contentDescription = film?.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Градиентное затемнение снизу
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    DarkBg.copy(alpha = 0.3f),
                                    DarkBg.copy(alpha = 0.5f),
                                    DarkBg.copy(alpha = 0.8f),
                                    DarkBg.copy(alpha = 0.9f),
                                    DarkBg
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // Информация о фильме поверх постера
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Название фильма
                    Text(
                        text = film?.title ?: "Без названия",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Жанры
                    val genreNames = filmWithGenreNames?.genreNames ?: emptyList()
                    if (genreNames.isNotEmpty()) {
                        Text(
                            text = genreNames.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Рейтинг и буду смотреть
                    Row(
                        modifier = Modifier
                            .height(42.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RatingButton(
                            backgroundColor = Color.Transparent,
                            isRated = uiState.isRated,
                            rating = uiState.rating,
                            onClick = { showRatingDialog = true }
                        )

                        WatchLaterButton(
                            backgroundColor = Color.Transparent,
                            isInWatchLater = uiState.isInWatchLater,
                            onClick = { viewModel.onWatchLaterClick() }
                        )
                    }
                }
            }

            // Контент под постером
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {
                // Разделитель
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = GrayBg
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Описание фильма
                if (film?.description != null && film.description.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = film.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                }
            }
        }
    }

    RatingDialog(
        isVisible = showRatingDialog,
        onDismiss = { showRatingDialog = false },
        onConfirm = { rating -> viewModel.rateFilm(rating) },
        filmTitle = film?.title,
        filmPosterUrl = film?.posterUrl,
        currentRatingInt = uiState.rating
    )
}