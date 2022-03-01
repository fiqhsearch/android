package com.fiqhsearcher.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.home.HomeScreen
import com.fiqhsearcher.screen.settings.SettingsScreen

@Composable
fun SearcherNavigator() {
    val controller = rememberNavController()
    val preferences = hiltViewModel<PreferencesViewModel>()
    NavHost(navController = controller, startDestination = "home") {

        composable("home") {
            HomeScreen(navigator = controller)
        }
        composable("settings") {
            SettingsScreen(navigator = controller)
        }
    }
}