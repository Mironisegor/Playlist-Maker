package com.example.playlistmaker

import com.example.playlistmaker.data.dto.Track

sealed class SearchState {
    object Initial: SearchState()
    object Searching: SearchState()
    data class Success(val list: List<Track>): SearchState()
    data class Fail(val error: String): SearchState()
}