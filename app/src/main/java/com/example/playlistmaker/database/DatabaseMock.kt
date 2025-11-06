package com.example.playlistmaker.database

import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.data.dto.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    fun getHistory(): List<String> {
        return historyList.toList()
    }

    fun addToHistory(word: String) {
        historyList.add(word)
        notifyHistoryChanged()
    }

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }


    fun getPlaylist(playlistId: Long): Flow<Playlist?> = flow {
        delay(200)
        val playlist = playlists.find { it.id == playlistId }
        val playlistTracks = tracks.filter { track ->
            track.playlistId == playlistId
        }
        emit(playlist?.copy(tracks = playlistTracks))
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val enriched = playlists.map { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            playlist.copy(tracks = playlistTracks)
        }
        emit(enriched)
    }

    fun addNewPlaylist(namePlaylist: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = namePlaylist,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(id: Long) {
        playlists.removeIf { it.id == id }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        val favorites = tracks.filter { it.favorite }
        emit(favorites)
    }
}