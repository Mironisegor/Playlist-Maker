package com.example.playlistmaker.data.network

import android.annotation.SuppressLint
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.database.entity.TrackEntity
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs


class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) : TracksRepository {
    private val trackDao = database.trackDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        if (expression.isBlank()) return emptyList()
        val response = networkClient.doRequest(TracksSearchRequest(expression.trim()))
        if (response !is TracksSearchResponse) return emptyList()
        return response.results.mapNotNull { dto -> dto.toDomainTrack() }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return trackDao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { it?.toTrack() }
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        val normalized = track.ensureId().copy(playlistId = playlistId)
        trackDao.insertTrack(normalized.toTrackEntity())
    }

    override suspend fun deleteSongFromPlaylist(track: Track) {
        val normalized = track.ensureId().copy(playlistId = 0)
        trackDao.insertTrack(normalized.toTrackEntity())
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val normalized = track.ensureId().copy(favorite = isFavorite)
        trackDao.insertTrack(normalized.toTrackEntity())
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks().map { entities ->
            entities.map { it.toTrack() }
        }
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        trackDao.clearPlaylistIdForFavorites(playlistId)
        trackDao.deleteNonFavoriteTracksByPlaylistId(playlistId)
    }
}

private fun TrackEntity.toTrack(): Track {
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

private fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        playlistId = playlistId,
        favorite = favorite,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl = artworkUrl
    )
}

@SuppressLint("DefaultLocale")
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