package com.example.playlistmaker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val playlistId: Long = 0,
    val favorite: Boolean = false,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl: String? = null
)

