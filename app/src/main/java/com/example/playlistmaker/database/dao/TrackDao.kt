package com.example.playlistmaker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.database.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE trackName = :trackName AND artistName = :artistName LIMIT 1")
    fun getTrackByNameAndArtist(trackName: String, artistName: String): Flow<TrackEntity?>

    @Query("SELECT * FROM tracks WHERE favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE playlistId = :playlistId")
    fun getTracksByPlaylistId(playlistId: Long): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Update
    suspend fun updateTrack(track: TrackEntity)

    @Query("UPDATE tracks SET playlistId = 0 WHERE playlistId = :playlistId AND favorite = 1")
    suspend fun clearPlaylistIdForFavorites(playlistId: Long)

    @Query("DELETE FROM tracks WHERE playlistId = :playlistId AND favorite = 0")
    suspend fun deleteNonFavoriteTracksByPlaylistId(playlistId: Long)

    @Query("DELETE FROM tracks WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
}

