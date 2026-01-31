package com.leonardower.mymovie.domain.repo

import com.leonardower.mymovie.domain.model.Genre

class MockGenreRepository : GenreRepository {

    private val mockGenres = listOf(
        Genre(1, "Фантастика", iconUrl = "https://example.com/sci-fi.jpg"),
        Genre(2, "Триллер", iconUrl = "https://example.com/thriller.jpg"),
        Genre(3, "Драма", iconUrl = "https://example.com/drama.jpg"),
        Genre(4, "Комедия", iconUrl = "https://example.com/comedy.jpg"),
        Genre(5, "Аниме", iconUrl = "https://example.com/anime.jpg"),
        Genre(6, "Ужасы", iconUrl = "https://example.com/horror.jpg"),
        Genre(7, "Боевик", iconUrl = "https://example.com/action.jpg"),
        Genre(8, "Криминал", iconUrl = "https://example.com/crime.jpg"),
        Genre(9, "Мелодрама", iconUrl = "https://example.com/romance.jpg"),
        Genre(10, "Приключения", iconUrl = "https://example.com/adventure.jpg")
    )

    override fun getAllGenres(): List<Genre> {
        return mockGenres
    }

    override fun getGenreById(id: Long): Genre? {
        return mockGenres.find { it.id == id }
    }
}