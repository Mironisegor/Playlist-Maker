package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.database.DatabaseMock
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs


class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val networkClient: NetworkClient,
) : TracksRepository {
    private val database = DatabaseMock.getInstance(scope)

    override suspend fun searchTracks(expression: String): List<Track> {
        if (expression.isBlank()) return emptyList()
        val response = networkClient.doRequest(TracksSearchRequest(expression.trim()))
        if (response !is TracksSearchResponse) return emptyList()
        return response.results.mapNotNull { dto -> dto.toDomainTrack() }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        val normalized = track.ensureId().copy(playlistId = playlistId)
        database.insertTrack(normalized)
    }

    override suspend fun deleteSongFromPlaylist(track: Track) {
        val normalized = track.ensureId().copy(playlistId = 0)
        database.insertTrack(normalized)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val normalized = track.ensureId().copy(favorite = isFavorite)
        database.insertTrack(normalized)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }
}

private fun TrackDto.toDomainTrack(): Track? {
    val name = trackName?.takeIf { it.isNotBlank() } ?: return null
    val artist = artistName?.takeIf { it.isNotBlank() } ?: return null
    val durationMillis = trackTimeMillis ?: 0L
    val minutes = durationMillis / 60000
    val seconds = (durationMillis % 60000) / 1000
    val time = String.format("%d:%02d", minutes, seconds)
    return Track(
        id = (trackId?.takeIf { it != 0L } ?: generateTrackId(name, artist)),
        playlistId = 0,
        favorite = false,
        trackName = name,
        artistName = artist,
        trackTime = time,
        artworkUrl = artworkUrl100
    )
}

private fun Track.ensureId(): Track {
    if (id != 0L) return this
    return copy(id = generateTrackId(trackName, artistName))
}

private fun generateTrackId(trackName: String, artistName: String): Long {
    val hash = (trackName + "_" + artistName).hashCode().toLong()
    return abs(hash)
}