package com.example.playlistmaker.data.preferences

import com.example.playlistmaker.domain.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences
) : SearchHistoryRepository {
    override suspend fun addSearchQuery(query: String) {
        searchHistoryPreferences.addEntry(query)
    }

    override suspend fun getSearchHistory(): List<String> {
        return searchHistoryPreferences.getEntries()
    }
}

