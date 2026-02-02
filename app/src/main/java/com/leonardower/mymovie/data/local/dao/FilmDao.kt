package com.leonardower.mymovie.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.FilmGenreCrossRef
import com.leonardower.mymovie.data.local.entities.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: Film): Long

    @Update
    suspend fun updateFilm(film: Film)

    @Delete
    suspend fun deleteFilm(film: Film)

    @Query("DELETE FROM films WHERE id = :filmId")
    suspend fun deleteFilmById(filmId: Long)


    // Получение фильмов
    @Query("SELECT * FROM films WHERE id = :filmId")
    suspend fun getFilmById(filmId: Long): Film?

    @Query("SELECT * FROM films ORDER BY created_at DESC")
    fun getAllFilms(): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE is_watch_later = 1 ORDER BY created_at DESC")
    fun getWatchLaterFilms(): Flow<List<Film>>

    @Query("SELECT * FROM films WHERE is_viewed = 1 ORDER BY view_date DESC")
    fun getViewedFilms(): Flow<List<Film>>


    // Поиск
    @Query("SELECT * FROM films WHERE title LIKE '%' || :query || '%' ORDER BY title")
    suspend fun searchFilms(query: String): List<Film>


    // Обновление полей
    @Query("UPDATE films SET is_watch_later = :isWatchLater WHERE id = :filmId")
    suspend fun updateWatchLaterStatus(filmId: Long, isWatchLater: Boolean)

    @Query("UPDATE films SET user_rating = :rating WHERE id = :filmId")
    suspend fun updateRating(filmId: Long, rating: Float?)

    @Query("UPDATE films SET is_viewed = :isViewed, view_date = :viewDate WHERE id = :filmId")
    suspend fun updateViewedStatus(filmId: Long, isViewed: Boolean, viewDate: Long)


    // Получение фильмов по жанру (через JOIN)
    @Transaction
    @Query("""
        SELECT films.* FROM films
        INNER JOIN film_genre_cross_ref ON films.id = film_genre_cross_ref.filmId
        WHERE film_genre_cross_ref.genreId = :genreId
        ORDER BY films.created_at DESC
    """)
    fun getFilmsByGenre(genreId: Long): Flow<List<Film>>

    // Получение всех фильмов с их жанрами
    @Transaction
    @Query("""
        SELECT films.* FROM films
        ORDER BY films.created_at DESC
    """)
    fun getAllFilmsWithGenres(): Flow<List<FilmWithGenres>>

    data class FilmWithGenres(
        @Embedded val film: Film,
        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = FilmGenreCrossRef::class,
                parentColumn = "filmId",
                entityColumn = "genreId"
            )
        )
        val genres: List<Genre>
    )
}