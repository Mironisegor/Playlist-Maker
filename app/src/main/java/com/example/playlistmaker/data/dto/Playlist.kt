package com.example.playlistmaker.data.dto

import com.example.playlistmaker.data.dto.Track

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)