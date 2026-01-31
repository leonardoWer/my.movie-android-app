package com.leonardower.mymovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.leonardower.mymovie.common.nav.BottomNavigationBar
import com.leonardower.mymovie.common.nav.MainNav
import com.leonardower.mymovie.domain.repo.MockFilmRepository
import com.leonardower.mymovie.domain.repo.MockGenreRepository
import com.leonardower.mymovie.ui.screens.home.HomeScreen
import com.leonardower.mymovie.ui.screens.home.vm.HomeVM
import com.leonardower.mymovie.ui.theme.MyMovieTheme
import com.leonardower.mymovie.ui.screens.home.vm.provideHomeVMFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyMovieApp()
                }
            }
        }
    }
}

@Composable
fun MyMovieApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        MainNav(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}