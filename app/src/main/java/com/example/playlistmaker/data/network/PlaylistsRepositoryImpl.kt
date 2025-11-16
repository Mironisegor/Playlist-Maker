package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.database.entity.PlaylistEntity
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: AppDatabase
) : PlaylistsRepository {
    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(
            playlistDao.getPlaylistById(playlistId),
            trackDao.getTracksByPlaylistId(playlistId)
        ) { playlistEntity, tracks ->
            playlistEntity?.let { playlist ->
                Playlist(
                    id = playlist.id,
                    name = playlist.name,
                    description = playlist.description,
                    tracks = tracks.map { it.toTrack() }
                )
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().flatMapLatest { playlists ->
            if (playlists.isEmpty()) {
                flow { emit(emptyList()) }
            } else {
                val flows = playlists.map { playlistEntity ->
                    trackDao.getTracksByPlaylistId(playlistEntity.id).map { tracks ->
                        Playlist(
                            id = playlistEntity.id,
                            name = playlistEntity.name,
                            description = playlistEntity.description,
                            tracks = tracks.map { it.toTrack() }
                        )
                    }
                }
                combine(flows) { arrays ->
                    arrays.map { it as Playlist }
                }
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        val playlistEntity = PlaylistEntity(
            name = name,
            description = description
        )
        playlistDao.insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylistById(id: Long) {
        // Удаление треков плейлиста выполняется через TracksRepository.deleteTracksByPlaylistId()
        // для правильной обработки избранных треков
        playlistDao.deletePlaylistById(id)
    }
}

private fun com.example.playlistmaker.database.entity.TrackEntity.toTrack(): Track {
    return Track(
        id = id,
        playlistId = playlistId,
        favorite = favorite,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl = artworkUrl
    )
}