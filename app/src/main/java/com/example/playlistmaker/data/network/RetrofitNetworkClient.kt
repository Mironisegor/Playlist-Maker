package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.BaseResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.ITunesApiService
import com.example.playlistmaker.domain.NetworkClient
import retrofit2.HttpException
import java.io.IOException

class RetrofitNetworkClient(
    private val apiService: ITunesApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): BaseResponse {
        return when (dto) {
            is TracksSearchRequest -> {
                try {
                    apiService.searchTracks(term = dto.expression.trim()).apply { resultCode = 200 }
                } catch (httpException: HttpException) {
                    throw IOException(httpException)
                }
            }
            else -> throw IllegalArgumentException("Unsupported request type: ${dto::class.java}")
        }
    }
}