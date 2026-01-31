package com.leonardower.mymovie.domain.repo

import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre

interface FilmRepository {
    fun getAllFilms(): List<Film>
    fun getWatchLaterFilms(): List<Film>
    fun getFilmsByGenre(genreId: Long): List<Film>
    fun getFilmById(id: Long): Film?
}

interface GenreRepository {
    fun getAllGenres(): List<Genre>
    fun getGenreById(id: Long): Genre?
}