package com.leonardower.mymovie.data.local.managers

import com.leonardower.mymovie.data.local.dao.GenreDao
import com.leonardower.mymovie.data.local.entities.Genre
import kotlinx.coroutines.flow.Flow

class GenreManager(
    private val genreDao: GenreDao
) {
    // Получение жанров
    fun getAllGenres(): Flow<List<Genre>> = genreDao.getAllGenres()

    fun getSystemGenres(): Flow<List<Genre>> = genreDao.getSystemGenres()

    suspend fun getGenreById(genreId: Long): Genre? = genreDao.getGenreById(genreId)

    suspend fun findGenreByName(name: String): Genre? = genreDao.findGenreByName(name)

    // Получить айди жанров по фильму
    suspend fun getGenreIdsForFilm(filmId: Long): List<Long> {
        return genreDao.getGenreIdsForFilm(filmId)
    }

    // Получение названий жанров для фильма
    suspend fun getGenreNamesForFilm(filmId: Long): List<String> {
        return genreDao.getGenreIdsForFilm(filmId)
            .mapNotNull { genreId ->
                genreDao.getGenreById(genreId)?.name
            }
    }

    // Создание жанра (системного или пользовательского)
    suspend fun createGenre(name: String, type: String = "system", iconUrl: String? = null): Long {
        val genre = Genre(
            name = name,
            type = type,
            iconUrl = iconUrl,
        )
        return genreDao.insertGenre(genre)
    }
}