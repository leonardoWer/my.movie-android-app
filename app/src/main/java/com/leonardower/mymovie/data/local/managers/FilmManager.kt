package com.leonardower.mymovie.data.local.managers

import com.leonardower.mymovie.data.local.dao.FilmDao
import com.leonardower.mymovie.data.local.dao.GenreDao
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.FilmGenreCrossRef
import com.leonardower.mymovie.data.local.entities.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FilmManager(
    private val filmDao: FilmDao,
    private val genreDao: GenreDao
) {
    // Получение потоков данных
    fun getAllFilms(): Flow<List<Film>> = filmDao.getAllFilms()
    fun getWatchLaterFilms(): Flow<List<Film>> = filmDao.getWatchLaterFilms()
    fun getViewedFilms(): Flow<List<Film>> = filmDao.getViewedFilms()

    // Получение одного фильма
    suspend fun getFilmById(filmId: Long): Film? {
        return filmDao.getFilmById(filmId)
    }

    // Добавление фильма
    suspend fun addFilm(film: Film, genreIds: List<Long>): Long {
        // Сохраняем фильм
        val filmId = filmDao.insertFilm(film)

        // Сохраняем связи с жанрами
        genreIds.forEach { genreId ->
            genreDao.insertFilmGenreCrossRef(FilmGenreCrossRef(filmId, genreId))
        }

        return filmId
    }

    // Обновление фильма
    suspend fun updateFilm(film: Film, genreIds: List<Long>) {
        // Обновляем фильм
        filmDao.updateFilm(film.copy(updatedAt = System.currentTimeMillis()))

        // Удаляем старые связи с жанрами
        genreDao.deleteAllGenresForFilm(film.id)

        // Добавляем новые связи
        genreIds.forEach { genreId ->
            genreDao.insertFilmGenreCrossRef(FilmGenreCrossRef(film.id, genreId))
        }
    }

    // Удаление фильма
    suspend fun deleteFilm(filmId: Long) {
        filmDao.deleteFilmById(filmId)
    }

    // Обновление статуса "Буду смотреть"
    suspend fun updateWatchLaterStatus(filmId: Long, isWatchLater: Boolean) {
        filmDao.updateWatchLaterStatus(filmId, isWatchLater)
    }

    // Обновление рейтинга
    suspend fun updateRating(filmId: Long, rating: Float?) {
        filmDao.updateRating(filmId, rating)
    }

    // Получение фильмов по жанру
    fun getFilmsByGenre(genreId: Long): Flow<List<Film>> {
        return filmDao.getFilmsByGenre(genreId)
    }

    fun getFilmsGroupedByGenre(): Flow<Map<Genre, List<Film>>> {
        return filmDao.getAllFilmsWithGenres()
            .map { filmWithGenresList ->
                val result = mutableMapOf<Genre, MutableList<Film>>()

                // Инициализируем мапу для каждого жанра
                val allGenres = genreDao.getAllGenres().first()
                allGenres.forEach { genre ->
                    result[genre] = mutableListOf()
                }

                // Добавляем фильмы в соответствующие жанры
                filmWithGenresList.forEach { filmWithGenres ->
                    val film = filmWithGenres.film
                    filmWithGenres.genres.forEach { genre ->
                        result[genre]?.add(film)
                    }
                }

                // Удаляем пустые жанры и возвращаем неизменяемую мапу
                result.filterValues { it.isNotEmpty() }
            }
    }

    // Поиск фильмов
    suspend fun searchFilms(query: String): List<Film> {
        return filmDao.searchFilms(query)
    }
}