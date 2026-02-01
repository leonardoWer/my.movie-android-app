package com.leonardower.mymovie.domain.model

data class Film(
    var id: Long,
    val title: String,
    val posterUrl: String,
    val genres: List<Genre>,
    val description: String? = null,
    val rating: Float? = null,
    val isWatchLater: Boolean = false,
    val isViewed: Boolean = false
)
