package com.leonardower.mymovie.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.leonardower.mymovie.ui.screens.home.HomeScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onFilmClick = { filmId ->
                    // TODO: Позже добавим экран деталей фильма
                },
                onGenreClick = { genreId ->
                    navController.navigate(Screen.GenreDetail.createRoute(genreId))
                }
            )
        }

//        composable(Screen.Search.route) {
//            SearchScreen(
//                onGenreClick = { genreId ->
//                    navController.navigate(Screen.GenreDetail.createRoute(genreId))
//                },
//                onSearch = { query ->
//                    // Позже добавим поиск
//                }
//            )
//        }
//
//        composable(
//            route = Screen.GenreDetail.route,
//            arguments = Screen.GenreDetail.arguments
//        ) { backStackEntry ->
//            val genreId = backStackEntry.arguments?.getLong("genreId") ?: 0L
//            GenreDetailScreen(
//                genreId = genreId,
//                onBackClick = { navController.navigateUp() },
//                onFilmClick = { filmId ->
//                    // Позже добавим экран деталей фильма
//                }
//            )
//        }
    }
}

// Screen.kt
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object GenreDetail : Screen("genre/{genreId}") {
        fun createRoute(genreId: Long) = "genre/$genreId"
        val arguments = listOf(
            navArgument("genreId") { type = NavType.LongType }
        )
    }
}