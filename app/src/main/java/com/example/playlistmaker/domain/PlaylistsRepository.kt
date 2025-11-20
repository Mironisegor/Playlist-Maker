package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?)

    suspend fun deletePlaylistById(id: Long)

    suspend fun updatePlaylistCover(playlistId: Long, coverImageUri: String?)

    suspend fun mergePlaylists(sourcePlaylistId: Long, targetPlaylistId: Long)
}