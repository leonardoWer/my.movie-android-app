package com.leonardower.mymovie.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.leonardower.mymovie.ui.screens.home.HomeScreen
import com.leonardower.mymovie.ui.screens.search.SearchScreen

@Composable
fun MainNav(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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