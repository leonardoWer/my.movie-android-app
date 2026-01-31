package com.leonardower.mymovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.leonardower.mymovie.common.nav.MainNavigation
import com.leonardower.mymovie.domain.repo.MockFilmRepository
import com.leonardower.mymovie.domain.repo.MockGenreRepository
import com.leonardower.mymovie.ui.screens.home.HomeScreen
import com.leonardower.mymovie.ui.screens.home.HomeVM
import com.leonardower.mymovie.ui.theme.MyMovieTheme
import com.leonardower.mymovie.ui.screens.home.provideHomeVMFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Nav
//                    val navController = rememberNavController()
//
//                    MainNavigation(navController = navController)

                    HomeScreenPage()
                }
            }
        }
    }
}

@Composable
fun HomeScreenPage() {
    // Создаем ViewModel с моковыми данными
    val viewModel: HomeVM = viewModel(
        factory = provideHomeVMFactory(
            MockFilmRepository(),
            MockGenreRepository()
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        watchLaterFilms = uiState.watchLaterFilms,
        allGenres = uiState.allGenres,
        filmsByGenre = uiState.filmsByGenre,
        onFilmClick = viewModel::onFilmClick,
        onGenreClick = viewModel::onGenreClick,
        modifier = Modifier.fillMaxSize()
    )
}