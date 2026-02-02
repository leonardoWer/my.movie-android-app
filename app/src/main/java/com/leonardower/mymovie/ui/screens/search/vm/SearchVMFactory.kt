package com.leonardower.mymovie.ui.screens.search.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leonardower.mymovie.App

object SearchViewModelFactory {
    val factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as App)
            SearchVM(
                filmManager = application.appModule.filmManager,
                genreManager = application.appModule.genreManager
            )
        }
    }
}