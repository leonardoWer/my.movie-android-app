package com.leonardower.mymovie.domain.model

data class Genre(
    val id: Long,
    val name: String,
    val iconUrl: String? = null, // Для версии с картинкой
)
