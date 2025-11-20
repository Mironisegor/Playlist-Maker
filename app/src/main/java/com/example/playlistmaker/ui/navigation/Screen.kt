package com.example.playlistmaker.ui.navigation

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings"),
    PLAYLISTS("playlists"),
    CREATE_PLAYLIST("create_playlist"),
    PLAYLIST("playlist/{playlistId}"),
    TRACK_DETAILS("track_details"),
    FAVORITES("favorites")
}