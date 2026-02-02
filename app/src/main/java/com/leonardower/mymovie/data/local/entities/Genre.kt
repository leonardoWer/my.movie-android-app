package com.leonardower.mymovie.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "genres",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["type"]),
        Index(value = ["user_id"])
    ]
)
data class Genre (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String, // "system" или "user"

    @ColumnInfo(name = "icon_url")
    val iconUrl: String? = null,

    @ColumnInfo(name = "user_id")
    val userId: Long? = null, // Для пользовательских жанров
)