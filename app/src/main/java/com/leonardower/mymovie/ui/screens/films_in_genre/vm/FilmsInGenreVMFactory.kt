package com.leonardower.mymovie.ui.screens.films_in_genre.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leonardower.mymovie.App

object FilmsInGenreVMFactory {
    fun create(genreId: Long) = viewModelFactory {
        initializer {
            val application = (
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as App
                    )
            FilmsInGenreVM(
                genreId = genreId,
                filmManager = application.appModule.filmManager,
                genreManager = application.appModule.genreManager
            )
        }
    }
}