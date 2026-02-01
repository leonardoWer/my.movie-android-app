package com.leonardower.mymovie.common.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object FilmsInGenre : Screen("genre/{genreId}") {
        fun createRoute(genreId: Long) = "genre/$genreId"
        val arguments = listOf(
            navArgument("genreId") { type = NavType.LongType }
        )
    }
    data object FilmDetail : Screen("film/{filmId}") {
        fun createRoute(filmId: Long) = "film/$filmId"
        val arguments = listOf(
            navArgument("filmId") { type = NavType.LongType }
        )
    }
    data object AddFilm : Screen("addFilm")
}