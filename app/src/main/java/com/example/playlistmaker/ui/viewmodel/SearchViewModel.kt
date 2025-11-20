package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.data.dto.SearchState
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.domain.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()

    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack = _selectedTrack.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()
    
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    
    init {
        loadSearchHistory()
    }
    
    fun setSearchText(text: String) {
        _searchText.value = text
    }
    
    fun setSelectedTrack(track: Track) {
        _selectedTrack.value = track
    }

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val query = whatSearch.trim()
            if (query.isEmpty()) {
                _searchScreenState.update { SearchState.Initial }
                return@launch
            }
            _searchText.value = query
            try {
                searchHistoryRepository.addSearchQuery(query)
                loadSearchHistory()
                
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = query)
                _searchScreenState.update { SearchState.Success(list = list) }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Fail(e.message.orEmpty()) }
            }
        }
    }

    fun resetSearchState() {
        _searchScreenState.update { SearchState.Initial }
    }
    
    private fun loadSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchHistory.value = searchHistoryRepository.getSearchHistory()
        }
    }
    
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.getTracksRepository(),
                        Creator.getSearchHistoryRepository()
                    ) as T
                }
            }
    }
}