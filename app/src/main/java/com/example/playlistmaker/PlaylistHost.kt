package com.example.playlistmaker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.MainScreen
import com.example.playlistmaker.SearchScreen
import com.example.playlistmaker.SettingsScreen

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings")
}

@Composable
fun PlaylistHost(navController: NavHostController) {
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
                onBack = { navController.navigateBack() }
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