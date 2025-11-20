package com.example.playlistmaker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history-preferences") + SupervisorJob())
) {
    private val preferencesKey = stringPreferencesKey("search_history")
    private val maxEntries = 10
    private val separator = ","
    fun addEntry(word: String) {
        if (word.isEmpty()) {
            return
        }

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[preferencesKey].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(separator).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(word)
                history.add(0, word)

                val limitedHistory = if (history.size > maxEntries) {
                    history.subList(0, maxEntries)
                } else {
                    history
                }
                val updatedString = limitedHistory.joinToString(separator)

                preferences[preferencesKey] = updatedString
            }
        }
    }

    suspend fun getEntries(): List<String> {
        return try {
            val preferences = dataStore.data.first()
            val historyString = preferences[preferencesKey].orEmpty()
            if (historyString.isEmpty()) {
                emptyList()
            } else {
                historyString.split(separator)
            }
        } catch (e: IOException) {
            emptyList()
        }
    }
}