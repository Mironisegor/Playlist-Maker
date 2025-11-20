package com.example.playlistmaker.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.playlistmaker.ui.activity.main.MainScreen
import com.example.playlistmaker.ui.activity.favorites.FavoritesScreen
import com.example.playlistmaker.ui.activity.playlist.CreatePlaylistScreen
import com.example.playlistmaker.ui.activity.playlist.PlaylistScreen
import com.example.playlistmaker.ui.activity.playlist.PlaylistsScreen
import com.example.playlistmaker.ui.activity.search.SearchScreen
import com.example.playlistmaker.ui.activity.settings.SettingsScreen
import com.example.playlistmaker.ui.activity.track.TrackDetailsScreen
import com.example.playlistmaker.ui.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.viewmodel.SearchViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun PlaylistHost(navController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )
    val playlistsViewModel = PlaylistViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.route
    ) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigateToSearch() },
                onNavigateToSettings = { navController.navigateToSettings() },
                onNavigateToPlaylists = { navController.navigate(Screen.PLAYLISTS.route) },
                onNavigateToFavorites = { navController.navigate(Screen.FAVORITES.route) }
            )
        }
        composable(Screen.PLAYLISTS.route) {
            PlaylistsScreen(
                modifier = Modifier.padding(6.dp),
                playlistViewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Screen.CREATE_PLAYLIST.route) },
                navigateToPlaylist = { playlistId ->
                    navController.navigate("playlist/$playlistId")
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.PLAYLIST.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            val playlist by playlistsViewModel.getPlaylist(playlistId).collectAsState(initial = null)
            
            playlist?.let { currentPlaylist ->
                PlaylistScreen(
                    playlist = currentPlaylist,
                    navigateBack = { navController.popBackStack() },
                    onNavigateToTrackDetails = { track ->
                        searchViewModel.setSelectedTrack(track)
                        navController.navigate(Screen.TRACK_DETAILS.route)
                    },
                    onDeletePlaylist = {
                        playlistsViewModel.deletePlaylistById(currentPlaylist.id)
                    },
                    onUpdateCoverImage = { playlistId, coverUri ->
                        playlistsViewModel.updatePlaylistCover(playlistId, coverUri)
                    }
                )
            }
        }

        composable(Screen.CREATE_PLAYLIST.route) {
            val newPlaylistViewModel: NewPlaylistViewModel = viewModel()
            CreatePlaylistScreen(
                onBack = { navController.popBackStack() },
                newPlaylistViewModel = newPlaylistViewModel
            )
        }

        composable(Screen.SEARCH.route) {
            SearchScreen(
                onBack = { navController.navigateBack() },
                modifier = Modifier.padding(6.dp),
                viewModel = searchViewModel,
                onNavigateToTrackDetails = { track ->
                    searchViewModel.setSelectedTrack(track)
                    navController.navigate(Screen.TRACK_DETAILS.route)
                }
            )
        }
        
        composable(Screen.TRACK_DETAILS.route) {
            val track by searchViewModel.selectedTrack.collectAsState()

            track?.let { currentTrack ->
                TrackDetailsScreen(
                    track = currentTrack,
                    onBack = { navController.popBackStack() },
                    playlistViewModel = playlistsViewModel
                )
            }
        }

        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBack = { navController.navigateBack() }
            )
        }
        composable(Screen.FAVORITES.route) {
            FavoritesScreen(
                playlistViewModel = playlistsViewModel,
                onNavigateToTrackDetails = { track ->
                    searchViewModel.setSelectedTrack(track)
                    navController.navigate(Screen.TRACK_DETAILS.route)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

fun NavController.navigateToSearch() {
    this.navigate(Screen.SEARCH.route)
}

fun NavController.navigateToSettings() {
    this.navigate(Screen.SETTINGS.route)
}

fun NavController.navigateBack() {
    this.popBackStack()
}