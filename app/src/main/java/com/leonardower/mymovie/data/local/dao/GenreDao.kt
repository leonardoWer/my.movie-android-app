package com.leonardower.mymovie.data.local.dao

import androidx.room.*
import com.leonardower.mymovie.data.local.entities.FilmGenreCrossRef
import com.leonardower.mymovie.data.local.entities.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenre(genre: Genre): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(genres: List<Genre>)

    @Update
    suspend fun updateGenre(genre: Genre)

    @Delete
    suspend fun deleteGenre(genre: Genre)

    @Query("SELECT * FROM genres WHERE id = :genreId")
    suspend fun getGenreById(genreId: Long): Genre?

    @Query("SELECT * FROM genres WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findGenreByName(name: String): Genre?

    @Query("SELECT * FROM genres ORDER BY name")
    fun getAllGenres(): Flow<List<Genre>>

    @Query("SELECT * FROM genres WHERE type = 'system' ORDER BY name")
    fun getSystemGenres(): Flow<List<Genre>>

    @Query("SELECT * FROM genres WHERE type = 'user' ORDER BY name")
    fun getUserGenres(): Flow<List<Genre>>

    // Для связи многие-ко-многим
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilmGenreCrossRef(crossRef: FilmGenreCrossRef)

    @Query("DELETE FROM film_genre_cross_ref WHERE filmId = :filmId AND genreId = :genreId")
    suspend fun deleteFilmGenreCrossRef(filmId: Long, genreId: Long)

    @Query("DELETE FROM film_genre_cross_ref WHERE filmId = :filmId")
    suspend fun deleteAllGenresForFilm(filmId: Long)

    @Query("SELECT genreId FROM film_genre_cross_ref WHERE filmId = :filmId")
    suspend fun getGenreIdsForFilm(filmId: Long): List<Long>
}