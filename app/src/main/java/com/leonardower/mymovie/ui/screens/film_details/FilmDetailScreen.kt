//package com.leonardower.mymovie.ui.screens.film_details
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.AsyncImage
//import com.leonardower.mymovie.R
//import com.leonardower.mymovie.ui.components.common.RatingButton
//import com.leonardower.mymovie.ui.components.common.WatchLaterButton
//import com.leonardower.mymovie.ui.screens.film_details.vm.FilmDetailUiState
//import com.leonardower.mymovie.ui.screens.film_details.vm.FilmDetailVM
//import com.leonardower.mymovie.ui.screens.film_details.vm.provideFilmDetailVMFactory
//import com.leonardower.mymovie.ui.theme.DarkBg
//import com.leonardower.mymovie.ui.theme.GrayBg
//import com.leonardower.mymovie.ui.theme.LightGray
//
//@Composable
//fun FilmDetailScreen(
//    filmId: Long,
//    onBackClick: () -> Unit,
//    viewModel: FilmDetailVM = viewModel(factory = provideFilmDetailVMFactory(filmId))
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Scaffold(
//        topBar = {
//            FilmDetailTopAppBar(
//                onBackClick = onBackClick
//            )
//        },
//        contentWindowInsets = WindowInsets(0, 0, 0, 0)
//    ) { paddingValues ->
//        FilmDetailContent(
//            uiState = uiState,
//            onRateClick = viewModel::onRateClick,
//            onWatchLaterClick = viewModel::onWatchLaterClick,
//            modifier = Modifier.padding(paddingValues)
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun FilmDetailTopAppBar(
//    onBackClick: () -> Unit
//) {
//    TopAppBar(
//        title = { },
//        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = stringResource(R.string.go_back),
//                    tint = Color.White
//                )
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent,
//            scrolledContainerColor = Color.Transparent
//        ),
//        modifier = Modifier.background(Color.Transparent),
//        windowInsets = WindowInsets(0, 0, 0, 0)
//    )
//}
//
//@Composable
//private fun FilmDetailContent(
//    uiState: FilmDetailUiState,
//    onRateClick: () -> Unit,
//    onWatchLaterClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier.fillMaxSize()
//    ) {
//        val scrollState = rememberScrollState()
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//        ) {
//            // Постер с градиентным затемнением
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height((LocalConfiguration.current.screenHeightDp * 0.6).dp)
//                    .background(GrayBg)
//            ) {
//                // Постер фильма
//                AsyncImage(
//                    model = uiState.film?.posterUrl,
//                    contentDescription = uiState.film?.title,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//
//                // Градиентное затемнение снизу
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent,
//                                    DarkBg.copy(alpha = 0.3f),
//                                    DarkBg.copy(alpha = 0.6f),
//                                    DarkBg.copy(alpha = 0.9f),
//                                    DarkBg
//                                ),
//                                startY = 0f,
//                                endY = Float.POSITIVE_INFINITY
//                            )
//                        )
//                )
//
//                // Информация о фильме поверх постера
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 24.dp)
//                        .padding(bottom = 16.dp),
//                    verticalArrangement = Arrangement.Bottom,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Название фильма
//                    Text(
//                        text = uiState.film?.title ?: "",
//                        style = MaterialTheme.typography.displayLarge,
//                        color = Color.White,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Жанры
//                    if (uiState.film?.genres?.isNotEmpty() == true) {
//                        Text(
//                            text = uiState.film.genres.joinToString(", ") { it.name },
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = LightGray,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(32.dp))
//
//                    // Рейтинг и буду смотреть
//                    Row(
//                        modifier = Modifier
//                            .height(42.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        RatingButton(
//                            backgroundColor = Color.Transparent,
//                            isRated = uiState.isRated,
//                            rating = uiState.rating,
//                            onClick = { onRateClick() }
//                        )
//
//                        WatchLaterButton(
//                            backgroundColor = Color.Transparent,
//                            isInWatchLater = uiState.isInWatchLater,
//                            onClick = { onWatchLaterClick() }
//                        )
//                    }
//                }
//            }
//
//            // Контент под постером
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp)
//                    .padding(top = 8.dp, bottom = 24.dp)
//            ) {
//                // Разделитель
//                HorizontalDivider(
//                    modifier = Modifier.fillMaxWidth(),
//                    thickness = 1.dp,
//                    color = GrayBg
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                // Описание фильма
//                if (uiState.film?.description != null) {
//                    Text(
//                        text = stringResource(R.string.description),
//                        style = MaterialTheme.typography.titleMedium,
//                        color = Color.White,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    Text(
//                        text = uiState.film.description,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.White,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun Preview() {
//    FilmDetailScreen(1, {})
//}