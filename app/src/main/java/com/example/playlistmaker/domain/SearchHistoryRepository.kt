package com.example.playlistmaker.domain

interface SearchHistoryRepository {
    suspend fun addSearchQuery(query: String)
    suspend fun getSearchHistory(): List<String>
}

