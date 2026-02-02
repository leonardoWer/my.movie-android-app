package com.leonardower.mymovie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leonardower.mymovie.data.local.dao.FilmDao
import com.leonardower.mymovie.data.local.dao.GenreDao
import com.leonardower.mymovie.data.local.entities.Film
import com.leonardower.mymovie.data.local.entities.Genre
import com.leonardower.mymovie.data.local.entities.FilmGenreCrossRef

@Database(
    entities = [
        Film::class,
        Genre::class,
        FilmGenreCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FilmDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao
    abstract fun genreDao(): GenreDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getInstance(context: Context): FilmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmDatabase::class.java,
                    "film_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}