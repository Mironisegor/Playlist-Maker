package com.example.playlistmaker.database

import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.data.dto.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    companion object {
        @Volatile
        private var INSTANCE: DatabaseMock? = null
        
        fun getInstance(scope: CoroutineScope): DatabaseMock {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseMock(scope).also { INSTANCE = it }
            }
        }
    }
    
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val _playlistsFlow = MutableStateFlow<List<Playlist>>(emptyList())
    private val _tracksFlow = MutableStateFlow<List<Track>>(emptyList())
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
        val playlist = playlists.find { it.id == playlistId }
        val playlistTracks = tracks.filter { track ->
            track.playlistId == playlistId
        }
        emit(playlist?.copy(tracks = playlistTracks))
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        updatePlaylistsFlow()
        return _playlistsFlow.asStateFlow()
    }
    
    private fun updatePlaylistsFlow() {
        val enriched = playlists.map { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            playlist.copy(tracks = playlistTracks)
        }
        _playlistsFlow.value = enriched.toList()
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
        updatePlaylistsFlow()
    }

    fun deletePlaylistById(id: Long) {
        playlists.removeIf { it.id == id }
        updatePlaylistsFlow()
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        _tracksFlow.map { it.find { existing ->
            existing.trackName == track.trackName && existing.artistName == track.artistName
        } }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
        _tracksFlow.value = tracks.toList()
        updatePlaylistsFlow()
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
        updatePlaylistsFlow()
        _tracksFlow.value = tracks.toList()
    }

    fun getFavoriteTracks(): Flow<List<Track>> =
        _tracksFlow.map { it.filter { track -> track.favorite } }
}