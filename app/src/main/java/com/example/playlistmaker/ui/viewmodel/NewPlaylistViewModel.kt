package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlaylistViewModel : ViewModel() {

    private val playlistsRepository: PlaylistsRepository =
        Creator.getPlaylistsRepository(scope = viewModelScope)

    private val _coverImageUri = MutableStateFlow<String?>(null)

    val coverImageUri: StateFlow<String?> = _coverImageUri.asStateFlow()

    fun setCoverImageUri(uri: String?) {
        _coverImageUri.value = uri
    }

    fun createNewPlaylist(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(
                name = namePlaylist,
                description = description,
                coverImageUri = _coverImageUri.value
            )
            withContext(Dispatchers.Main) {
                _coverImageUri.value = null
            }
        }
    }
}