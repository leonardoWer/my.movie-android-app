package com.leonardower.mymovie.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "films",
    indices = [
        Index(value = ["title"], unique = false),
        Index(value = ["is_watch_later"]),
        Index(value = ["is_viewed"]),
        Index(value = ["created_at"])
    ]
)
data class Film(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "user_rating")
    val userRating: Float? = null,

    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int? = null,

    @ColumnInfo(name = "is_watch_later")
    val isWatchLater: Boolean = false,

    @ColumnInfo(name = "is_viewed")
    val isViewed: Boolean = false,

    @ColumnInfo(name = "view_date")
    val viewDate: Long? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long? = System.currentTimeMillis()
)