package com.leonardower.mymovie.data.local.di

import android.content.Context
import com.leonardower.mymovie.data.local.FilmDatabase
import com.leonardower.mymovie.data.local.managers.FilmManager
import com.leonardower.mymovie.data.local.managers.GenreManager

class AppModule(private val context: Context) {
    private val database: FilmDatabase by lazy {
        FilmDatabase.getInstance(context)
    }

    // Менеджеры
    val filmManager: FilmManager by lazy {
        FilmManager(
            filmDao = database.filmDao(),
            genreDao = database.genreDao()
        )
    }

    val genreManager: GenreManager by lazy {
        GenreManager(
            genreDao = database.genreDao()
        )
    }
}