package com.leonardower.mymovie.ui.screens.search.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository

class SearchVMFactory(
    private val filmRepository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchVM::class.java)) {
            return SearchVM(filmRepository, genreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun provideSearchVMFactory(
    filmRepository: FilmRepository,
    genreRepository: GenreRepository
): ViewModelProvider.Factory {
    return SearchVMFactory(filmRepository, genreRepository)
}