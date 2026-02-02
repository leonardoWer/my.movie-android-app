package com.leonardower.mymovie.common.helpers

import com.leonardower.mymovie.data.local.entities.Film

data class FilmWithGenreNames(
    val film: Film,
    val genreNames: List<String>
)