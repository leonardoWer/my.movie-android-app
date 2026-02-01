package com.leonardower.mymovie.ui.screens.add_film.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leonardower.mymovie.domain.repo.FilmRepository
import com.leonardower.mymovie.domain.repo.GenreRepository
import com.leonardower.mymovie.domain.repo.MockFilmRepository
import com.leonardower.mymovie.domain.repo.MockGenreRepository

class AddFilmVMFactory(
    private val filmRepository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFilmVM::class.java)) {
            return AddFilmVM(filmRepository, genreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun provideAddFilmVMFactory(
    filmRepository: FilmRepository = MockFilmRepository(),
    genreRepository: GenreRepository = MockGenreRepository()
): ViewModelProvider.Factory {
    return AddFilmVMFactory(filmRepository, genreRepository)
}