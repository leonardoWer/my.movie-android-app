package com.leonardower.mymovie.common.nav

import androidx.navigation.NavController

class NavigationManager(
    private var navController: NavController? = null
) {

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigateToHome() {
        navController?.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    fun navigateToSearch() {
        navController?.navigate(Screen.Search.route) {
            popUpTo(Screen.Search.route) { inclusive = true }
        }
    }

    fun navigateToFilmsInGenre(genreId: Long) {
        navController?.navigate(Screen.FilmsInGenre.createRoute(genreId))
    }

    fun navigateToFilmDetail(filmId: Long) {
        // Позже добавим экран деталей фильма
        // navController?.navigate(Screen.FilmDetail.createRoute(filmId))
    }

    fun navigateToAddFilm() {
        navController?.navigate(Screen.AddFilm.route)
    }

    fun navigateBack() {
        navController?.navigateUp()
    }
}

// Объект для доступа из всего приложения
object AppNavigation {
    val manager = NavigationManager()
}