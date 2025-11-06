package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.database.DatabaseMock
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope
) : PlaylistsRepository {
    private val database = DatabaseMock(
        scope = scope,
    )

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return database.getPlaylist(playlistId)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return database.getAllPlaylists()
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(
            namePlaylist = name,
            description = description
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id = id)
    }
}