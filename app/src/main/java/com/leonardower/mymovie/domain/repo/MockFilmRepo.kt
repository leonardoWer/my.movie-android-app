package com.leonardower.mymovie.domain.repo

import com.leonardower.mymovie.domain.model.Film
import com.leonardower.mymovie.domain.model.Genre

class MockFilmRepository : FilmRepository {

    private val mockFilms = listOf(
        Film(
            id = 1,
            title = "Начало",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg",
            genres = listOf(
                Genre(1, "Фантастика"),
                Genre(2, "Триллер"),
                Genre(7, "Боевик")
            ),
            year = 2010,
            rating = 8.8f,
            isWatchLater = true
        ),
        Film(
            id = 2,
            title = "Побег из Шоушенка",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_FMjpg_UX1000_.jpg",
            genres = listOf(Genre(3, "Драма")),
            year = 1994,
            rating = 9.3f,
            isWatchLater = true
        ),
        Film(
            id = 3,
            title = "Крестный отец",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_FMjpg_UX1000_.jpg",
            genres = listOf(
                Genre(3, "Драма"),
                Genre(8, "Криминал")
            ),
            year = 1972,
            rating = 9.2f
        ),
        Film(
            id = 4,
            title = "Темный рыцарь",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_FMjpg_UX1000_.jpg",
            genres = listOf(
                Genre(7, "Боевик"),
                Genre(2, "Триллер"),
                Genre(8, "Криминал")
            ),
            year = 2008,
            rating = 9.0f
        ),
        Film(
            id = 5,
            title = "Твой апрель",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BMDkyNTYyZGQtY2E5NS00MWJiLTk3ODYtYTQzNmQwZjQ2ZThiXkEyXkFqcGdeQXVyMTUzMTg2ODkz._V1_FMjpg_UX1000_.jpg",
            genres = listOf(
                Genre(3, "Драма"),
                Genre(9, "Мелодрама")
            ),
            year = 2024,
            rating = 8.5f
        ),
        Film(
            id = 6,
            title = "Наруто",
            posterUrl = "https://m.media-amazon.com/images/M/MV5BZmQ5NGFiNWEtMmMyMC00MDdiLTg4YjktOGY5Yzc2MDUxMTE1XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_FMjpg_UX1000_.jpg",
            genres = listOf(
                Genre(5, "Аниме"),
                Genre(7, "Боевик"),
                Genre(10, "Приключения")
            ),
            year = 2002,
            rating = 8.4f
        )
    )

    override fun getWatchLaterFilms(): List<Film> {
        return mockFilms.filter { it.isWatchLater }
    }

    override fun getFilmsByGenre(genreId: Long): List<Film> {
        return mockFilms.filter { film ->
            film.genres.any { it.id == genreId }
        }
    }

    override fun getAllFilms(): List<Film> {
        return mockFilms
    }

    override fun getFilmById(id: Long): Film? {
        return mockFilms.find { it.id == id }
    }
}