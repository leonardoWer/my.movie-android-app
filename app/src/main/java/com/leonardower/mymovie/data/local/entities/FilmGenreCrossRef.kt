package com.leonardower.mymovie.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "film_genre_cross_ref",
    primaryKeys = ["filmId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = Film::class,
            parentColumns = ["id"],
            childColumns = ["filmId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Genre::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["filmId"]),
        Index(value = ["genreId"])
    ]
)
data class FilmGenreCrossRef(
    val filmId: Long,
    val genreId: Long
)