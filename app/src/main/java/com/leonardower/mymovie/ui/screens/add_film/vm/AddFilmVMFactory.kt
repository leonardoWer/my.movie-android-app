package com.leonardower.mymovie.ui.screens.add_film.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leonardower.mymovie.App

object AddFilmViewModelFactory {
    val factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as App)
            AddFilmVM(
                filmManager = application.appModule.filmManager,
                genreManager = application.appModule.genreManager
            )
        }
    }
}