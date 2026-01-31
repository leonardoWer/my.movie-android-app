package com.leonardower.mymovie.ui.screens.home.vm

import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository

fun provideHomeVMFactory(
    filmRepository: FilmRepository,
    genreRepository: GenreRepository
): androidx.lifecycle.ViewModelProvider.Factory {
    return object : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return HomeVM(filmRepository, genreRepository) as T
        }
    }
}