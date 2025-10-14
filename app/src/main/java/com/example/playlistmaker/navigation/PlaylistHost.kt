package com.example.playlistmaker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.MainScreen
import com.example.playlistmaker.SearchScreen
import com.example.playlistmaker.SettingsScreen
import com.example.playlistmaker.viewmodel.SearchViewModel

@Composable
fun PlaylistHost(navController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.route
    ) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigateToSearch() },
                onNavigateToSettings = { navController.navigateToSettings() }
            )
        }

        composable(Screen.SEARCH.route) {
            SearchScreen(
                onBack = { navController.navigateBack() },
                modifier = Modifier.padding(6.dp),
                viewModel = searchViewModel
            )
        }

        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBack = { navController.navigateBack() }
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