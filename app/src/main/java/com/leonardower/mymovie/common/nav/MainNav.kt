package com.leonardower.mymovie.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.leonardower.mymovie.ui.screens.add_film.AddFilmScreen
import com.leonardower.mymovie.ui.screens.film_details.FilmDetailScreen
import com.leonardower.mymovie.ui.screens.films_in_genre.FilmsInGenreScreen
import com.leonardower.mymovie.ui.screens.home.HomeScreen
import com.leonardower.mymovie.ui.screens.search.SearchScreen

@Composable
fun MainNav(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(navController) {
        AppNavigation.manager.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }

        composable(Screen.Search.route) {
            SearchScreen()
        }

        composable(
            route = Screen.FilmsInGenre.route,
            arguments = Screen.FilmsInGenre.arguments
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getLong("genreId") ?: 0L
            FilmsInGenreScreen(
                genreId = genreId,
                onBackClick = { navController.navigateUp() },
            )
        }

        composable(
            route = Screen.FilmDetail.route,
            arguments = Screen.FilmDetail.arguments
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getLong("filmId") ?: 0L
            FilmDetailScreen(
                filmId = filmId,
                onBackClick = { AppNavigation.manager.navigateBack() }
            )
        }

        composable(Screen.AddFilm.route) {
            AddFilmScreen(
                onBackClick = { AppNavigation.manager.navigateBack() },
                onSaveSuccess = { AppNavigation.manager.navigateToHome() }
            )
        }
    }
}