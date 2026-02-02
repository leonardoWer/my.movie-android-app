package com.leonardower.mymovie.data.local.di

import android.content.Context
import androidx.startup.Initializer
import com.leonardower.mymovie.data.local.FilmDatabase
import com.leonardower.mymovie.data.local.dao.GenreDao
import com.leonardower.mymovie.data.local.entities.Genre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val database = FilmDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            initializeSystemGenres(database.genreDao())
        }
    }

    private suspend fun initializeSystemGenres(genreDao: GenreDao) {
        // Проверяем, есть ли уже системные жанры
        val existingGenres = genreDao.getSystemGenres()

        // Если нет системных жанров - создаем
        val systemGenres = listOf(
            Genre(
                name = "Драма",
                type = "system",
                iconUrl = "https://example.com/genres/drama.jpg",
            ),
            Genre(
                name = "Комедия",
                type = "system",
                iconUrl = "https://example.com/genres/comedy.jpg",
            ),
            Genre(
                name = "Триллер",
                type = "system",
                iconUrl = "https://example.com/genres/thriller.jpg",
            ),
            Genre(
                name = "Фантастика",
                type = "system",
                iconUrl = "https://example.com/genres/sci_fi.jpg",
            ),
            Genre(
                name = "Боевик",
                type = "system",
                iconUrl = "https://example.com/genres/action.jpg",
            ),
            Genre(
                name = "Аниме",
                type = "system",
                iconUrl = "https://example.com/genres/anime.jpg",
            ),
            Genre(
                name = "Мелодрама",
                type = "system",
                iconUrl = "https://example.com/genres/romance.jpg",
            ),
            Genre(
                name = "Ужасы",
                type = "system",
                iconUrl = "https://example.com/genres/horror.jpg",
            ),
            Genre(
                name = "Детектив",
                type = "system",
                iconUrl = "https://example.com/genres/detective.jpg",
            ),
            Genre(
                name = "Приключения",
                type = "system",
                iconUrl = "https://example.com/genres/adventure.jpg",
            )
        )

        genreDao.insertAll(systemGenres)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}