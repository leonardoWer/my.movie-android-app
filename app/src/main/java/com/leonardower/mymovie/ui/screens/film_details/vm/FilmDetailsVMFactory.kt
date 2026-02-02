package com.leonardower.mymovie.ui.screens.film_details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leonardower.mymovie.App

object FilmDetailVMFactory {
    fun create(filmId: Long) = viewModelFactory {
        initializer {
            val application = (
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as App
                    )
            FilmDetailVM(
                filmId = filmId,
                filmManager = application.appModule.filmManager,
                genreManager = application.appModule.genreManager
            )
        }
    }
}