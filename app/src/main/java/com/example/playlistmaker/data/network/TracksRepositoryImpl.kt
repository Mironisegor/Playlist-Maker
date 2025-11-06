package com.example.playlistmaker.data.network

import com.example.playlistmaker.creator.Storage
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.database.DatabaseMock
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val networkClient: RetrofitNetworkClient,
) : TracksRepository {
    private val database = DatabaseMock(
        scope = scope
    )

    override suspend fun searchTracks(expression: String): List<Track> {
        val response: TracksSearchResponse = networkClient.doRequest(TracksSearchRequest(expression))
        return response.results.map { dto -> dto.toDomainTrack() }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track.copy(playlistId = playlistId))
    }

    override suspend fun deleteSongFromPlaylist(track: Track) {
        database.insertTrack(track.copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }
}

private fun TrackDto.toDomainTrack(): Track {
    val minutes = trackTimeMillis / 60000
    val seconds = (trackTimeMillis % 60000) / 1000
    val time = String.format("%d:%02d", minutes, seconds)
    return Track(
        id = 0,
        playlistId = 0,
        favorite = false,
        trackName = trackName,
        artistName = artistName,
        trackTime = time
    )
}