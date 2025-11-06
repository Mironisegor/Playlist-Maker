package com.example.playlistmaker

import com.example.playlistmaker.creator.Storage
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Creator {
    fun getTracksRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(
            scope = scope,
            networkClient = RetrofitNetworkClient(Storage())
        )
    }

    fun getTracksRepository(): TracksRepository {
        return getTracksRepository(CoroutineScope(Dispatchers.IO))
    }
}