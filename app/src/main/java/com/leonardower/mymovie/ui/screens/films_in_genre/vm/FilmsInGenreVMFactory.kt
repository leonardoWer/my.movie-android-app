//package com.leonardower.mymovie.ui.screens.films_in_genre.vm
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.leonardower.mymovie.domain.repo.FilmRepository
//import com.leonardower.mymovie.domain.repo.GenreRepository
//import com.leonardower.mymovie.domain.repo.MockFilmRepository
//import com.leonardower.mymovie.domain.repo.MockGenreRepository
//
//class FilmsInGenreVMFactory(
//    private val genreId: Long,
//    private val filmRepository: FilmRepository,
//    private val genreRepository: GenreRepository
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FilmsInGenreVM::class.java)) {
//            return FilmsInGenreVM(genreId, filmRepository, genreRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//fun provideFilmsInGenreVMFactory(
//    genreId: Long,
//    filmRepository: FilmRepository = MockFilmRepository(),
//    genreRepository: GenreRepository = MockGenreRepository()
//): ViewModelProvider.Factory {
//    return FilmsInGenreVMFactory(genreId, filmRepository, genreRepository)
//}