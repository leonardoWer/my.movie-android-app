package com.leonardower.mymovie.ui.screens.home.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leonardower.mymovie.App

object HomeViewModelFactory {
    val factory = viewModelFactory {
        initializer {
            val application = (
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as App
            )
            HomeVM(
                filmManager = application.appModule.filmManager,
                genreManager = application.appModule.genreManager
            )
        }
    }
}