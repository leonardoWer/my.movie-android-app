//package com.leonardower.mymovie.ui.screens.film_details.vm
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.leonardower.mymovie.domain.repo.FilmRepository
//import com.leonardower.mymovie.domain.repo.MockFilmRepository
//
//class FilmDetailVMFactory(
//    private val filmId: Long,
//    private val filmRepository: FilmRepository
//) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FilmDetailVM::class.java)) {
//            return FilmDetailVM(filmId, filmRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//fun provideFilmDetailVMFactory(
//    filmId: Long,
//    filmRepository: FilmRepository = MockFilmRepository()
//): ViewModelProvider.Factory {
//    return FilmDetailVMFactory(filmId, filmRepository)
//}