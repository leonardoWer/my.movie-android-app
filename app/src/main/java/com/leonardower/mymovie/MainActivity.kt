package com.leonardower.mymovie

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.leonardower.mymovie.common.nav.bottom.BottomNavigationBar
import com.leonardower.mymovie.common.nav.MainNav
import com.leonardower.mymovie.data.local.di.AppModule
import com.leonardower.mymovie.ui.screens.splash.SplashScreen
import com.leonardower.mymovie.ui.theme.MyMovieTheme

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
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(
            onLoadingComplete = { showSplash = false }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding(),
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
        ) { paddingValues ->
            MainNav(
                navController = navController,
                modifier = Modifier
                    .padding(paddingValues)
                    .systemBarsPadding()
            )
        }
    }
}

class App : Application() {
    val appModule: AppModule = AppModule(this)
}