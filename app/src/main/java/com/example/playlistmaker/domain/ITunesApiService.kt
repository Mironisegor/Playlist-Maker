package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
        @Query("country") country: String = "ru",
        @Query("limit") limit: Int = 50
    ): TracksSearchResponse
}